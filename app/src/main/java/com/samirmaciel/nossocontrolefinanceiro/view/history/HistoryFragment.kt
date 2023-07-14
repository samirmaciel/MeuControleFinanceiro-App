package com.samirmaciel.nossocontrolefinanceiro.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentHistoryBinding
import com.samirmaciel.nossocontrolefinanceiro.model.MonthTransactions
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.util.LoadState
import com.samirmaciel.nossocontrolefinanceiro.view.history.adapter.FilterHistoryAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.history.viewModel.HistoryViewModel

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var binding : FragmentHistoryBinding? = null
    private var viewModel: HistoryViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setObservers()
    }

    private fun setObservers() {
        viewModel?.monthsTransactionsList?.observe(viewLifecycleOwner){list ->
            list?.let {
                setupHistoryAdapter(it)
            }
        }

        viewModel?.yearsFilterList?.observe(viewLifecycleOwner){ list ->
            list?.let {
                setupFilterAdapter(it)
            }

        }
    }

    private fun setupFilterAdapter(list: List<Filter?>) {
        val filterAdapter = FilterHistoryAdapter{

        }

        binding?.historyFilterRecyclerview?.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        filterAdapter.setList(list)
        loadStateFilters(LoadState.ON_FINISH)
    }

    private fun loadStateFilters(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.historyFilterRecyclerview?.visibility = View.GONE
                binding?.historyProgressBarFilters?.visibility = View.VISIBLE
            }

            LoadState.ON_FINISH -> {
                binding?.historyFilterRecyclerview?.visibility = View.VISIBLE
                binding?.historyProgressBarFilters?.visibility = View.GONE
            }
        }
    }

    private fun loadStateMonths(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.historyMonthsRecyclerview?.visibility = View.GONE
                binding?.historyProgressBarMonths?.visibility = View.VISIBLE
            }

            LoadState.ON_FINISH -> {
                binding?.historyMonthsRecyclerview?.visibility = View.VISIBLE
                binding?.historyProgressBarMonths?.visibility = View.GONE
            }
        }
    }

    private fun setupHistoryAdapter(list: List<MonthTransactions>) {
        val historyAdapter = HistoryAdapter{
            val action  = HistoryFragmentDirections.actionHistoryFragmentToHistoryDetailsFragment("${it.month}|${it.year}")
            findNavController().navigate(action)
        }

        binding?.historyMonthsRecyclerview?.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }

        historyAdapter.setItemList(list)
        loadStateMonths(LoadState.ON_FINISH)

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    private fun bindView(view: View) {
        binding = FragmentHistoryBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

}