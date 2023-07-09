package com.samirmaciel.nossocontrolefinanceiro.view.history.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.MonthTransactions
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.util.FilterTransactionType

class HistoryViewModel : ViewModel() {

    val currentUser: MutableLiveData<User> = MutableLiveData()
    val currentControl: MutableLiveData<Control> = MutableLiveData()
    val monthsTransactionsList: MutableLiveData<List<MonthTransactions>?> = MutableLiveData()
    val yearsFilterList: MutableLiveData<List<Filter?>> = MutableLiveData()

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()


    init {
        loadCurrentUser()
    }

    private fun loadTransactions(controlID: String) {
        //val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date()).toString()

        val collectionPath = "${Constants.CONTROLDATA}/${controlID}/Transactions"
        mFireStore.collection(Constants.CONTROLDATA).document(controlID).collection("Transactions")
            .get()
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    val transactionDocuments = task.result?.documents
                    if (transactionDocuments != null) {
                        val transactionList = mutableListOf<Transaction?>()
                        for (document in transactionDocuments) {
                            val transaction = document.toObject(Transaction::class.java)
                            transactionList.add(transaction)
                        }

                        val pairYearsMonthsTransactions = processTransactions(transactionList)

                        monthsTransactionsList.value = pairYearsMonthsTransactions.second
                        yearsFilterList.value = pairYearsMonthsTransactions.first.map {
                            Filter(
                                name = it!!,
                                filterType = FilterTransactionType.CATEGORY
                            )
                        }

                    }
                }
            }
    }

    private fun processTransactions(transactionList: MutableList<Transaction?>): Pair<List<String?>, List<MonthTransactions>> {
        val monthTransactions = arrayListOf<MonthTransactions>()
        val months: HashMap<String, ArrayList<Transaction?>> = hashMapOf()
        val years: HashMap<String, ArrayList<Transaction?>> = hashMapOf()

        // Generate year transactions
        transactionList.forEach { transaction ->
            if (years[transaction?.year] != null) {
                years[transaction?.year]?.add(transaction)
            } else {
                transaction?.year?.let { year ->
                    years.put(year, arrayListOf())
                    years[year]?.add(transaction)
                }
            }
        }

        // Generate months transactions
        years.forEach { map ->
            map.value.forEach { transaction ->
                if (months[transaction?.month] != null) {
                    months[transaction?.month]?.add(transaction)
                } else {
                    transaction?.month?.let { monthName ->
                        months.put(monthName, arrayListOf())
                        months[monthName]?.add(transaction)
                    }
                }
            }

            months.map { it.value }.forEach { transactions ->
                val newMonthTransaction = MonthTransactions()
                val totalValue = getTotalValueFromTransactions(transactions)
                newMonthTransaction.transactions = transactions
                newMonthTransaction.monthName = transactions[0]?.month
                newMonthTransaction.year = map.key
                newMonthTransaction.totalValue = totalValue
                monthTransactions.add(newMonthTransaction)
            }
        }

        return Pair(years.map { it.key }, monthTransactions)

    }

    private fun getTotalValueFromTransactions(transactionList: ArrayList<Transaction?>): Double {
        var result: Double = 0.0

        transactionList.forEach {
            result += it?.value ?: 0.0
        }

        return result
    }


    private fun loadCurrentControl(user: User?) {
        user?.currentControlId?.let {
            mFireStore.collection(Constants.CONTROLS).document(it).get()
                .addOnCompleteListener {

                    it.addOnSuccessListener {
                        val currentControl = it.toObject(Control::class.java)

                        this.currentControl.value = currentControl
                        loadTransactions(currentControl?.id!!)

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
}