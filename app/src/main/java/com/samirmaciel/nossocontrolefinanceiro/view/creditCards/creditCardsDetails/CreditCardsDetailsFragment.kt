package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.creditCardsDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentCreditCardsDetailsBinding


class CreditCardsDetailsFragment : Fragment(R.layout.fragment_credit_cards_details) {

    private var binding: FragmentCreditCardsDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = FragmentCreditCardsDetailsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

}