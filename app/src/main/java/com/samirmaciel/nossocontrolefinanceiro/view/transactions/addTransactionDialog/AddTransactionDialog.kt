package com.samirmaciel.nossocontrolefinanceiro.view.transactions.addTransactionDialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.DialogAddTransactionBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.InstallmentPurchase
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.viewModel.AddTransactionViewModel
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransactionDialog(val control: Control?, val onFinish: (Transaction?, InstallmentPurchase?) -> Unit) : DialogFragment(R.layout.dialog_add_transaction),
    RadioGroup.OnCheckedChangeListener {

    private var binding: DialogAddTransactionBinding? = null
    private var viewModel: AddTransactionViewModel? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        //dialog.window?.attributes?.windowAnimations = R.style.CustomAnimationStyle
        return dialog
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setObservers()
        setupRadioGroup()
        setListeners()
        setupPaymentTypeSpinnerAdapter()
    }

    private fun setListeners() {
        binding?.addTransactionConfirmButton?.setOnClickListener {

            if(validateFields()){
                generateInputData()
                onFinish(viewModel?.getTransaction(), viewModel?.getInstallmentPurchase())
                dismiss()
            }else{
                Snackbar.make(requireView(), "Precisa preencher todos os campos!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateInputData() {
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toString()
        val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date()).toString()
        val currentDay =  SimpleDateFormat("dd", Locale.getDefault()).format(Date()).toString()

        val transaction = Transaction(id = "TR${Date().time}", day = currentDay, month = currentMonth, year = currentYear)
        var installmentPurchase: InstallmentPurchase? = null

        if(binding?.addTransactionRadioGroup?.checkedRadioButtonId == R.id.addTransaction_radioGroup_installmentPurchase){
            installmentPurchase = InstallmentPurchase(id = "IP${Date().time}")
            installmentPurchase.value = binding?.addTransactionInstallmentPurchasesValueInput?.text.toString().toDouble()
            installmentPurchase.installmentsPaid = 0
            installmentPurchase.totalInstallment = binding?.addTransactionInstallmentPurchasesTotalQuantityInput?.text.toString().toInt()
        }

        if(installmentPurchase != null){
            transaction.value = (installmentPurchase.totalInstallment ?: 0) * (installmentPurchase.value ?: 0.0)
            transaction.creditCardId = (binding?.addTransactionCreditCardSpinner?.selectedItem as CreditCard).id
        }else{
            transaction.value = binding?.addTransactionValue?.text.toString().toDouble()
        }

        transaction.date = Date()
        transaction.category = binding?.addTransactionCategorySpinner?.selectedItem.toString()
        transaction.type = binding?.addTransactionPaymentTypeSpinner?.selectedItem.toString()
        transaction.description = binding?.addTransactionDescription?.text.toString()

        viewModel?.setTransaction(transaction)
        viewModel?.setInstallmentPurchase(installmentPurchase)
    }

    private fun validateFields(): Boolean {
        if(binding?.addTransactionRadioGroup?.checkedRadioButtonId == R.id.addTransaction_radioGroup_installmentPurchase){
            return binding?.addTransactionDescription?.text.toString().isNotEmpty() &&
                    binding?.addTransactionInstallmentPurchasesValueInput?.text.toString().isNotEmpty() &&
                    binding?.addTransactionInstallmentPurchasesTotalQuantityInput?.text.toString().isNotEmpty()
        }else{
            return binding?.addTransactionDescription?.text.toString().isNotEmpty() &&
                    binding?.addTransactionValue?.text.toString().isNotEmpty()
        }

    }

    private fun setupRadioGroup() {
        binding?.addTransactionRadioGroup?.setOnCheckedChangeListener(this)
    }

    private fun setupPaymentTypeSpinnerAdapter() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.PaymentType, R.layout.spinner_item_layout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.addTransactionPaymentTypeSpinner?.adapter = adapter

        binding?.addTransactionPaymentTypeSpinner?.onItemSelectedListener = paymentTypeSpinnerListener
    }

    private fun setObservers() {
        viewModel?.categoriesList?.observe(viewLifecycleOwner){ list->
            list?.let {
                setupCategoriesSpinnerAdapter(it)
            }
        }

        viewModel?.creditCardList?.observe(viewLifecycleOwner){list->
            list?.let {
                setupCreditCardSpinnerAdapter(it)
            }

        }
    }

    private fun setupCreditCardSpinnerAdapter(list: List<CreditCard?>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.addTransactionCreditCardSpinner?.adapter = adapter

    }

    private fun setupCategoriesSpinnerAdapter(list: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.addTransactionCategorySpinner?.adapter = adapter

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[AddTransactionViewModel::class.java]
        viewModel?.initialize(control)
    }

    private fun bindView(view: View) {
        binding = DialogAddTransactionBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private val paymentTypeSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(list: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

            if((list?.getItemAtPosition(position) == "Debito" || list?.getItemAtPosition(position) == "Dinheiro")){
                binding?.addTransactionCreditCardCardview?.visibility = View.GONE
                binding?.addTransactionIsInsallmentPurchaseCardview?.visibility = View.GONE
            }else{
                binding?.addTransactionCreditCardCardview?.visibility = View.VISIBLE
                binding?.addTransactionIsInsallmentPurchaseCardview?.visibility = View.VISIBLE
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

    }

    override fun onCheckedChanged(radioGroup: RadioGroup?, checked: Int) {
        when(checked){
            R.id.addTransaction_radioGroup_cash-> {
                binding?.addTransactionInstallmentPurchaseCardView?.visibility = View.GONE
                binding?.addTransactionValueCardView?.visibility = View.VISIBLE
            }

            R.id.addTransaction_radioGroup_installmentPurchase -> {
                binding?.addTransactionInstallmentPurchaseCardView?.visibility = View.VISIBLE
                binding?.addTransactionValueCardView?.visibility = View.GONE
            }
        }
    }


}