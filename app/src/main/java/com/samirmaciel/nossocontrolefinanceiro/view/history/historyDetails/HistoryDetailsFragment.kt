package com.samirmaciel.nossocontrolefinanceiro.view.history.historyDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentHistoryDetailsBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import com.samirmaciel.nossocontrolefinanceiro.util.LoadState
import com.samirmaciel.nossocontrolefinanceiro.util.LocaleUtils
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.creditCardsDetails.CreditCardDetailFragmentArgs
import com.samirmaciel.nossocontrolefinanceiro.view.history.viewModel.HistoryDetailViewModel
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter.FilterTransactionAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter.TransactionAdapter


class HistoryDetailsFragment : Fragment(R.layout.fragment_history_details) {

    private var binding : FragmentHistoryDetailsBinding? = null
    private val args: HistoryDetailsFragmentArgs by navArgs()
    private var viewModel: HistoryDetailViewModel? = null
    private var transactionsAdapter: TransactionAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setObservers()
        setListeners()
    }

    private fun setListeners() {
        binding?.historyDetailsBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setObservers() {
        viewModel?.currentControl?.observe(viewLifecycleOwner){
            val month = args.monthYear?.split("|")?.get(0)
            val year = args.monthYear?.split("|")?.get(1)

            it.id?.let { controlID ->

                if(month != null && year != null){
                    setupUI(month, year)
                    viewModel?.loadTransactions(controlID, month, year)
                }
            }
        }

        viewModel?.transactionsList?.observe(viewLifecycleOwner){
            setupTransactionsAdapter(it)
        }

        viewModel?.filters?.observe(viewLifecycleOwner){
            setupFiltersAdapter(it)
        }
    }

    private fun loadStateFilters(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.historyDetailsFilterRecyclerview?.visibility = View.GONE
                binding?.historyDetailsProgressbarFilters?.visibility = View.VISIBLE
            }

            LoadState.ON_FINISH -> {
                binding?.historyDetailsFilterRecyclerview?.visibility = View.VISIBLE
                binding?.historyDetailsProgressbarFilters?.visibility = View.GONE
            }
        }
    }

    private fun loadStateTransactions(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.historyDetailsTransactionsRecyclerview?.visibility = View.GONE
                binding?.historyDetailsProgressbarTransactions?.visibility = View.VISIBLE
            }

            LoadState.ON_FINISH -> {
                binding?.historyDetailsTransactionsRecyclerview?.visibility = View.VISIBLE
                binding?.historyDetailsProgressbarTransactions?.visibility = View.GONE
            }
        }
    }

    private fun loadStateTitle(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.historyDetailsTitle?.visibility = View.GONE
                binding?.historyDetailsProgressbarTitle?.visibility = View.VISIBLE
            }

            LoadState.ON_FINISH -> {
                binding?.historyDetailsTitle?.visibility = View.VISIBLE
                binding?.historyDetailsProgressbarTitle?.visibility = View.GONE
            }
        }
    }
    private fun setupUI(month: String, year: String) {
        binding?.historyDetailsTitle?.text = "${LocaleUtils.getMonthNameByNumber(month.toInt(), resources)} $year"
        loadStateTitle(LoadState.ON_FINISH)
    }

    private fun setupFiltersAdapter(list: MutableList<Filter>) {
        val filterAdapter = FilterTransactionAdapter{
            transactionsAdapter?.setFilter(it)
        }

        binding?.historyDetailsFilterRecyclerview?.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        }

        filterAdapter.setFilterList(list)
        loadStateFilters(LoadState.ON_FINISH)
    }

    private fun setupTransactionsAdapter(list: MutableList<Transaction?>) {
        transactionsAdapter = TransactionAdapter()

        binding?.historyDetailsTransactionsRecyclerview?.apply {
            adapter = transactionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        transactionsAdapter?.setTransactionList(list)
        loadStateTransactions(LoadState.ON_FINISH)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[HistoryDetailViewModel::class.java]
    }

    private fun bindView(view: View) {
        binding = FragmentHistoryDetailsBinding.bind(view)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}