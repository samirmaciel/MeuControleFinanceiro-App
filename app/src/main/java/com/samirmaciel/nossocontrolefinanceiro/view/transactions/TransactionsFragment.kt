package com.samirmaciel.nossocontrolefinanceiro.view.transactions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentTransactionsBinding
import com.samirmaciel.nossocontrolefinanceiro.model.FilterTransaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter.FilterAdapter
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

    private fun setObserver() {
        viewModel?.filters?.observe(viewLifecycleOwner){
            it?.let { list ->
                setupFilterAdapter(list)
            }
        }

        viewModel?.transactions?.observe(viewLifecycleOwner){
            it?.let {list ->
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
        val filterAdapter = FilterAdapter{
              transactionsAdapter?.setFilter(it)
        }

        binding?.transactionsFilterRecyclerview?.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        filterAdapter.setFilterList(list)
        filterAdapter.notifyDataSetChanged()
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