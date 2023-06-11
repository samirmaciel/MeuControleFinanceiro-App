package com.samirmaciel.nossocontrolefinanceiro.view.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.api.Distribution.BucketOptions.Linear
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentCategoriesBinding
import com.samirmaciel.nossocontrolefinanceiro.util.LoadState
import com.samirmaciel.nossocontrolefinanceiro.view.categories.adapter.CategoriesAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.categories.addCategoryDialog.AddCategoryDialog
import com.samirmaciel.nossocontrolefinanceiro.view.categories.viewModel.CategoriesViewModel

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private var binding : FragmentCategoriesBinding? = null
    private var viewModel: CategoriesViewModel? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        loadState(LoadState.ON_LOAD)
        setupViewModel()
        setListeners()
        setObserver()
    }

    private fun setObserver() {
        viewModel?.categoryList?.observe(viewLifecycleOwner){
            it?.let {list ->
                setupAdapter(list)
            }
        }

        viewModel?.messageListener?.observe(viewLifecycleOwner){
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun setupAdapter(list: MutableList<String>) {
        val categoryAdapter = CategoriesAdapter{ categoryToDelete ->
            loadState(LoadState.ON_LOAD)
            viewModel?.deleteCategory(categoryToDelete)
        }

        binding?.categoryRecyclerView?.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        categoryAdapter.setCategorieList(list)
        loadState(LoadState.ON_FINISH)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]
    }

    private fun setListeners() {
        binding?.categoriesBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }

        binding?.categoriesAddButton?.setOnClickListener {
            AddCategoryDialog{ newCategory ->
                loadState(LoadState.ON_LOAD)
                viewModel?.addNewCategory(newCategory)
            }.show(childFragmentManager, "Dialog add Category")
        }
    }

    private fun loadState(loadState: LoadState){
        when(loadState){
            LoadState.ON_LOAD -> {
                binding?.categoryRecyclerView?.visibility = View.GONE
                binding?.categoriesProgressbar?.visibility = View.VISIBLE
            }

            LoadState.ON_FINISH -> {
                binding?.categoryRecyclerView?.visibility = View.VISIBLE
                binding?.categoriesProgressbar?.visibility = View.GONE
            }

            else -> {}
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