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

    val creditCardsList: MutableLiveData<MutableList<CreditCard>?> = MutableLiveData()

    init {
        loadCurrentUser()
    }

    fun addCreditCard(creditCard: CreditCard){
        val control = currentControl.value

        if(control?.creditCards.isNullOrEmpty()){
            val newList = mutableListOf<CreditCard>()
            newList.add(creditCard)
            control?.creditCards = newList
        }else{
            control?.creditCards?.add(creditCard)
        }

        updateControl(control){ isSucess, message ->

        }

    }

    private fun updateControl(newControl: Control?, onFinish: (Boolean, String) -> Unit) {

        newControl?.id?.let {
            fireStore.collection(CollectionsNames.CONTROLS).document(it).set(newControl).addOnCompleteListener {
                it.addOnSuccessListener {
                    currentControl.value = newControl
                    creditCardsList.value = newControl.creditCards
                    onFinish(true, "Controle atualizado com sucesso!")
                }

                it.addOnFailureListener {

                }
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
                        creditCardsList.value = it.creditCards
                    }
                }

                it.addOnFailureListener {

                }
            }
        }

    }
}