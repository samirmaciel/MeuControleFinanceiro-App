package com.samirmaciel.nossocontrolefinanceiro.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
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
        setObservers()
        checkLogin()
    }

    private fun checkLogin() {
        isLoading(true)
        mViewModel?.checkLogin {
            if(it.first){
                mViewModel?.checkUserControl {
                    if(it.first){
                        isLoading(false)
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }else{
                        isLoading(false)
                        findNavController().navigate(R.id.action_loginFragment_to_lobbyFragment)
                    }
                }
            }else{
                isLoading(false)
            }
        }
    }

    private fun setObservers() {
        mViewModel?.emailInput?.observe(viewLifecycleOwner){
            setEnterButtonVisibility(mViewModel?.validateInputs())
        }

        mViewModel?.passwordInput?.observe(viewLifecycleOwner){
            setEnterButtonVisibility(mViewModel?.validateInputs())
        }
    }

    private fun setEnterButtonVisibility(validateInputs: Boolean?) {
        if(validateInputs == true){
            binding?.loginEnterButton?.isEnabled = true
            binding?.loginEnterButton?.setTextColor(resources.getColor(R.color.white))
        }else{
            binding?.loginEnterButton?.isEnabled = false
            binding?.loginEnterButton?.setTextColor(resources.getColor(R.color.darkBlue))
        }

    }

    private fun setViewModel() {
        mViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun setListeners() {

        binding?.loginEmailInput?.doOnTextChanged { text, start, before, count -> mViewModel?.emailInput?.value = text.toString()  }
        binding?.loginPasswordInput?.doOnTextChanged { text, start, before, count -> mViewModel?.passwordInput?.value = text.toString()  }

        binding?.loginEnterButton?.setOnClickListener {
            isLoading(true)
            mViewModel?.makeLogin{ isSucess, hasControl, message ->
                if(isSucess){
                    isLoading(false)
                    if(hasControl){
                        findNavController()?.navigate(R.id.action_loginFragment_to_homeFragment)
                    }else{
                        findNavController().navigate(R.id.action_loginFragment_to_lobbyFragment)
                    }

                }else{
                    isLoading(false)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }

        }
        binding?.loginRegisterButton?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerNewUserFragment)
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if(isLoading){
            binding?.loginProgressbar?.visibility = View.VISIBLE
            binding?.loginEnterButton?.visibility = View.GONE
            binding?.loginRegisterButton?.visibility = View.GONE
        }else{
            binding?.loginProgressbar?.visibility = View.GONE
            binding?.loginEnterButton?.visibility = View.VISIBLE
            binding?.loginRegisterButton?.visibility = View.VISIBLE
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