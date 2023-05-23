package com.samirmaciel.nossocontrolefinanceiro.view.lobby

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentLobbyBinding


class LobbyFragment : Fragment(R.layout.fragment_lobby) {

    private var binding : FragmentLobbyBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setListeners()
    }

    private fun setListeners() {
        binding?.lobbyEnterButton?.setOnClickListener {
            findNavController().navigate(R.id.action_lobbyFragment_to_homeFragment)
        }
    }

    private fun bindView(view: View) {
        binding = FragmentLobbyBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}