package com.samirmaciel.nossocontrolefinanceiro.view.myTransactions.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class MyTransactionsViewModel: ViewModel() {

    private val currentUser: MutableLiveData<User> = MutableLiveData()
    private val currentControl: MutableLiveData<Control> = MutableLiveData()
    private val fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val transactions: MutableLiveData<MutableList<Transaction?>?> = MutableLiveData()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val firebaseUser = auth.currentUser

        firebaseUser?.let {
            fireStore.collection(Constants.USERS).document(it.uid).get().addOnCompleteListener {
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
        user.currentControlId?.let {currentControlID ->
            fireStore.collection(Constants.CONTROLS).document(currentControlID).get().addOnCompleteListener {
                it.addOnSuccessListener {
                    val control = it.toObject(Control::class.java)

                    control?.let {
                        currentControl.value = it
                        loadTransactions(it.id, user.id)
                    }
                }

                it.addOnFailureListener {

                }
            }
        }

    }

    private fun loadTransactions(controlID: String?, userID: String?) {
        controlID?.let {
            val collectionPath = "${Constants.CONTROLDATA}/${it}/Transactions"
            fireStore.collection(collectionPath).whereEqualTo("userID", userID).get().addOnCompleteListener {task ->
                task.addOnSuccessListener {
                    val transactionDocuments = task.result?.documents
                    if (transactionDocuments != null) {
                        val transactionList = mutableListOf<Transaction?>()
                        for (document in transactionDocuments) {
                            val transaction = document.toObject(Transaction::class.java)
                            transactionList.add(transaction)
                        }

                       transactions.value = transactionList

                    }
                }

                task.addOnFailureListener {

                }
            }
        }

    }


}