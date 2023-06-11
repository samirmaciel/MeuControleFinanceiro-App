package com.samirmaciel.nossocontrolefinanceiro.view.profile.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.DialogEditUserNameBinding

class EditUserNameDialog(val oldUserName: String, val onFinish: (String?) -> Unit) : DialogFragment(R.layout.dialog_edit_user_name) {

    private var binding: DialogEditUserNameBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBinding(view)
        setupUI()
        setListeners()
    }

    private fun setupUI() {
        binding?.dialogEditUserNameInput?.setText(oldUserName)
    }

    private fun setListeners() {
        binding?.dialogEditUserNameButton?.setOnClickListener {
            val newUserName = binding?.dialogEditUserNameInput?.text.toString()
            onFinish(newUserName)
            dismiss()
        }
    }

    private fun setBinding(view: View) {
        binding = DialogEditUserNameBinding.bind(view)
    }
}