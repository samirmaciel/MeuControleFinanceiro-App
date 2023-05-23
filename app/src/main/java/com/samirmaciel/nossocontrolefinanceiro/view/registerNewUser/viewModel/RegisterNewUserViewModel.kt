package com.samirmaciel.nossocontrolefinanceiro.view.registerNewUser.viewModel

import android.net.Uri
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.nossocontrolefinanceiro.R

class RegisterNewUserViewModel : ViewModel() {

    var emailInput: MutableLiveData<String> = MutableLiveData()
    var userNameInput: MutableLiveData<String> = MutableLiveData()
    var passwordInput: MutableLiveData<String> = MutableLiveData()
    var rePasswordInput: MutableLiveData<String> = MutableLiveData()
    var userImageUpload: MutableLiveData<Uri?> = MutableLiveData(null)
    var alertMessage: MutableLiveData<Inputs?> = MutableLiveData()


    fun validateInputs(): Boolean{
        if(validateEmail() && validateUserName() && validatePassword() && validateRePassword() && validateUserImageUpload()){
            alertMessage.value = null
            return true
        }else{
            return false
        }

    }

    private fun validateEmail() : Boolean {
        if(Patterns.EMAIL_ADDRESS.matcher(emailInput.value.toString()).matches()){
            return true
        }else{
            alertMessage.value = Inputs.EMAIL
            return false
        }

    }

    private fun validateUserName(): Boolean {
        if(!userNameInput.value.isNullOrEmpty()){
           return true
        }else{
            alertMessage.value = Inputs.USERNAME
            return false
        }
    }

    private fun validatePassword() : Boolean {
        if((passwordInput.value?.length ?: 0) >= 8){
            return true
        }else{
            alertMessage.value = Inputs.PASSWORD
            return false
        }

    }

    private fun validateRePassword() : Boolean {
        if(rePasswordInput.value.equals(passwordInput.value)){
            return true
        }else{
            alertMessage.value = Inputs.REPASSWORD
            return false
        }

    }

    private fun validateUserImageUpload() : Boolean {
        if(userImageUpload.value != null){
            return true
        }else{
           alertMessage.value = Inputs.IMAGE
           return false
        }

    }

    enum class Inputs(@StringRes val id: Int) {
        USERNAME(R.string.alert_message_username),
        PASSWORD(R.string.alert_message_password),
        REPASSWORD(R.string.alert_message_repassword),
        EMAIL(R.string.alert_message_email),
        IMAGE(R.string.alert_message_imageupload)
    }

}