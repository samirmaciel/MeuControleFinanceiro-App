package com.samirmaciel.nossocontrolefinanceiro.view.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentSplashBinding
import com.samirmaciel.nossocontrolefinanceiro.view.splash.viewModel.SplashViewModel

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var binding: FragmentSplashBinding? = null
    private var viewModel: SplashViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setObservers()


    }

    private fun setObservers() {
        viewModel?.userIsLogged?.observe(viewLifecycleOwner){result ->
            if(result){
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
    }

    private fun bindView(view: View) {
        binding = FragmentSplashBinding.bind(view)
    }


}