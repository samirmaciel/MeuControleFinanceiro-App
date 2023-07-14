package com.samirmaciel.nossocontrolefinanceiro.view.splash.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userIsLogged: MutableLiveData<Boolean> = MutableLiveData()


    init {
        checkLogin()
    }

    private fun checkLogin(){
        val fireBaseUser = auth.currentUser

        userIsLogged.value = fireBaseUser != null
    }
}