package com.samirmaciel.nossocontrolefinanceiro.view.creditCards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentCreditCardsBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.util.LoadState
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.AddCreditCard.AddCreditCardDialog
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter.CreditCardsAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter.FilterAdapterCreditCard
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel.CreditCardViewModel


class CreditCardsFragment : Fragment(R.layout.fragment_credit_cards) {

    private var binding : FragmentCreditCardsBinding? = null
    private var viewModel: CreditCardViewModel? = null
    private var creditCardAdapter: CreditCardsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel?.creditCardList?.observe(viewLifecycleOwner){list ->
            list?.let {
                loadStateCreditCard(LoadState.ON_FINISH)
                setupCreditCard(it)
            }

        }

        viewModel?.filterList?.observe(viewLifecycleOwner){list ->
            list?.let {
                loadStateFilters(LoadState.ON_FINISH)
                setupFilter(it)
            }
        }
    }

    private fun loadStateCreditCard(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.creditCardCreditCardRecycler?.visibility = View.GONE
                binding?.creditCardCredCardProgressbar?.visibility = View.VISIBLE
                //binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }

            LoadState.ON_FINISH -> {
                binding?.creditCardCreditCardRecycler?.visibility = View.VISIBLE
                binding?.creditCardCredCardProgressbar?.visibility = View.GONE
                //binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }
        }
    }

    private fun loadStateFilters(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.creditCardFilterRecyclerview?.visibility = View.GONE
                binding?.creditCardFilterProgressbar?.visibility = View.VISIBLE
                //binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }

            LoadState.ON_FINISH -> {
                binding?.creditCardFilterRecyclerview?.visibility = View.VISIBLE
                binding?.creditCardFilterProgressbar?.visibility = View.GONE
                //binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }
        }
    }

    private fun setupFilter(filters: MutableList<Filter>) {
        val filterAdapter = FilterAdapterCreditCard(){
            creditCardAdapter?.setFilter(it)
        }

        binding?.creditCardFilterRecyclerview?.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        filterAdapter.setFilterList(filters)
    }

    private fun setupCreditCard(it: MutableList<CreditCard?>) {
        creditCardAdapter = CreditCardsAdapter{
            val action  = CreditCardsFragmentDirections.actionCreditCardsFragmentToCreditCardsDetailsFragment(it?.id)
            findNavController().navigate(action)
        }

        binding?.creditCardCreditCardRecycler?.apply {
            adapter = creditCardAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        creditCardAdapter?.setCreditCardsList(it)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[CreditCardViewModel::class.java]
    }

    private fun setListeners() {
        binding?.creditCardsAddButton?.setOnClickListener {
            AddCreditCardDialog(this, User()){
                viewModel?.saveCreditCard(it)
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