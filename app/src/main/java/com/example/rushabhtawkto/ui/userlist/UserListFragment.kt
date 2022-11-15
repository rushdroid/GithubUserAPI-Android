package com.example.rushabhtawkto.ui.userlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rushabhtawkto.adapters.UserAdapter
import com.example.rushabhtawkto.adapters.UserLoadingStateAdapter
import com.example.rushabhtawkto.databinding.FragmentUserListBinding
import com.example.rushabhtawkto.utils.RecyclerViewItemDecoration
import com.example.tawktopractice.data.model.User
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private val viewModel: UserListViewModel by viewModels()

    private val adapter =
        UserAdapter { user: User -> Log.d("Click", ": ${user.login}") }

    private var searchJob: Job? = null

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
        startSearchJob()
    }

    @ExperimentalPagingApi
    private fun startSearchJob() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getUsers()
                .collectLatest {
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
                        showLoading()
                    }
                }

            }
        }
    }


    private fun showNoInternetView() {
        binding.recyclerView.isVisible = false
        binding.layoutNoInternet.ivErrorIcon.isVisible = true
        binding.layoutNoInternet.tvError.isVisible = true
    }

    private fun hideNoInternetView() {
        binding.recyclerView.isVisible = true
        binding.layoutNoInternet.ivErrorIcon.isVisible = false
        binding.layoutNoInternet.tvError.isVisible = false
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