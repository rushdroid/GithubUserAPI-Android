package com.example.rushabhtawkto.ui.userdetail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rushabhtawkto.R
import com.example.rushabhtawkto.ui.compose.UserDetailScreen
import com.example.rushabhtawkto.utils.Resource
import com.example.tawktopractice.data.model.UserDetail
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private val viewModel: UserDetailViewModel by viewModels()
    private lateinit var loginName: String
    private lateinit var userdetail: UserDetail

    private var followersCountState = mutableStateOf("")
    private var followingCountState = mutableStateOf("")
    private var nameState = mutableStateOf("")
    private var companyState = mutableStateOf("")
    private var blogState = mutableStateOf("")
    private var noteState = mutableStateOf("")
    private var profileImageState = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loginName = it.getString("loginName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                UserDetailScreen(
                    followersCount = followersCountState.value,
                    followingCount = followingCountState.value,
                    name = nameState.value,
                    company = companyState.value,
                    blog = blogState.value,
                    note = noteState.value,
                    profileImage = profileImageState.value,
                    onNoteChanged = {
                        noteState.value = it
                    },
                    onSaveClicked = {
                        userdetail.note = noteState.value.trim()
                        viewModel.saveUserNote(userdetail = userdetail)
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModel.userDetail.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    if (!this::userdetail.isInitialized) {
                        internetObserver()
                    }
                }
                is Resource.Loading -> {
                    Log.d("TAG", "getUserDetail: Loading")
                }
                is Resource.Success -> {
                    it.data?.let { it1 ->
                        userdetail = it1
                        initDetailView()
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
        Handler(Looper.getMainLooper()).post {
            followersCountState.value =
                getString(R.string.text_followers, userdetail.followers.toString())
            followingCountState.value =
                getString(R.string.text_following, userdetail.following.toString())
            nameState.value = getString(R.string.text_name, userdetail.name)
            companyState.value = getString(R.string.text_company, userdetail.company)
            blogState.value = getString(R.string.text_blog, userdetail.blog)
            userdetail.note.let { noteState.value = it ?: "" }
            profileImageState.value = userdetail.avatarUrl ?: ""
        }
        initToolbar()
    }

    private fun initToolbar() {
        requireActivity().title = userdetail.name
    }

    private fun internetObserver() {
        val subscribe =
            ReactiveNetwork.observeNetworkConnectivity(context).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { connectivity ->
                    when {
                        connectivity.available() -> {
                            if (!this::userdetail.isInitialized) {

                            }
                        }
                        else -> {
                            val login = (this::userdetail.isInitialized)
                            if (!login) {

                            }
                        }
                    }
                }
    }
}

