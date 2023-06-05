package com.samirmaciel.nossocontrolefinanceiro.view.lobby.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.CollectionsNames
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import java.util.Date

class LobbyViewModel : ViewModel() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val controlIDInput: MutableLiveData<String> = MutableLiveData()
    val currentUser: MutableLiveData<User?> = MutableLiveData()

    fun signOut() {
        mAuth.signOut()
    }

    init {
        getCurrentUser {
            currentUser.value = it
        }
    }

    fun validateInputs(): Boolean {
        return validateControlID(controlIDInput.value)
    }

    private fun validateControlID(controlID: String?): Boolean {
        return (controlID?.length ?: 0) >= 10
    }

    fun createNewControl(onFinish: (Pair<Boolean, String?>) -> Unit) {

        if (currentUser.value != null) {
            val currentUser = currentUser.value
            val controlID = "${currentUser!!.id?.take(10)}"
            val membersList = mutableListOf<User>()
            membersList.add(currentUser!!)

            val newControl = Control(
                id = controlID,
                currentUser.name,
                members = membersList,
                date = Date(),
                createdBy = currentUser
            )

            mFireStore.collection(CollectionsNames.CONTROLS).document(newControl.id!!).set(newControl)
                .addOnCompleteListener {
                    it.addOnSuccessListener {
                        currentUser.currentControlId = newControl.id
                        updateCurrentUser(currentUser)
                        onFinish(Pair(true, newControl.id))
                    }

                    it.addOnFailureListener {
                        onFinish(Pair(false, it.localizedMessage))
                    }
                }
        }

    }

    private fun updateCurrentUser(user: User){
        mFireStore.collection(CollectionsNames.USERS).document(user.id!!).set(user).addOnCompleteListener {
            it.addOnSuccessListener {

            }

            it.addOnFailureListener {

            }
        }
    }

    private fun getCurrentUser(onFinish: (User?) -> Unit) {
        val fireBaseUser = mAuth.currentUser

        if (fireBaseUser != null) {
            mFireStore.collection(CollectionsNames.USERS).document(fireBaseUser.uid).get().addOnCompleteListener {

                it.addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        onFinish(user)
                    }
                }

                it.addOnFailureListener {
                    onFinish(null)
                }
            }
        }

    }
}