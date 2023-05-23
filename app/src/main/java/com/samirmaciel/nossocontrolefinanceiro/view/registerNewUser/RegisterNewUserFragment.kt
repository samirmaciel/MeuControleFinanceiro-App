package com.samirmaciel.nossocontrolefinanceiro.view.registerNewUser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentRegisterNewUserBinding
import com.samirmaciel.nossocontrolefinanceiro.view.registerNewUser.viewModel.RegisterNewUserViewModel
import java.lang.Exception


class RegisterNewUserFragment : Fragment(R.layout.fragment_register_new_user) {

   private var binding : FragmentRegisterNewUserBinding? = null
    private var mViewModel: RegisterNewUserViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setListeners()
        setViewModel()
        setObservers()
    }

    private fun setObservers() {
        mViewModel?.emailInput?.observe(viewLifecycleOwner){
            setConfirmButtonState(mViewModel?.validateInputs())
        }

        mViewModel?.userNameInput?.observe(viewLifecycleOwner){
            setConfirmButtonState(mViewModel?.validateInputs())
        }

        mViewModel?.passwordInput?.observe(viewLifecycleOwner){
            setConfirmButtonState(mViewModel?.validateInputs())
        }

        mViewModel?.rePasswordInput?.observe(viewLifecycleOwner){
            setConfirmButtonState(mViewModel?.validateInputs())
        }

        mViewModel?.alertMessage?.observe(viewLifecycleOwner){

         if(it != null){
             binding?.registerNewUserAlertMessage?.visibility = View.VISIBLE
             binding?.registerNewUserAlertMessage?.text = getAlertMessage(it)
         }else{
             binding?.registerNewUserAlertMessage?.visibility = View.GONE
         }

        }
    }

    private fun getAlertMessage(input: RegisterNewUserViewModel.Inputs): String {
        return getString(input.id)
    }

    private fun setViewModel() {
        mViewModel = ViewModelProvider(this)[RegisterNewUserViewModel::class.java]
    }

    private fun setListeners() {
        binding?.registerNewUserConfirmButton?.setOnClickListener {
            findNavController().navigate(R.id.action_registerNewUserFragment_to_lobbyFragment)
        }

        binding?.registerNewUserEmailInput?.doOnTextChanged { text, start, before, count ->
            mViewModel?.emailInput?.value = text.toString()
        }

        binding?.registerNewUserNameInput?.doOnTextChanged { text, start, before, count ->
            mViewModel?.userNameInput?.value = text.toString()
        }

        binding?.registerNewUserPasswordInput?.doOnTextChanged { text, start, before, count ->
            mViewModel?.passwordInput?.value = text.toString()
        }

        binding?.registerNewUserRepasswordInput?.doOnTextChanged { text, start, before, count ->
            mViewModel?.rePasswordInput?.value = text.toString()
        }

        binding?.registerNewUserImageUploadButton?.setOnClickListener {
            captureUserImage()
        }
    }

    private fun captureUserImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1){
            try{
               val capturedImage = data?.data
               binding?.registerNewUserImageUploadButton?.setImageURI(capturedImage)
                mViewModel?.userImageUpload?.value = capturedImage
            }catch (e: Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setConfirmButtonState(isEnable: Boolean?){
        binding?.registerNewUserConfirmButton?.isEnabled = isEnable ?: false
    }

    private fun bindView(view: View) {
        binding = FragmentRegisterNewUserBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}