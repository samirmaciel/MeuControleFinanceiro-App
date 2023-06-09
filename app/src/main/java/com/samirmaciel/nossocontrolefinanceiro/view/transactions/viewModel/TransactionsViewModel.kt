package com.samirmaciel.nossocontrolefinanceiro.view.transactions.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.util.FilterTransactionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionsViewModel : ViewModel() {

    val filters: MutableLiveData<MutableList<Filter>> = MutableLiveData()
    val transactions: MutableLiveData<MutableList<Transaction?>?> = MutableLiveData()
    val currentUser: MutableLiveData<User> = MutableLiveData()
    val currentControl: MutableLiveData<Control> = MutableLiveData()

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        loadCurrentUser()
    }

    private fun loadTransactions(controlID: String) {
        val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date()).toString()

        val collectionPath = "${Constants.CONTROLDATA}/${controlID}/Transactions"
        mFireStore.collection(collectionPath).whereEqualTo("month", currentMonth).get()
            .addOnCompleteListener { task ->
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

    private fun loadCurrentControl(user: User?) {
        user?.currentControlId?.let {
            mFireStore.collection(Constants.CONTROLS).document(it).get()
                .addOnCompleteListener {

                    it.addOnSuccessListener {
                        val currentControl = it.toObject(Control::class.java)

                        this.currentControl.value = currentControl
                        loadTransactions(currentControl?.id!!)
                        loadDefaultFilters(currentControl)
                    }

                    it.addOnFailureListener {

                    }
                }
        }
    }

    private fun loadCurrentUser() {
        val firebaseUser = mAuth.currentUser
        if (firebaseUser != null) {
            mFireStore.collection(Constants.USERS).document(firebaseUser.uid).get()
                .addOnCompleteListener {

                    it.addOnSuccessListener {
                        val currentUser = it.toObject(User::class.java)

                        if (currentUser != null) {
                            this.currentUser.value = currentUser
                            loadCurrentControl(currentUser)
                        }
                    }

                    it.addOnFailureListener {

                    }
                }
        }
    }

    fun saveTransaction(transaction: Transaction?) {
        if (transaction != null) {
            transaction.user = currentUser.value
            transaction.userID = currentUser.value?.id
            mFireStore.collection(Constants.CONTROLDATA)
                .document(currentControl.value?.id!!).collection("Transactions")
                .document(transaction.id!!).set(transaction).addOnCompleteListener {

                    it.addOnSuccessListener {

                    }
                    it.addOnFailureListener {

                    }
                }
        }
    }
}