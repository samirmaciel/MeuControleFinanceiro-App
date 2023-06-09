package com.samirmaciel.nossocontrolefinanceiro.view.login.viewModel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class LoginViewModel : ViewModel() {

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val mFirebaseStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val emailInput: MutableLiveData<String> = MutableLiveData()
    val passwordInput: MutableLiveData<String> = MutableLiveData()


    fun checkLogin(onFinish: (Pair<Boolean, String?>) -> Unit){
        val fireBaseUser = mAuth.currentUser

        if(fireBaseUser != null){
            onFinish(Pair(true, fireBaseUser.displayName))
        }else{
            onFinish(Pair(false, ""))
        }
    }

    fun checkUserControl(onFinish: (Pair<Boolean, String?>) -> Unit){
        val fireBaseUser = mAuth.currentUser

        if(fireBaseUser != null){
            mFirebaseStore.collection("USERS").document(fireBaseUser.uid).get().addOnCompleteListener {
                it.addOnSuccessListener {
                    val user = it.toObject(User::class.java)

                    if(user?.currentControlId != null){
                        onFinish(Pair(true, user?.currentControlId))
                    }else{
                        onFinish(Pair(false, ""))
                    }
                }

                it.addOnFailureListener {

                }
            }
        }
    }

    fun makeLogin(onFinish : (Boolean, Boolean, String) -> Unit){
        mAuth.signInWithEmailAndPassword(emailInput.value.toString(), passwordInput.value.toString()).addOnCompleteListener {

            it.addOnSuccessListener {
                val firebaseUser = it.user

                getUserData(firebaseUser?.uid){ hasControl ->

                    onFinish(true, hasControl, firebaseUser?.displayName.toString())

                }

            }

            it.addOnFailureListener {
                onFinish(false, false, it.localizedMessage)
            }
        }
    }

    private fun getUserData(userID: String?, onFinish: (Boolean) -> Unit) {

        userID?.let{
            mFirebaseStore.collection(Constants.USERS).document(it).get().addOnCompleteListener {
                it.addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    if(user?.currentControlId != null){
                        onFinish(true)
                    }else{
                        onFinish(false)
                    }
                }
                it.addOnFailureListener {
                    onFinish(false)
                }
            }
        }

    }

    fun validateInputs() : Boolean {
        return validateEmail(emailInput.value) && validatePassword(passwordInput.value)
    }

    private fun validateEmail(email: String?) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()
    }

    private fun validatePassword(password: String?) : Boolean {
        return (password?.length ?: 0) >= 8
    }

}