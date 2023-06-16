package com.samirmaciel.nossocontrolefinanceiro.view.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentProfileBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.view.profile.dialog.EditUserNameDialog
import com.samirmaciel.nossocontrolefinanceiro.view.profile.viewModel.ProfileViewModel
import java.lang.Exception


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var binding : FragmentProfileBinding? = null
    private var viewModel: ProfileViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel?.currentUser?.observe(viewLifecycleOwner){user ->
            setupUI(user)
        }
    }

    private fun setupUI(user: User?) {
        Glide.with(this).load(user?.image).into(binding?.profileUserImage!!)
        binding?.profileUserName?.text = user?.name.toString()
        binding?.profileControlId?.text = user?.currentControlId.toString()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private fun setListeners() {
        binding?.profileCategoriesButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_categoriesFragment)
        }

        binding?.profileEditUserNameButton?.setOnClickListener {
            EditUserNameDialog(binding?.profileUserName?.text.toString()){ newUserName ->
                if(!newUserName.isNullOrEmpty()){
                    viewModel?.updateCurrentUserName(newUserName)
                    binding?.profileUserName?.text = newUserName
                }

            }.show(childFragmentManager, "Dialog Edit User Name")
        }

        binding?.profileEditUserImage?.setOnClickListener {
            captureUserImage()
        }

        binding?.profileMyTransactionsButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myTransactionsFragment)
        }

        binding?.profileMyCreditCardsButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myCreditCardsFragment)
        }

        binding?.profileSettingsButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        binding?.profileBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }

        binding?.profileLogoutButton?.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Deseja realmente sair?")
                setPositiveButton("Sim"){_,_ ->
                    viewModel?.logout(){
                        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                    }
                }
                setNegativeButton("Não", null)
            }.show()

        }
    }

    private fun bindView(view: View) {
        binding = FragmentProfileBinding.bind(view)
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
                binding?.profileUserImage?.setImageURI(capturedImage)
                viewModel?.uploadNewCurrentUserImage(capturedImage)
            }catch (e: Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}