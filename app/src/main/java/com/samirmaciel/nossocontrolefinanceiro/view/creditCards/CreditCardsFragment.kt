package com.samirmaciel.nossocontrolefinanceiro.view.creditCards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentCreditCardsBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.AddCreditCard.AddCreditCardDialog
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel.CreditCardViewModel


class CreditCardsFragment : Fragment(R.layout.fragment_credit_cards) {

    private var binding : FragmentCreditCardsBinding? = null
    private var viewModel: CreditCardViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel?.creditCardList?.observe(viewLifecycleOwner){

        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[CreditCardViewModel::class.java]
    }

    private fun setListeners() {
        binding?.creditCardsAddButton?.setOnClickListener {
            AddCreditCardDialog(this, User()){

            }.show(childFragmentManager, "Add Credit Card Dialog")
        }
    }

    private fun bindView(view: View) {
        binding = FragmentCreditCardsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}