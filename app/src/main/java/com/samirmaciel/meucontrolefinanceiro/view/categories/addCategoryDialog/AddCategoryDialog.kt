package com.samirmaciel.meucontrolefinanceiro.view.categories.addCategoryDialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.samirmaciel.meucontrolefinanceiro.R
import com.samirmaciel.meucontrolefinanceiro.databinding.AddCategoryDialogBinding
import com.samirmaciel.meucontrolefinanceiro.databinding.AddCreditCardDialogBinding

class AddCategoryDialog : DialogFragment(R.layout.add_category_dialog) {

    private var binding : AddCategoryDialogBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = AddCategoryDialogBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}