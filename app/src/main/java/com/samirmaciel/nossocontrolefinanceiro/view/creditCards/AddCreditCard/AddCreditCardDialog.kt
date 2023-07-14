package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.AddCreditCard

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.DialogAddCreditCardBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.InstallmentPurchase
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.util.MoneyTextWatcher
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter.InstallmentPurchasesAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel.AddCreditCardViewModel
import java.util.Date
import java.util.Locale

class AddCreditCardDialog(val parent: Fragment, val currentUser: User?, val onFinish: (CreditCard) -> Unit) : DialogFragment(R.layout.dialog_add_credit_card), AdapterView.OnItemSelectedListener{

    private var binding : DialogAddCreditCardBinding? = null
    private var viewModel: AddCreditCardViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setupBackgroundTransaparent()
        setObserver()
        setListeners()
        setupSpinnerDueDate()

        //dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun setListeners() {

        binding?.addCreditCardCashPurchasesValueInput?.addTextChangedListener(MoneyTextWatcher(binding?.addCreditCardCashPurchasesValueInput!!, Locale.getDefault()))
        binding?.addCreditCardLimitAvailableValueInput?.addTextChangedListener(MoneyTextWatcher(binding?.addCreditCardLimitAvailableValueInput!!, Locale.getDefault()))
        binding?.addCreditCardLimitTotalValueInput?.addTextChangedListener(MoneyTextWatcher(binding?.addCreditCardLimitTotalValueInput!!, Locale.getDefault()))

        binding?.addCreditCardAddCashPurchasesButton?.setOnClickListener {
            if(validateInstallmentPurchasesField()){
                viewModel?.addInstallmentPurchase(getInstallmentPurchase())
                clearInstallmentPurchaseFields()
            }else{
                Snackbar.make(requireView(), "Preencha todos os campos da compra parcelada!", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.addCreditCardConfirmButton?.setOnClickListener {
            if(validateCreditCardFields()){
                getCreditCard()
            }else{
                Snackbar.make(requireView(), "Preencha todos os campos do cartão de credito!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInstallmentPurchasesField(): Boolean {
        return !binding?.addCreditCardCashPurchasesDescriptionInput?.text.isNullOrEmpty() &&
                !binding?.addCreditCardCashPurchasesValueInput?.text.isNullOrEmpty() &&
                !binding?.addCreditCardCashPurchasesTotalQuantityInput?.text.isNullOrEmpty() &&
                !binding?.addCreditCardCashPurchasesPayedInput?.text.isNullOrEmpty()
    }

    private fun validateCreditCardFields(): Boolean {
        return !binding?.addCreditCardNameInput?.text.isNullOrEmpty() &&
                !binding?.addCreditCardLimitAvailableValueInput?.text.isNullOrEmpty() &&
                !binding?.addCreditCardLimitTotalValueInput?.text.isNullOrEmpty()
    }

    private fun getCreditCard() {
        val creditCard = CreditCard()

        creditCard.apply{
            id = "CC${Date().time}"
            description = binding?.addCreditCardNameInput?.text.toString()
            limitTotal = binding?.addCreditCardLimitTotalValueInput?.text.toString().toDouble()
            availableLimit = binding?.addCreditCardLimitAvailableValueInput?.text.toString().toDouble()
            user = currentUser
            dueDate = viewModel?.dueDate
        }
        viewModel?.addCreditCard(creditCard)
    }

    private fun clearInstallmentPurchaseFields() {
        binding?.addCreditCardCashPurchasesDescriptionInput?.text?.clear()
        binding?.addCreditCardCashPurchasesValueInput?.text?.clear()
        binding?.addCreditCardCashPurchasesTotalQuantityInput?.text?.clear()
        binding?.addCreditCardCashPurchasesPayedInput?.text?.clear()
    }

    private fun getInstallmentPurchase(): InstallmentPurchase {
        val installmentPurchase = InstallmentPurchase()

        installmentPurchase.description = binding?.addCreditCardCashPurchasesDescriptionInput?.text.toString()
        installmentPurchase.value = binding?.addCreditCardCashPurchasesValueInput?.text.toString().toDouble()
        installmentPurchase.totalInstallment = binding?.addCreditCardCashPurchasesTotalQuantityInput?.text.toString().toInt()
        installmentPurchase.installmentsPaid = binding?.addCreditCardCashPurchasesPayedInput?.text.toString().toInt()

        return installmentPurchase
    }

    private fun setObserver() {
        viewModel?.installmentsPurchasesList?.observe(viewLifecycleOwner){list ->
            setupAdapter(list)
        }

        viewModel?.currentCreditCard?.observe(viewLifecycleOwner){
            onFinish(it)
            dismiss()
        }
    }

    private fun setupSpinnerDueDate(){
        val numbers = (1..28).toList()

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numbers)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.addCreditCardDueDateSpinner?.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = this@AddCreditCardDialog
        }
    }

    private fun setupAdapter(list: MutableList<InstallmentPurchase>?) {
        val installmentPurchasesAdapter = InstallmentPurchasesAdapter()

        binding?.addCreditCardCashPurchasesRecyclerView?.apply {
            adapter = installmentPurchasesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        list?.let { mutableList ->
            installmentPurchasesAdapter.setInstallmentPurchasesList(mutableList)
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[AddCreditCardViewModel::class.java]
    }

    private fun setupBackgroundTransaparent() {
        dialog?.window?.setBackgroundDrawable(
            ColorDrawable(
                Color
                    .TRANSPARENT
            )
        )
    }

    private fun bindView(view: View) {
        binding = DialogAddCreditCardBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onItemSelected(list: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        viewModel?.dueDate = list?.getItemAtPosition(position).toString().toInt()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}