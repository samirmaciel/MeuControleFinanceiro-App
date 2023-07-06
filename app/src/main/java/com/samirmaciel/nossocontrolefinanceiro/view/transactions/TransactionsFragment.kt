package com.samirmaciel.nossocontrolefinanceiro.view.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentTransactionsBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.util.LoadState
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter.FilterTransactionAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter.TransactionAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.addTransactionDialog.AddTransactionDialog
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.viewModel.TransactionsViewModel


class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private var binding : FragmentTransactionsBinding? = null
    private var viewModel: TransactionsViewModel? = null
    private var transactionsAdapter: TransactionAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setViewModel()
        setListeners()
        setObserver()
    }

    private fun setListeners() {
        binding?.transactionAddButton?.setOnClickListener {
            AddTransactionDialog(viewModel?.currentControl?.value){transaction, installmentPurchase ->
                viewModel?.saveTransaction(transaction)
            }.show(childFragmentManager, "Add Transaction Dialog Fragment")
        }
    }

    private fun loadStateTransactions(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.transactionsLastTransactionsRecyclerView?.visibility = View.GONE
                binding?.transactionsTransactionsProgressbar?.visibility = View.VISIBLE
                //binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }

            LoadState.ON_FINISH -> {
                binding?.transactionsLastTransactionsRecyclerView?.visibility = View.VISIBLE
                binding?.transactionsTransactionsProgressbar?.visibility = View.GONE
                //binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }
        }
    }

    private fun loadStateFilters(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.transactionsFilterRecyclerview?.visibility = View.GONE
                binding?.transactionsFilterProgressbar?.visibility = View.VISIBLE
                //binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }

            LoadState.ON_FINISH -> {
                binding?.transactionsFilterRecyclerview?.visibility = View.VISIBLE
                binding?.transactionsFilterProgressbar?.visibility = View.GONE
                //binding?.myTransactionsTextToListEmpty?.visibility = View.GONE
            }
        }
    }

    private fun setObserver() {
        viewModel?.filters?.observe(viewLifecycleOwner){
            it?.let { list ->
                loadStateFilters(LoadState.ON_FINISH)
                setupFilterAdapter(list)
            }
        }

        viewModel?.transactions?.observe(viewLifecycleOwner){
            it?.let {list ->
                loadStateTransactions(LoadState.ON_FINISH)
                setupTransactionAdapter(list)
            }

        }
    }

    private fun setupTransactionAdapter(list: MutableList<Transaction?>) {
        transactionsAdapter = TransactionAdapter()

        binding?.transactionsLastTransactionsRecyclerView?.apply {
            adapter = transactionsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        transactionsAdapter?.setTransactionList(list)
    }

    private fun setupFilterAdapter(list: MutableList<Filter>) {
        val filterTransactionAdapter = FilterTransactionAdapter{
              transactionsAdapter?.setFilter(it)
        }

        binding?.transactionsFilterRecyclerview?.apply {
            adapter = filterTransactionAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        filterTransactionAdapter.setFilterList(list)
        filterTransactionAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]
    }

    private fun bindView(view: View) {
        binding = FragmentTransactionsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}