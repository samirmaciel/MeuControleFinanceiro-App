package com.samirmaciel.nossocontrolefinanceiro.view.transactions.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.InstallmentPurchase
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction

class AddTransactionViewModel : ViewModel() {

    val categoriesList: MutableLiveData<List<String>?> = MutableLiveData()
    val creditCardList: MutableLiveData<List<CreditCard>?> = MutableLiveData()
    val currentControl: MutableLiveData<Control?> = MutableLiveData()

    private var transaction: Transaction? = null
    private var installmentPurchase: InstallmentPurchase? = null


    fun initialize(control: Control?){
        currentControl.value = control

        categoriesList.value = control?.categories
        creditCardList.value = control?.creditCards
    }

    fun getTransaction(): Transaction?{
        return transaction
    }

    fun getInstallmentPurchase(): InstallmentPurchase?{
        return installmentPurchase
    }

    fun setTransaction(newTransaction: Transaction?){
        transaction = newTransaction
    }

    fun setInstallmentPurchase(newInstallmentPurchase: InstallmentPurchase?){
        installmentPurchase = newInstallmentPurchase
    }
}