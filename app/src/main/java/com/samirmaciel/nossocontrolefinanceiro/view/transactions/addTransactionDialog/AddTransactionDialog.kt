package com.samirmaciel.nossocontrolefinanceiro.view.transactions.addTransactionDialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.AddTransactionDialogBinding

class AddTransactionDialog : DialogFragment(R.layout.add_transaction_dialog) {

    private var binding: AddTransactionDialogBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = AddTransactionDialogBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}