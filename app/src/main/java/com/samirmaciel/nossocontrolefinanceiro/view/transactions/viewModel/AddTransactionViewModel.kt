package com.samirmaciel.nossocontrolefinanceiro.view.transactions.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.InstallmentPurchase
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction

class AddTransactionViewModel : ViewModel() {

    val categoriesList: MutableLiveData<List<String>?> = MutableLiveData()
    val creditCardList: MutableLiveData<MutableList<CreditCard?>?> = MutableLiveData()
    val currentControl: MutableLiveData<Control?> = MutableLiveData()

    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var transaction: Transaction? = null
    private var installmentPurchase: InstallmentPurchase? = null


    fun initialize(control: Control?){
        currentControl.value = control
        categoriesList.value = control?.categories
        control?.let {
            loadCreditCards(control.id!!)
        }

    }

    private fun loadCreditCards(controlID: String) {
        val collectionPath = "${Constants.CONTROLDATA}/${controlID}/CreditCards"

        fireStore.collection(collectionPath).get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    val creditCardDocuments = task.result?.documents
                    if (creditCardDocuments != null) {
                        val creditCardList = mutableListOf<CreditCard?>()
                        for (document in creditCardDocuments) {
                            val creditCard = document.toObject(CreditCard::class.java)
                            creditCardList.add(creditCard)
                        }

                        this.creditCardList.value = creditCardList
                    }
                }
            }
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