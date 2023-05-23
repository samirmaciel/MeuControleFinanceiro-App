package com.samirmaciel.nossocontrolefinanceiro.view.categories.addCategoryDialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.AddCategoryDialogBinding

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