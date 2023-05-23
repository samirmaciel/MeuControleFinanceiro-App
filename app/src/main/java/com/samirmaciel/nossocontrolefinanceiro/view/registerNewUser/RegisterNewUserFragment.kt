package com.samirmaciel.nossocontrolefinanceiro.view.registerNewUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentRegisterNewUserBinding


class RegisterNewUserFragment : Fragment(R.layout.fragment_register_new_user) {

   private var binding : FragmentRegisterNewUserBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setListeners()
    }

    private fun setListeners() {
        binding?.registerNewUserConfirmButton?.setOnClickListener {
            findNavController().navigate(R.id.action_registerNewUserFragment_to_lobbyFragment)
        }
    }

    private fun bindView(view: View) {
        binding = FragmentRegisterNewUserBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}