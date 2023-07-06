package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.util.FilterTransactionType

class CreditCardViewModel : ViewModel() {

    private val currentUser: MutableLiveData<User?> = MutableLiveData()
    private val currentControl: MutableLiveData<Control> = MutableLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val creditCardList: MutableLiveData<MutableList<CreditCard?>?> = MutableLiveData()
    val filterList: MutableLiveData<MutableList<Filter>?> = MutableLiveData()

    init {
        loadCurrentUser()
    }

    private fun loadFilter(){
        val currentControl = currentControl.value

        val filtersList = mutableListOf<Filter>()

        currentControl?.members?.forEach {
            filtersList.add(Filter(it.name!!, FilterTransactionType.USERNAME, false))
        }

        this.filterList.value = filtersList
    }


    private fun loadCreditCard(){
        val controlID = currentControl?.value?.id
        val collectionPath = "${CollectionsNames.CONTROLDATA}/${controlID}/CreditCards"

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

    fun saveCreditCard(creditCard: CreditCard) {
        creditCard.user = currentUser.value
        fireStore.collection(CollectionsNames.CONTROLDATA)
            .document(currentControl.value?.id!!).collection("CreditCards")
            .document(creditCard.id!!).set(creditCard).addOnCompleteListener {

                it.addOnSuccessListener {

                }
                it.addOnFailureListener {

                }
            }

    }

    private fun loadCurrentUser() {
        val fireBaseUser = auth.currentUser

        fireBaseUser?.let {
            fireStore.collection(CollectionsNames.USERS).document(it.uid).get()
                .addOnCompleteListener {
                    it.addOnSuccessListener {
                        val user = it.toObject(User::class.java)

                        user?.let {
                            currentUser.value = user
                            loadCurrentControl(user)
                        }
                    }
                    it.addOnFailureListener {

                    }
                }
        }
    }

    private fun loadCurrentControl(user: User) {

        user.currentControlId?.let {
            fireStore.collection(CollectionsNames.CONTROLS).document(it).get()
                .addOnCompleteListener {
                    it.addOnSuccessListener {
                        val control = it.toObject(Control::class.java)

                        control?.let {
                            currentControl.value = it
                            loadCreditCard()
                            loadFilter()
                        }
                    }

                    it.addOnFailureListener {

                    }
                }
        }

    }
}