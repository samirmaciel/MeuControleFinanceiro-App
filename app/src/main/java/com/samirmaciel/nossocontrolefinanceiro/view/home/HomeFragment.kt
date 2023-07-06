package com.samirmaciel.nossocontrolefinanceiro.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentHomeBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.User
import com.samirmaciel.nossocontrolefinanceiro.view.home.viewModel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var binding: FragmentHomeBinding? = null
    private var mViewModel: HomeViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setViewModel()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        mViewModel?.currentUser?.observe(viewLifecycleOwner) {
            setupUI(it)
        }

    }

    private fun setupUI(user: User?) {
        Glide.with(requireContext()).load(user?.image).into(binding?.homeProfileImageButton!!)

        val currentDate = Date()
        val currentMonthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(currentDate)
        binding?.homeCurrentMonth?.text = currentMonthName.capitalize()
    }

    private fun setViewModel() {
        mViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setSelectedPage(mViewModel?.currentPageSelected!!)
    }

    private fun setListeners() {
        binding?.homeProfileButton?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding?.homeProfileImageButton?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding?.homeTransactionsButton?.setOnClickListener {
            binding?.homeContainer?.findNavController()?.navigate(R.id.transactionsFragment)
            setSelectedPage(TRANSACTIONSPAGE)
        }

        binding?.homeCreditCardsButton?.setOnClickListener {
            binding?.homeContainer?.findNavController()?.navigate(R.id.creditCardsFragment)
            setSelectedPage(CREDITCARDSPAGE)
        }

        binding?.homeHistoryButton?.setOnClickListener {
            binding?.homeContainer?.findNavController()?.navigate(R.id.historyFragment)
            setSelectedPage(HISTORYPAGE)
        }

    }


    private fun setSelectedPage(page: String) {
        mViewModel?.currentPageSelected = page
        when (page) {
            TRANSACTIONSPAGE -> {
                setToSelectedTitle(binding?.homeTransationsTitle)
            }

            CREDITCARDSPAGE -> {
                setToSelectedTitle(binding?.homeCreditCardsTitle)
            }

            HISTORYPAGE -> {
                setToSelectedTitle(binding?.homeHistoryTitle)
            }
        }

    }

    private fun setToSelectedTitle(selectedPage: TextView?) {
        if (selectedPage == null) return

        binding?.homeTransationsTitle?.setTextColor(resources.getColor(R.color.gray))
        binding?.homeCreditCardsTitle?.setTextColor(resources.getColor(R.color.gray))
        binding?.homeHistoryTitle?.setTextColor(resources.getColor(R.color.gray))

        selectedPage.setTextColor(resources.getColor(R.color.white))
    }


    private fun bindView(view: View) {
        binding = FragmentHomeBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val TRANSACTIONSPAGE = "TransactionsPage"
        const val CREDITCARDSPAGE = "CreditCardsPage"
        const val HISTORYPAGE = "HistoryPage"
    }
}