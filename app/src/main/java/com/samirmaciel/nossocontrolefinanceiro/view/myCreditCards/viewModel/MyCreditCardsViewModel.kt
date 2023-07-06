package com.samirmaciel.nossocontrolefinanceiro.view.myCreditCards.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class MyCreditCardsViewModel : ViewModel() {

    val currentUser: MutableLiveData<User> = MutableLiveData()
    private val currentControl: MutableLiveData<Control?> = MutableLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val creditCardList: MutableLiveData<MutableList<CreditCard?>> = MutableLiveData()

    init {
        loadCurrentUser()
    }

    private fun loadCreditCard(){
        val controlID = currentControl?.value?.id
        val collectionPath = "${CollectionsNames.CONTROLDATA}/${controlID}/CreditCards"

        fireStore.collection(collectionPath).get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    val transactionDocuments = task.result?.documents
                    if (transactionDocuments != null) {
                        val creditCardList = mutableListOf<CreditCard?>()
                        for (document in transactionDocuments) {
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
        val firebaseUser = auth.currentUser

        firebaseUser?.let {
            fireStore.collection(CollectionsNames.USERS).document(it.uid).get().addOnCompleteListener {
                it.addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    user?.let {
                        currentUser.value = it
                        loadCurrentControl(it)
                    }
                }

                it.addOnFailureListener {

                }
            }
        }
    }

    private fun loadCurrentControl(user: User) {

        user?.currentControlId?.let {currentControlID ->
            fireStore.collection(CollectionsNames.CONTROLS).document(currentControlID).get().addOnCompleteListener {
                it.addOnSuccessListener {
                    val control = it.toObject(Control::class.java)

                    control?.let {
                        currentControl.value = it
                        loadCreditCard()
                    }
                }

                it.addOnFailureListener {

                }
            }
        }

    }
}