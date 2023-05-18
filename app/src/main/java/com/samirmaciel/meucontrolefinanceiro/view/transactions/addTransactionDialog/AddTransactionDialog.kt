package com.samirmaciel.meucontrolefinanceiro.view.transactions.addTransactionDialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.samirmaciel.meucontrolefinanceiro.R
import com.samirmaciel.meucontrolefinanceiro.databinding.AddTransactionDialogBinding

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