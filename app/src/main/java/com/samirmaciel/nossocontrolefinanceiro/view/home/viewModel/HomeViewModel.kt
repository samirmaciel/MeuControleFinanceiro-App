package com.samirmaciel.nossocontrolefinanceiro.view.home.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.view.home.HomeFragment

class HomeViewModel : ViewModel(){

    val currentUser : MutableLiveData<User?> = MutableLiveData()
    val currentControl : MediatorLiveData<Control?> = MediatorLiveData()
    var currentPageSelected = HomeFragment.TRANSACTIONSPAGE

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val mFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()


    init {

        currentControl.addSource(currentUser){
            loadCurrentControl(it)
        }

        loadCurrentUser()
    }

    private fun loadCurrentUser(){
        val firebaseUser = mAuth.currentUser

        if(firebaseUser != null){
            mFirestore.collection(Constants.USERS).document(firebaseUser.uid).get().addOnCompleteListener {

                it.addOnSuccessListener {
                    val currentUser = it.toObject(User::class.java)

                    if(currentUser != null){
                        this.currentUser.value = currentUser
                    }
                }

                it.addOnFailureListener {

                }
            }
        }
    }

    private fun loadCurrentControl(user: User?){

        user?.currentControlId?.let {
            mFirestore.collection(Constants.CONTROLS).document(it).get().addOnCompleteListener {

                it.addOnSuccessListener {
                    val currentControl = it.toObject(Control::class.java)

                    this.currentControl.value = currentControl
                }

                it.addOnFailureListener {

                }
            }
        }
    }


}