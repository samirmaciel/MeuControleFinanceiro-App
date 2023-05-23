package com.samirmaciel.nossocontrolefinanceiro.view.creditCards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentCreditCardsBinding


class CreditCardsFragment : Fragment(R.layout.fragment_credit_cards) {

    private var binding : FragmentCreditCardsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = FragmentCreditCardsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}