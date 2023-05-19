package com.samirmaciel.meucontrolefinanceiro.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.samirmaciel.meucontrolefinanceiro.R
import com.samirmaciel.meucontrolefinanceiro.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var binding : FragmentProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setListeners()
    }

    private fun setListeners() {
        binding?.profileCategoriesButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_categoriesFragment)
        }

        binding?.profileMyTransactionsButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myTransactionsFragment)
        }

        binding?.profileMyCreditCardsButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myCreditCardsFragment)
        }

        binding?.profileSettingsButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        binding?.profileBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindView(view: View) {
        binding = FragmentProfileBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}