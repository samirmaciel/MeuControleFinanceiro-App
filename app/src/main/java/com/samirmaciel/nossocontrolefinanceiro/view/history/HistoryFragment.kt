package com.samirmaciel.nossocontrolefinanceiro.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentHistoryBinding
import com.samirmaciel.nossocontrolefinanceiro.model.MonthTransactions
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
    }

    private fun setupHistoryAdapter(list: List<MonthTransactions>) {
        val historyAdapter = HistoryAdapter{

        }

        binding?.historyMonthsRecyclerview?.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }

        historyAdapter.setItemList(list)

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