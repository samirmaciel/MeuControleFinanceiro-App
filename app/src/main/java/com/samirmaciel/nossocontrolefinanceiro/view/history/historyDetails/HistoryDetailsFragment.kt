package com.samirmaciel.nossocontrolefinanceiro.view.history.historyDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentHistoryDetailsBinding


class HistoryDetailsFragment : Fragment(R.layout.fragment_history_details) {

    private var binding : FragmentHistoryDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
    }

    private fun bindView(view: View) {
        binding = FragmentHistoryDetailsBinding.bind(view)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}