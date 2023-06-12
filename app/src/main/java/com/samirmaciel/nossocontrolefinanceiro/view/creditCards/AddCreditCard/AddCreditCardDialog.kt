package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.AddCreditCard

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.DialogAddCreditCardBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.InstallmentPurchase
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter.InstallmentPurchasesAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel.AddCreditCardViewModel
import kotlin.random.Random

class AddCreditCardDialog(val currentUser: User?, val onFinish: (CreditCard) -> Unit) : DialogFragment(R.layout.dialog_add_credit_card) {

    private var binding : DialogAddCreditCardBinding? = null
    private var viewModel: AddCreditCardViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setupBackgroundTransaparent()
        setObserver()
        setListeners()
    }

    private fun setListeners() {
        binding?.addCreditCardAddCashPurchasesButton?.setOnClickListener {
            viewModel?.addInstallmentPurchase(getInstallmentPurchase())
            clearInstallmentPurchaseFields()
        }

        binding?.addCreditCardConfirmButton?.setOnClickListener {
            getCreditCard()
        }
    }

    private fun getCreditCard() {
        val creditCard = CreditCard()

        creditCard.apply{
            id = Random.nextInt(100, 900).toString()
            description = binding?.addCreditCardNameInput?.text.toString()
            limitTotal = binding?.addCreditCardLimitTotalValueInput?.text.toString().toDouble()
            availableLimit = binding?.addCreditCardLimitAvailableValueInput?.text.toString().toDouble()
            user = currentUser
        }
        viewModel?.addCrediCard(creditCard)
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
}