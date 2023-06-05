package com.samirmaciel.nossocontrolefinanceiro.view.transactions.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import java.util.Date

class TransactionsViewModel : ViewModel() {

    val filters: MutableLiveData<MutableList<Filter>> = MutableLiveData()
    val transactions: MutableLiveData<MutableList<Transaction>?> = MutableLiveData()
    val currentUser: MutableLiveData<User> = MutableLiveData()
    val currentControl: MutableLiveData<Control> = MutableLiveData()

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {

        loadCurrentUser()
        loadTransactions()
    }

    private fun loadTransactions() {
        val transactionsList = mutableListOf<Transaction>()

        val transaction1 = Transaction(category = "Conta", date = Date(), description = "Fatura RENNER", value = 300.50, user = User(image = "https://firebasestorage.googleapis.com/v0/b/nosso-controle-financeiro.appspot.com/o/USERSIMAGES%2FuCPJ6YDwhhcJ9u4DimAvlgiUWhq2?alt=media&token=f86da9f8-4cc8-4030-a4c6-e13359074645&_gl=1*nawo5n*_ga*MTQ4NTY4MTcxOS4xNjgzNjU4Mjkz*_ga_CW55HF8NVT*MTY4NTkyNTk4Ni45LjEuMTY4NTkyNTk5OS4wLjAuMA.."))
        val transaction2 = Transaction(category = "Conta", date = Date(), description = "Fatura NET", value = 49.50, user = User(image = "https://firebasestorage.googleapis.com/v0/b/nosso-controle-financeiro.appspot.com/o/USERSIMAGES%2FuCPJ6YDwhhcJ9u4DimAvlgiUWhq2?alt=media&token=f86da9f8-4cc8-4030-a4c6-e13359074645&_gl=1*nawo5n*_ga*MTQ4NTY4MTcxOS4xNjgzNjU4Mjkz*_ga_CW55HF8NVT*MTY4NTkyNTk4Ni45LjEuMTY4NTkyNTk5OS4wLjAuMA.."))
        val transaction3 = Transaction(category = "Conta", date = Date(), description = "Fatura NET", value = 50.50, user = User(image = "https://firebasestorage.googleapis.com/v0/b/nosso-controle-financeiro.appspot.com/o/USERSIMAGES%2FuCPJ6YDwhhcJ9u4DimAvlgiUWhq2?alt=media&token=f86da9f8-4cc8-4030-a4c6-e13359074645&_gl=1*nawo5n*_ga*MTQ4NTY4MTcxOS4xNjgzNjU4Mjkz*_ga_CW55HF8NVT*MTY4NTkyNTk4Ni45LjEuMTY4NTkyNTk5OS4wLjAuMA.."))
        val transaction4 = Transaction(category = "Conta", date = Date(), description = "Fatura NET", value = 9.50, user = User(image = "https://firebasestorage.googleapis.com/v0/b/nosso-controle-financeiro.appspot.com/o/USERSIMAGES%2FuCPJ6YDwhhcJ9u4DimAvlgiUWhq2?alt=media&token=f86da9f8-4cc8-4030-a4c6-e13359074645&_gl=1*nawo5n*_ga*MTQ4NTY4MTcxOS4xNjgzNjU4Mjkz*_ga_CW55HF8NVT*MTY4NTkyNTk4Ni45LjEuMTY4NTkyNTk5OS4wLjAuMA.."))

        transactionsList.add(transaction2)
        transactionsList.add(transaction1)
        transactionsList.add(transaction3)
        transactionsList.add(transaction4)

        transactions.value = transactionsList
    }

    private fun loadDefaultFilters(currentControl: Control?){
        val filtersList = mutableListOf<Filter>()

        val highest = Filter("Maior", false)
        val smaller = Filter("Menor", false)

        filtersList.add(highest)
        filtersList.add(smaller)

        currentControl?.members?.forEach {
            filtersList.add(Filter(it.name!!, false))
        }

        filtersList.reverse()
        filters.value = filtersList
    }

    private fun loadCurrentControl(user: User?){
        user?.currentControlId?.let {
            mFireStore.collection(CollectionsNames.CONTROLS).document(it).get().addOnCompleteListener {

                it.addOnSuccessListener {
                    val currentControl = it.toObject(Control::class.java)

                    this.currentControl.value = currentControl

                    loadDefaultFilters(currentControl)
                }

                it.addOnFailureListener {

                }
            }
        }
    }

    private fun loadCurrentUser(){
        val firebaseUser = mAuth.currentUser
        if(firebaseUser != null){
            mFireStore.collection(CollectionsNames.USERS).document(firebaseUser.uid).get().addOnCompleteListener {

                it.addOnSuccessListener {
                    val currentUser = it.toObject(User::class.java)

                    if(currentUser != null){
                        this.currentUser.value = currentUser
                        loadCurrentControl(currentUser)
                    }
                }

                it.addOnFailureListener {

                }
            }
        }
    }

    fun filterTransactionListToHighest(){
        var transactionList = transactions.value
        transactionList = transactionList?.sortedByDescending { it.value }?.toMutableList()
        transactions.value = transactionList
    }

    fun filterTransactionListToSmaller(){
        var transactionList = transactions.value
        transactionList = transactionList?.sortedBy { it.value }?.toMutableList()
        transactions.value = transactionList
    }
}