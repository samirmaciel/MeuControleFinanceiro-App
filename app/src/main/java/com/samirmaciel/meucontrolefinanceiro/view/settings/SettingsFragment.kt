package com.samirmaciel.meucontrolefinanceiro.view.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.samirmaciel.meucontrolefinanceiro.R
import com.samirmaciel.meucontrolefinanceiro.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var binding: FragmentSettingsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setListeners()
    }

    private fun setListeners() {
        binding?.settingsBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindView(view: View) {
        binding = FragmentSettingsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}