package com.example.rushabhtawkto.ui.userdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.rushabhtawkto.R
import com.example.rushabhtawkto.databinding.FragmentUserDetailBinding
import com.example.rushabhtawkto.utils.Resource
import com.example.tawktopractice.data.model.UserDetail
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()
    private lateinit var loginName: String
    private lateinit var userdetail: UserDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loginName = it.getString("loginName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserDetails()
        initListener()
    }


    private fun getUserDetails() {
        viewModel.userDetail.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    hideLoadingView()
                    if (!this::userdetail.isInitialized) {
                        showNoInternetView()
                    } else {
                        hideNoInternetView()
                    }
                }
                is Resource.Loading -> {
                    Log.d("TAG", "getUserDetail: Loading")
                    showLoadingView()
                }
                is Resource.Success -> {
                    hideLoadingView()
                    it.data?.let { it1 ->
                        userdetail = it1
                        initDetailView()
                        hideNoInternetView()
                    }
                }
            }
        }
        viewModel.noteData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.getUserDetail(login = loginName)
    }

    private fun initDetailView() {
        binding.tvFollowers.text =
            getString(R.string.text_followers, userdetail.followers.toString())
        binding.tvFollowing.text =
            getString(R.string.text_following, userdetail.following.toString())
        binding.tvName.text = getString(R.string.text_name, userdetail.name)
        binding.tvCompany.text = getString(R.string.text_company, userdetail.company)
        binding.tvBlog.text = getString(R.string.text_blog, userdetail.blog)
        userdetail.note.let {
            binding.etNote.setText(it)
        }
        Glide.with(requireContext()).load(userdetail.avatarUrl).into(binding.ivProfile)
        initToolbar()
    }

    private fun initListener() {
        binding.materialButton.setOnClickListener {
            if (binding.etNote.text.isNotEmpty()) {
                userdetail.note = binding.etNote.text.toString().trim()
                viewModel.saveUserNote(userdetail = userdetail)
            } else {
                userdetail.note = ""
                viewModel.saveUserNote(userdetail = userdetail)
            }
        }
        internetObserver()
    }

    private fun initToolbar() {
        requireActivity().title = userdetail.name
    }

    private fun showNoInternetView() {
        binding.layoutParent.isVisible = false
        binding.layoutNoInternet.ivErrorIcon.isVisible = true
        binding.layoutNoInternet.tvError.isVisible = true
    }

    private fun hideNoInternetView() {
        binding.layoutParent.isVisible = true
        binding.layoutNoInternet.ivErrorIcon.isVisible = false
        binding.layoutNoInternet.tvError.isVisible = false
    }

    private fun showLoadingView() {
        binding.layoutShimmerview.isVisible = true
        binding.layoutParent.isVisible = false
        binding.layoutNoInternet.ivErrorIcon.isVisible = false
        binding.layoutNoInternet.tvError.isVisible = false
    }

    private fun hideLoadingView() {
        binding.layoutShimmerview.isVisible = false
        binding.layoutParent.isVisible = true
        binding.layoutNoInternet.ivErrorIcon.isVisible = false
        binding.layoutNoInternet.tvError.isVisible = false
    }

    private fun internetObserver() {
        val subscribe =
            ReactiveNetwork.observeNetworkConnectivity(context).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { connectivity ->
                    when {
                        connectivity.available() -> {
                            if (!this::userdetail.isInitialized) {
//                                getUserDetails()
                            }
                        }
                        else -> {
                            val login = (this::userdetail.isInitialized)
                            if (!login) {
                                showNoInternetView()
                            }
                        }
                    }
                }
    }
}