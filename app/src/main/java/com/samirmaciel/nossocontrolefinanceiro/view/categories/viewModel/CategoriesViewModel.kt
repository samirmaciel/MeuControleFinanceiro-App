package com.samirmaciel.nossocontrolefinanceiro.view.categories.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.nossocontrolefinanceiro.firebase.Constants
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Control
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User

class CategoriesViewModel: ViewModel() {

    private val currentUser: MutableLiveData<User> = MutableLiveData()
    private val currentControl: MediatorLiveData<Control> = MediatorLiveData()
    private val fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val categoryList: MutableLiveData<MutableList<String>?> = MutableLiveData()
    val messageListener: MutableLiveData<String> = MutableLiveData()

    init {
        loadCurrentUser()
    }

    fun deleteCategory(category: String?){

        val newControl =  currentControl.value

        newControl?.categories?.removeIf { it == category }

        updateControl(newControl){ isSucess, exceptionMessage ->
            if(isSucess){
                messageListener.value = "Categoria removida com sucesso!"
            }else{
                messageListener.value = "Não foi possivel remover a categoria: $exceptionMessage"
            }
        }
    }

    private fun updateControl(newControl: Control?, onFinish: (Boolean, String?) -> Unit) {
        newControl?.id?.let { controlID ->
            fireStore.collection(Constants.CONTROLS).document(controlID).set(newControl).addOnCompleteListener {

                it.addOnSuccessListener {
                    categoryList.value = newControl.categories
                    onFinish(true, null)
                }

                it.addOnFailureListener {
                    onFinish(false, it.localizedMessage)
                }
            }
        }
    }

    fun addNewCategory(newCategory: String){
        var newControl = currentControl.value

        if (newControl?.categories == null){
            val newList : MutableList<String> = mutableListOf()
            newList.add(newCategory)
            newControl?.categories = newList
        }else{
            newControl?.categories?.add(newCategory)
        }

        updateControl(newControl){ isSucess, exceptionMessage ->
            if(isSucess){
                messageListener.value = "Categoria adicionada com sucesso!"
            }else{
                messageListener.value = "Categoria não foi adicionada $exceptionMessage"
            }
        }
    }

    private fun loadCurrentControl(user: User?) {
        user?.currentControlId?.let { controlID ->
            fireStore.collection(Constants.CONTROLS).document(controlID).get().addOnCompleteListener {

                it.addOnSuccessListener { documentSnapshot ->
                    val control = documentSnapshot.toObject(Control::class.java)

                    control?.let {control ->
                        currentControl.value = control
                        categoryList.value = control.categories
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
                        loadCurrentControl(user)
                    }
                }

                it.addOnFailureListener {

                }
            }
        }
    }
}