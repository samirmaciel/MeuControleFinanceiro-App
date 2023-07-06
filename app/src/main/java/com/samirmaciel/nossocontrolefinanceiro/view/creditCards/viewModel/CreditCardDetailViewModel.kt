package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class CreditCardDetailViewModel: ViewModel() {

    val currentCreditCard : MutableLiveData<CreditCard> = MutableLiveData()
    val currentUser: MutableLiveData<User> = MutableLiveData()
    val currentControl: MutableLiveData<Control> = MutableLiveData()
    private val fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()


    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val fireBaseUser = auth.currentUser

        fireBaseUser?.let {
            fireStore.collection(CollectionsNames.USERS).document(it.uid).get()
                .addOnCompleteListener {
                    it.addOnSuccessListener {
                        val user = it.toObject(User::class.java)

                        user?.let {_user ->
                            currentUser.value = _user
                            user.currentControlId?.let {controlID ->
                                loadCurrentControl(controlID)
                            }
                        }
                    }
                    it.addOnFailureListener {

                    }
                }
        }
    }

    private fun loadCurrentControl(controlID: String){
        fireStore.collection(CollectionsNames.CONTROLS).document(controlID).get().addOnCompleteListener {

            it.addOnSuccessListener {documentSnapshot ->
                val control = documentSnapshot.toObject(Control::class.java)

                control?.let {_control ->
                    currentControl.value = _control
                }
            }

            it.addOnFailureListener {

            }
        }
    }

    fun loadCreditCard(credicardID: String){
        val controlID = currentControl.value?.id

        fireStore.collection(CollectionsNames.CONTROLDATA).document(controlID!!).collection("CreditCards").document(credicardID).get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    val creditCard = it.toObject(CreditCard::class.java)

                    creditCard?.let { _creditCard ->
                        currentCreditCard.value = _creditCard
                    }
                }

                task.addOnFailureListener {

                }
            }
    }
}