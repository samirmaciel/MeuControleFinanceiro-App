package com.samirmaciel.meucontrolefinanceiro.view.myCreditCards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.samirmaciel.meucontrolefinanceiro.R
import com.samirmaciel.meucontrolefinanceiro.databinding.FragmentMyCreditCardsBinding

class MyCreditCardsFragment : Fragment(R.layout.fragment_my_credit_cards) {

    private var binding : FragmentMyCreditCardsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setListeners()
    }

    private fun setListeners() {
        binding?.myCreditCardsBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindView(view: View) {
        binding = FragmentMyCreditCardsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}