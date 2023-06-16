package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class CreditCardViewModel : ViewModel() {

    private val currentUser: MutableLiveData<User?> = MutableLiveData()
    private val currentControl: MutableLiveData<Control> = MutableLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val creditCardList : MutableLiveData<MutableList<CreditCard>?> = MutableLiveData()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val fireBaseUser = auth.currentUser

        fireBaseUser?.let {
            fireStore.collection(CollectionsNames.USERS).document(it.uid).get().addOnCompleteListener {
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
            fireStore.collection(CollectionsNames.CONTROLS).document(it).get().addOnCompleteListener {
                it.addOnSuccessListener {
                    val control = it.toObject(Control::class.java)

                    control?.let {
                        currentControl.value = it
                        creditCardList.value = it.creditCards
                    }
                }

                it.addOnFailureListener {

                }
            }
        }

    }
}