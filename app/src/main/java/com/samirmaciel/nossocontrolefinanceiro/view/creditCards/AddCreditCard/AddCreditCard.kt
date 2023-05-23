package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.AddCreditCard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.AddCreditCardDialogBinding

class AddCreditCard : DialogFragment(R.layout.add_credit_card_dialog) {

    private var binding : AddCreditCardDialogBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = AddCreditCardDialogBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}