package com.samirmaciel.meucontrolefinanceiro.view.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.samirmaciel.meucontrolefinanceiro.R
import com.samirmaciel.meucontrolefinanceiro.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private var binding : FragmentCategoriesBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setListeners()
    }

    private fun setListeners() {
        binding?.categoriesBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindView(view: View) {
        binding = FragmentCategoriesBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}