package com.samirmaciel.nossocontrolefinanceiro.view.transactions.viewModel


import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class TransactionsViewModel : ViewModel() {

    val currentFilters: MutableLiveData<MutableList<Filter>> = MutableLiveData()
    val currentUser: MutableLiveData<User> = MutableLiveData()
    val currentControl: MutableLiveData<Control> = MutableLiveData()

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {

        loadCurrentUser()
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
        currentFilters.value = filtersList
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
}