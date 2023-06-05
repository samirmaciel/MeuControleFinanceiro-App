package com.samirmaciel.nossocontrolefinanceiro.view.registerNewUser.viewModel

import android.net.Uri
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class RegisterNewUserViewModel : ViewModel() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val mStore: FirebaseStorage = FirebaseStorage.getInstance()

    var emailInput: MutableLiveData<String> = MutableLiveData()
    var userNameInput: MutableLiveData<String> = MutableLiveData()
    var passwordInput: MutableLiveData<String> = MutableLiveData()
    var rePasswordInput: MutableLiveData<String> = MutableLiveData()
    var userImageUpload: MutableLiveData<Uri?> = MutableLiveData(null)
    var alertMessage: MutableLiveData<Inputs?> = MutableLiveData()


    fun createNewUser(onFinish: (Pair<Boolean, String?>) -> Unit) {

        mAuth.createUserWithEmailAndPassword(
            emailInput.value.toString(),
            passwordInput.value.toString()
        ).addOnCompleteListener {

            it.addOnSuccessListener {

                val firebaseUser = it.user

                if (firebaseUser != null) {
                    uploadUserImage(firebaseUser.uid){ result ->
                        if(result != null){
                            val newUser = User(id = firebaseUser.uid, name = userNameInput.value, image = result)

                            registerUserProfile(newUser){
                                onFinish(it)
                            }

                        }else{
                            onFinish(Pair(false, result))
                        }
                    }


                } else {
                    onFinish(Pair(false, ""))
                }
            }
        }

    }

    private fun uploadUserImage(userID: String, onFinish: (String?) -> Unit) {
        val storageRef = mStore.reference
        val imageRef = storageRef.child(CollectionsNames.USERSIMAGES)
        val fileRef = imageRef.child(userID)

        val uploadTask = fileRef.putFile(userImageUpload.value!!)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                onFinish(null)
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                onFinish(downloadUri.toString())
            } else {
                onFinish(task.exception?.localizedMessage)
            }
        }

    }


    private fun registerUserProfile(user: User, onFinish: (Pair<Boolean, String?>) -> Unit) {
        mFireStore.collection(CollectionsNames.USERS).document(user.id.toString()).set(user)
            .addOnCompleteListener {

                it.addOnSuccessListener {
                    onFinish(Pair(true, user.name.toString()))
                }

                it.addOnFailureListener {
                    onFinish(Pair(false, it.localizedMessage))
                }
            }
    }


    fun validateInputs(): Boolean {
        if (validateEmail() && validateUserName() && validatePassword() && validateRePassword() && validateUserImageUpload()) {
            alertMessage.value = null
            return true
        } else {
            return false
        }

    }

    private fun validateEmail(): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(emailInput.value.toString()).matches()) {
            return true
        } else {
            alertMessage.value = Inputs.EMAIL
            return false
        }

    }

    private fun validateUserName(): Boolean {
        if (!userNameInput.value.isNullOrEmpty()) {
            return true
        } else {
            alertMessage.value = Inputs.USERNAME
            return false
        }
    }

    private fun validatePassword(): Boolean {
        if ((passwordInput.value?.length ?: 0) >= 8) {
            return true
        } else {
            alertMessage.value = Inputs.PASSWORD
            return false
        }

    }

    private fun validateRePassword(): Boolean {
        if (rePasswordInput.value.equals(passwordInput.value)) {
            return true
        } else {
            alertMessage.value = Inputs.REPASSWORD
            return false
        }

    }

    private fun validateUserImageUpload(): Boolean {
        if (userImageUpload.value != null) {
            return true
        } else {
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