package com.samirmaciel.nossocontrolefinanceiro.view.myTransactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentMyTransactionsBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.util.LoadState
import com.samirmaciel.nossocontrolefinanceiro.view.myTransactions.viewModel.MyTransactionsViewModel
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter.TransactionAdapter


class MyTransactionsFragment : Fragment(R.layout.fragment_my_transactions) {

    private var binding: FragmentMyTransactionsBinding? = null
    private var viewModel: MyTransactionsViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel?.transactions?.observe(viewLifecycleOwner) { list ->
            if(list.isNullOrEmpty()){
                loadStateTransactions(LoadState.ON_FINISH)
                setEmptyListMessageVisibility(true)
            }else{
                setEmptyListMessageVisibility(false)
                setupAdapter(list)
            }

        }
    }

    private fun setEmptyListMessageVisibility(isVisible: Boolean) {
        if(isVisible){
            binding?.myTransactionsTextToListEmpty?.visibility = View.VISIBLE
            binding?.myTransactionsRecyclerView?.visibility = View.GONE
            binding?.mytransactionsProgressbar?.visibility = View.GONE
        }else{
            binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            binding?.myTransactionsRecyclerView?.visibility = View.VISIBLE
        }
    }

    private fun setupAdapter(list: MutableList<Transaction?>) {
        val transactionsAdapter = TransactionAdapter()

        binding?.myTransactionsRecyclerView?.apply {
            adapter = transactionsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        transactionsAdapter.setTransactionList(list)
        loadStateTransactions(LoadState.ON_FINISH)
    }

    private fun loadStateTransactions(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.myTransactionsRecyclerView?.visibility = View.GONE
                binding?.mytransactionsProgressbar?.visibility = View.VISIBLE
                binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }

            LoadState.ON_FINISH -> {
                binding?.myTransactionsRecyclerView?.visibility = View.VISIBLE
                binding?.mytransactionsProgressbar?.visibility = View.GONE
                binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MyTransactionsViewModel::class.java]
    }

    private fun setListeners() {
        binding?.myTransactionsBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindView(view: View) {
        binding = FragmentMyTransactionsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}