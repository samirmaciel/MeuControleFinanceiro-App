package com.samirmaciel.nossocontrolefinanceiro.view.lobby

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentLobbyBinding
import com.samirmaciel.nossocontrolefinanceiro.view.lobby.viewModel.LobbyViewModel


class LobbyFragment : Fragment(R.layout.fragment_lobby) {

    private var binding : FragmentLobbyBinding? = null
    private var mViewModel: LobbyViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setViewModel()
        setListeners()
        setObservers()
        isLoading(true)
    }

    private fun isLoading(isloading: Boolean) {

        if(isloading){
            binding?.lobbyProgressBar?.visibility = View.VISIBLE
            binding?.lobbyIdInput?.visibility = View.GONE
            binding?.lobbyEnterButton?.visibility = View.GONE
            binding?.lobbyCreateNewControlButton?.visibility = View.GONE
        }else{
            binding?.lobbyProgressBar?.visibility = View.GONE
            binding?.lobbyIdInput?.visibility = View.VISIBLE
            binding?.lobbyEnterButton?.visibility = View.VISIBLE
            binding?.lobbyCreateNewControlButton?.visibility = View.VISIBLE
        }
    }

    private fun setObservers() {

        mViewModel?.currentUser?.observe(viewLifecycleOwner){
            if(it != null){
                isLoading(false)
            }
        }
    }
    private fun setViewModel() {
        mViewModel = ViewModelProvider(this)[LobbyViewModel::class.java]
    }

    private fun setListeners() {

        binding?.lobbyEnterButton?.setOnClickListener {
            if(validateFields()){
                mViewModel?.enterInControl(binding?.lobbyIdInput?.text.toString()){isSuccess, message ->
                    if(isSuccess){
                        findNavController().navigate(R.id.action_lobbyFragment_to_homeFragment)
                    }else{
                        Snackbar.make(requireView(), "Não foi possivel entrar neste controle: $message", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }else{
                Snackbar.make(requireView(), "Preencha o campo de ID com um ID valido!", Snackbar.LENGTH_SHORT).show()
            }

        }

        binding?.lobbyCreateNewControlButton?.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Deseja criar um novo controle")
                setPositiveButton("Sim"){_,_ ->
                    mViewModel?.createNewControl {
                        if(it.first){
                            Toast.makeText(requireContext(), "Controle criado com sucesso! ID:${it.second}", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_lobbyFragment_to_homeFragment)
                        }else{
                            Toast.makeText(requireContext(), it.second, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                setNegativeButton("Não", null)
            }.show()
        }

        binding?.lobbyBackButton?.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                 setTitle("Deseja realmente sair?")
                setPositiveButton("Sim"){_, _ ->
                    mViewModel?.signOut()
                    findNavController().navigate(R.id.action_lobbyFragment_to_loginFragment)
                }
                setNegativeButton("Não", null)
            }.show()
        }
    }

    private fun validateFields(): Boolean {
        return !binding?.lobbyIdInput?.text.isNullOrEmpty()
    }

    private fun bindView(view: View) {
        binding = FragmentLobbyBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}