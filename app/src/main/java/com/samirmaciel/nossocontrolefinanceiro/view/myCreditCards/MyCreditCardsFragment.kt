package com.samirmaciel.nossocontrolefinanceiro.view.myCreditCards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentMyCreditCardsBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.util.LoadState
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.AddCreditCard.AddCreditCardDialog
import com.samirmaciel.nossocontrolefinanceiro.view.myCreditCards.adapter.MyCreditCardsAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.myCreditCards.viewModel.MyCreditCardsViewModel

class MyCreditCardsFragment : Fragment(R.layout.fragment_my_credit_cards) {

    private var binding : FragmentMyCreditCardsBinding? = null
    private var viewModel: MyCreditCardsViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel?.creditCardsList?.observe(viewLifecycleOwner){list ->
            if(list.isNullOrEmpty()){
                loadState(LoadState.ON_FINISH)
                setEmptyListMessageVisibility(true)
            }else{
                setEmptyListMessageVisibility(false)
                setupAdapter(list)
            }
        }
    }

    private fun setupAdapter(list: MutableList<CreditCard>) {
        val creditCardsAdapter  = MyCreditCardsAdapter()

        binding?.myCreditCardsRecyclerView?.apply {
            adapter  = creditCardsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        creditCardsAdapter.setCreditCardsList(list)
    }

    private fun setEmptyListMessageVisibility(isVisible: Boolean) {
        if(isVisible){
            binding?.myCreditCardsEmptyListText?.visibility = View.VISIBLE
            binding?.myCreditCardsRecyclerView?.visibility = View.GONE
            binding?.myCreditCardsProgressbar?.visibility = View.GONE
        }else{
            binding?.myCreditCardsEmptyListText?.visibility = View.GONE
            binding?.myCreditCardsProgressbar?.visibility = View.GONE
            binding?.myCreditCardsRecyclerView?.visibility = View.VISIBLE
        }
    }

    private fun loadState(loadState: LoadState) {
        when (loadState) {
            LoadState.ON_LOAD -> {
                binding?.myCreditCardsRecyclerView?.visibility = View.GONE
                binding?.myCreditCardsProgressbar?.visibility = View.VISIBLE
                binding?.myCreditCardsEmptyListText?.visibility = View.GONE
            }

            LoadState.ON_FINISH -> {
                binding?.myCreditCardsRecyclerView?.visibility = View.VISIBLE
                binding?.myCreditCardsProgressbar?.visibility = View.GONE
                binding?.myCreditCardsEmptyListText?.visibility = View.GONE
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MyCreditCardsViewModel::class.java]
    }

    private fun setListeners() {
        binding?.myCreditCardsBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }

        binding?.myCreditCardsAddButton?.setOnClickListener {
            AddCreditCardDialog(viewModel?.currentUser?.value) {
                loadState(LoadState.ON_LOAD)
                viewModel?.addCreditCard(it)
            }.show(childFragmentManager, "Add Credit Card Dialog Fragment")
        }
    }

    private fun bindView(view: View) {
        binding = FragmentMyCreditCardsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}