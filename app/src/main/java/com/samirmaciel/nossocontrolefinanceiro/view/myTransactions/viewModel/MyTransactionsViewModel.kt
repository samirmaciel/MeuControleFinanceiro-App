package com.samirmaciel.nossocontrolefinanceiro.view.myTransactions.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class MyTransactionsViewModel: ViewModel() {

    private val currentUser: MutableLiveData<User> = MutableLiveData()
    private val currentControl: MutableLiveData<Control> = MutableLiveData()
    private val fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val transactionsList: MutableLiveData<MutableList<Transaction>?> = MutableLiveData(null)

    init {
        loadCurrentUser()
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
        Log.d("FOIFOI", "loadCurrentControl: ")
        user.currentControlId?.let {currentControlID ->
            fireStore.collection(CollectionsNames.CONTROLS).document(currentControlID).get().addOnCompleteListener {
                it.addOnSuccessListener {
                    val control = it.toObject(Control::class.java)

                    control?.let {
                        Log.d("FOIFOI", "loadCurrentControl: " + it.transactions?.size)
                        currentControl.value = it
                        transactionsList.value = it.transactions

                    }
                }

                it.addOnFailureListener {

                }
            }
        }

    }


}