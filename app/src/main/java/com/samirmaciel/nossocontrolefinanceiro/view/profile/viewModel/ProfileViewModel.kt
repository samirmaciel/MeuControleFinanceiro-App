package com.samirmaciel.nossocontrolefinanceiro.view.profile.viewModel

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class ProfileViewModel: ViewModel() {

    val currentUser : MutableLiveData<User> = MutableLiveData()
    val currentControl: MediatorLiveData<Control> = MediatorLiveData()

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private  val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()


    init {
        currentControl.addSource(currentUser){user ->
            loadCurrentControl(user)
        }
        loadCurrentUser()
    }

    private fun loadCurrentControl(user: User?) {

        user?.currentControlId?.let { controlID ->
            fireStore.collection(Constants.CONTROLS).document(controlID).get().addOnCompleteListener {

                it.addOnSuccessListener { documentSnapshot ->
                    val control = documentSnapshot.toObject(Control::class.java)

                    control?.let {control ->
                        currentControl.value = control
                    }

                }

                it.addOnFailureListener {

                }
            }
        }

    }

    private fun loadCurrentUser(){
        val firebaseUser = auth.currentUser

        firebaseUser?.let {
            fireStore.collection(Constants.USERS).document(it.uid).get().addOnCompleteListener {

                it.addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)

                    user?.let { user ->
                        currentUser.value = user
                    }
                }

                it.addOnFailureListener {

                }
            }
        }
    }

    fun updateCurrentUserName(newUserName: String){
        val newUser = currentUser.value
        newUser?.name = newUserName

        newUser?.id?.let {
            fireStore.collection(Constants.USERS).document(it).set(newUser).addOnCompleteListener {

                it.addOnSuccessListener {

                }

                it.addOnFailureListener {

                }
            }
        }
    }

    private fun updateCurrentUserImage(newImageUrl: String){
        val newUser = currentUser.value
        newUser?.image = newImageUrl

        newUser?.id?.let {
            fireStore.collection(Constants.USERS).document(it).set(newUser).addOnCompleteListener {

                it.addOnSuccessListener {

                }

                it.addOnFailureListener {

                }
            }
        }
    }

    fun uploadNewCurrentUserImage(imageUri: Uri?){
        val storageRef = storage.reference
        val imageRef = storageRef.child(Constants.USERSIMAGES)
        val fileRef = imageRef.child(currentUser.value?.id!!)


        imageUri?.let { uri ->
            val uploadTask = fileRef.putFile(uri)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {

                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    updateCurrentUserImage(downloadUri.toString())
                } else {

                }
            }
        }

    }

    fun logout(onFinish: () -> Unit) {
        auth.signOut()
        onFinish()
    }

}