package com.samirmaciel.nossocontrolefinanceiro.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentLoginBinding
import com.samirmaciel.nossocontrolefinanceiro.view.login.viewModel.LoginViewModel


class LoginFragment : Fragment(R.layout.fragment_login) {

    private var binding : FragmentLoginBinding? = null
    private var mViewModel : LoginViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        setListeners()
        setViewModel()
    }

    private fun setViewModel() {
        mViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun setListeners() {
        binding?.loginEnterButton?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_lobbyFragment)
        }
        binding?.loginRegisterButton?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerNewUserFragment)
        }
    }

    private fun bindView(view: View) {
        binding = FragmentLoginBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}