package com.example.rushabhtawkto.ui.userdetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rushabhtawkto.R
import com.example.rushabhtawkto.databinding.FragmentUserDetailBinding

class UserDetailFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailBinding
    private lateinit var viewModel: UserDetailViewModel

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