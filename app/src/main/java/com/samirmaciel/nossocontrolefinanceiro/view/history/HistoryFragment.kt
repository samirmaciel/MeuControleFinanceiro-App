package com.samirmaciel.nossocontrolefinanceiro.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var binding : FragmentHistoryBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = FragmentHistoryBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

}