package com.samirmaciel.meucontrolefinanceiro.view.lobby

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.samirmaciel.meucontrolefinanceiro.R
import com.samirmaciel.meucontrolefinanceiro.databinding.FragmentLobbyBinding
import com.samirmaciel.meucontrolefinanceiro.databinding.FragmentLoginBinding


class LobbyFragment : Fragment(R.layout.fragment_lobby) {

    private var binding : FragmentLobbyBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = FragmentLobbyBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}