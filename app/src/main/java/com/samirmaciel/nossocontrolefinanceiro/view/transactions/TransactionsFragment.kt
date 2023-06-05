package com.samirmaciel.nossocontrolefinanceiro.view.transactions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentTransactionsBinding
import com.samirmaciel.nossocontrolefinanceiro.model.Filter
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter.FilterAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.viewModel.TransactionsViewModel


class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private var binding : FragmentTransactionsBinding? = null
    private var mViewModel: TransactionsViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setViewModel()
        setObserver()
    }

    private fun setObserver() {
        mViewModel?.currentFilters?.observe(viewLifecycleOwner){

            it?.let { list ->
                setupFilterAdapter(list)
            }

        }
    }

    private fun setupFilterAdapter(list: MutableList<Filter>) {
        val filterAdapter = FilterAdapter{
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_LONG).show()
        }

        binding?.transactionsFilterRecyclerview?.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        filterAdapter.setFilterList(list)
        filterAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        mViewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]
    }

    private fun bindView(view: View) {
        binding = FragmentTransactionsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}