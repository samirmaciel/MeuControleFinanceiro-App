package com.samirmaciel.nossocontrolefinanceiro.view.categories.addCategoryDialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.DialogAddCategoryBinding

class AddCategoryDialog(val onFinish: (String) -> Unit) : DialogFragment(R.layout.dialog_add_category) {

    private var binding : DialogAddCategoryBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setListeners()
    }

    private fun setListeners() {
        binding?.addCategoryConfirmButton?.setOnClickListener {
            val newCategory = binding?.addCategoryNameInput?.text.toString()
            if(!newCategory.isNullOrEmpty()){
                onFinish(newCategory)
                dismiss()
            }else{
                Toast.makeText(requireContext(), "O nome da categoria não pode ser vazio!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun bindView(view: View) {
        binding = DialogAddCategoryBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}