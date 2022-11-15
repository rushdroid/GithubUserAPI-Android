package com.example.rushabhtawkto.ui.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rushabhtawkto.databinding.FragmentUserDetailBinding
import com.example.tawktopractice.data.model.Userdetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailBinding
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var loginName: String
    private lateinit var userdetail: Userdetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loginName = it.getString("loginName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        fun newInstance() = UserDetailFragment()
    }
}