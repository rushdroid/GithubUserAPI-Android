package com.example.rushabhtawkto.ui.userlist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rushabhtawkto.R
import com.example.rushabhtawkto.adapters.UserAdapter
import com.example.rushabhtawkto.adapters.UserLoadingStateAdapter
import com.example.rushabhtawkto.databinding.FragmentUserListBinding
import com.example.rushabhtawkto.utils.RecyclerViewItemDecoration
import com.example.tawktopractice.data.model.User
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private val viewModel: UserListViewModel by viewModels()
    private var isSearch = false
    private val adapter =
        UserAdapter { user: User ->
            val action = UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(
                id = user.id!!, loginName = user.login
            )
            action.let { findNavController().navigate(it) }
        }

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.addLoadStateListener { loadState ->
            if (loadState.mediator?.refresh is LoadState.Loading) {
                if (adapter.snapshot().isEmpty()) {
                    showLoading()
                }
            } else {
                hideLoading()
                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                        val action =
                            UserListFragmentDirections.actionUserListFragmentToNoInternetFragment2(
                                isInternet = false
                            )
                        action.let { findNavController().navigate(it) }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(layoutInflater)
        return binding.root
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        initListener()
        initToolbar()
        if (binding.searchView.query.toString().isBlank()) {
            startSearchJob()
        } else {
            startSearchJob(query = binding.searchView.query.toString().trim())
        }
    }

    @ExperimentalPagingApi
    private fun startSearchJob() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getUsers().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    @ExperimentalPagingApi
    private fun startSearchJob(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getUsers(
                query = query
            ).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setUpAdapter() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(RecyclerViewItemDecoration())
        }
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = UserLoadingStateAdapter { retry() }
        )
    }

    private fun initToolbar() {
        requireActivity().title = getString(R.string.text_user_list)
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun initListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                startSearchJob(query = binding.searchView.query.toString().trim())
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                isSearch = newText.isNotBlank()
                if (newText.isBlank()) {
                    startSearchJob()
                }
                return false
            }
        });
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun internetObserver() {
        val subscribe =
            ReactiveNetwork.observeNetworkConnectivity(context).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { connectivity ->
                    when {
                        connectivity.available() -> {

                        }
                        else -> {

                        }
                    }
                }
    }

    private fun showLoading() {
        binding.linearShimmerview.isVisible = true
        binding.recyclerView.isVisible = false
        binding.layoutNoInternet.ivErrorIcon.isVisible = false
        binding.layoutNoInternet.tvError.isVisible = false
    }

    private fun hideLoading() {
        binding.linearShimmerview.isVisible = false
        binding.recyclerView.isVisible = true
        binding.layoutNoInternet.ivErrorIcon.isVisible = false
        binding.layoutNoInternet.tvError.isVisible = false
    }

    private fun retry() {
        adapter.retry()
    }
}