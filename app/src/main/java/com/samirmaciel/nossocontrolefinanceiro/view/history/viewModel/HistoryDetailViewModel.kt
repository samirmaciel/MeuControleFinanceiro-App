package com.samirmaciel.nossocontrolefinanceiro.view.history.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.FilterTransaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.util.FilterTransactionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryDetailViewModel: ViewModel() {

    val transactionsList : MutableLiveData<MutableList<Transaction?>> = MutableLiveData()
    val filters:  MutableLiveData<MutableList<Filter>> =  MutableLiveData()
    val currentUser: MutableLiveData<User> = MutableLiveData()
    val currentControl: MutableLiveData<Control> = MutableLiveData()
    private val fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    init {
        loadCurrentUser()
    }

    fun loadTransactions(controlID: String, month: String, year: String) {

        val collectionPath = "${Constants.CONTROLDATA}/${controlID}/Transactions"
        fireStore.collection(collectionPath).whereEqualTo("month", month).whereEqualTo("year", year).get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    val transactionDocuments = task.result?.documents
                    if (transactionDocuments != null) {
                        val transactionList = mutableListOf<Transaction?>()
                        for (document in transactionDocuments) {
                            val transaction = document.toObject(Transaction::class.java)
                            transactionList.add(transaction)
                        }

                        transactionsList.value = transactionList

                    }
                }
            }
    }

    private fun loadDefaultFilters(currentControl: Control?) {
        val filtersList = mutableListOf<Filter>()

        val highest = Filter("Maior", FilterTransactionType.HIGHESTVALUE, false)
        val smaller = Filter("Menor", FilterTransactionType.SMALLESTVALUE, false)

        filtersList.add(highest)
        filtersList.add(smaller)

        currentControl?.categories?.forEach {
            filtersList.add(Filter(it, FilterTransactionType.CATEGORY, false))
        }

        currentControl?.members?.forEach {
            filtersList.add(Filter(it.name!!, FilterTransactionType.USERNAME, false))
        }

        filtersList.reverse()
        filters.value = filtersList
    }

    private fun loadCurrentUser() {
        val fireBaseUser = auth.currentUser

        fireBaseUser?.let {
            fireStore.collection(Constants.USERS).document(it.uid).get()
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
        fireStore.collection(Constants.CONTROLS).document(controlID).get().addOnCompleteListener {

            it.addOnSuccessListener {documentSnapshot ->
                val control = documentSnapshot.toObject(Control::class.java)

                control?.let {_control ->
                    currentControl.value = _control
                    loadDefaultFilters(_control)
                }
            }

            it.addOnFailureListener {

            }
        }
    }

}