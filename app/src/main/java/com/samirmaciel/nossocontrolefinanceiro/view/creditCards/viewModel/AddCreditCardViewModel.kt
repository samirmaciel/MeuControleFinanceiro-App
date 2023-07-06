package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.InstallmentPurchase

class AddCreditCardViewModel : ViewModel() {

    val installmentsPurchasesList: MutableLiveData<MutableList<InstallmentPurchase>?> = MutableLiveData(
        mutableListOf()
    )
    val currentCreditCard: MutableLiveData<CreditCard> = MutableLiveData()
    var dueDate: Int? = null

    fun addInstallmentPurchase(installmentPurchase: InstallmentPurchase){
        val oldList = installmentsPurchasesList.value
        oldList?.add(installmentPurchase)

        installmentsPurchasesList.value = oldList
    }

    fun removeInstallmentPurchase(installmentPurchase: InstallmentPurchase){
        installmentsPurchasesList.value?.remove(installmentPurchase)
    }

    fun addCreditCard(creditCard: CreditCard) {
        creditCard.installmentPurchases = installmentsPurchasesList.value
        currentCreditCard.value = creditCard
    }

}