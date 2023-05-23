package com.samirmaciel.nossocontrolefinanceiro.view.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentTransactionsBinding


class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private var binding : FragmentTransactionsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = FragmentTransactionsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}