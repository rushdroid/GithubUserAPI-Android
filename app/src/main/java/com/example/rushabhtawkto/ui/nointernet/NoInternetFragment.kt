package com.example.rushabhtawkto.ui.nointernet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rushabhtawkto.databinding.FragmentNoInternetBinding
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 * Use the [NoInternetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoInternetFragment : Fragment() {

    private lateinit var binding: FragmentNoInternetBinding
    private var isInternet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoInternetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        internetObserver()
    }

    private fun initView() {
        binding.btnRetry.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun internetObserver() {
        val subscribe =
            ReactiveNetwork.observeNetworkConnectivity(context).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { connectivity ->
                    when {
                        connectivity.available() -> {
                            isInternet = true
                            initView()
                        }
                        else -> {
                            isInternet = false
                            initView()
                        }
                    }
                }
    }
}