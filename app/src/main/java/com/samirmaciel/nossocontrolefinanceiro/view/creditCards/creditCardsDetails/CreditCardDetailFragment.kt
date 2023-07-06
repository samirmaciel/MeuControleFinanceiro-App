package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.creditCardsDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.databinding.FragmentCreditCardDetailBinding
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.InstallmentPurchase
import com.samirmaciel.nossocontrolefinanceiro.util.LoadState
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter.InstallmentPurchasesAdapter
import com.samirmaciel.nossocontrolefinanceiro.view.creditCards.viewModel.CreditCardDetailViewModel
import java.text.NumberFormat
import java.util.Locale


class CreditCardDetailFragment : Fragment(R.layout.fragment_credit_card_detail) {

    private var binding: FragmentCreditCardDetailBinding? = null
    private var viewModel: CreditCardDetailViewModel? = null
    private val args: CreditCardDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setupViewModel()
        setObservers()
        setListeners()
        loadStateUI(LoadState.ON_LOAD)
        loadStateInstallmentPurchasesRecycler(LoadState.ON_LOAD)

    }

    private fun setListeners() {
        binding?.creditCardDetailBackButtom?.setOnClickListener {
            findNavController().navigate(R.id.action_creditCardDetailFragment_to_creditCardsFragment)
        }
    }

    private fun setObservers() {
        viewModel?.currentCreditCard?.observe(viewLifecycleOwner){creditCard ->
            setupUI(creditCard)


            if(!creditCard?.installmentPurchases.isNullOrEmpty()){
                setupAdapter(creditCard.installmentPurchases!!)
                loadStateInstallmentPurchasesRecycler(LoadState.ON_FINISH)

            }else{
                loadStateInstallmentPurchasesRecycler(LoadState.ON_FINISH)
                binding?.creditCardDetailInstallmentPurchasePlaceholder?.visibility = View.VISIBLE
            }
        }

        viewModel?.currentControl?.observe(viewLifecycleOwner){
            val creditCardID = args.creditCardID

            creditCardID?.let {id ->
                viewModel?.loadCreditCard(id)
            }
        }

    }

    private fun loadStateUI(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.creditCardDetailCardName?.visibility = View.GONE
                binding?.creditCardDetailUserImage?.visibility = View.GONE
                binding?.creditCardDetailLimitAvailableValue?.visibility = View.GONE
                binding?.creditCardDetailProgressBarTitle?.visibility = View.VISIBLE
            }

            LoadState.ON_FINISH -> {
                binding?.creditCardDetailCardName?.visibility = View.VISIBLE
                binding?.creditCardDetailUserImage?.visibility = View.VISIBLE
                binding?.creditCardDetailLimitAvailableValue?.visibility = View.VISIBLE
                binding?.creditCardDetailProgressBarTitle?.visibility = View.GONE
                binding?.creditCardDetailProgressBarLimitAvailable?.visibility = View.GONE
            }
        }
    }

    private fun loadStateInstallmentPurchasesRecycler(state: LoadState) {
        when (state) {
            LoadState.ON_LOAD -> {
                binding?.creditCardDetailCashPurchasesRecyclerview?.visibility = View.GONE
                binding?.creditCardDetailProgressBarRecycler?.visibility = View.VISIBLE
                binding?.creditCardDetailInstallmentPurchasePlaceholder?.visibility = View.GONE
            }

            LoadState.ON_FINISH -> {
                binding?.creditCardDetailCashPurchasesRecyclerview?.visibility = View.VISIBLE
                binding?.creditCardDetailProgressBarRecycler?.visibility = View.GONE
            }
        }
    }


    private fun setupAdapter(list: List<InstallmentPurchase>) {
        val installmentPurchaseAdapter = InstallmentPurchasesAdapter()

        binding?.creditCardDetailCashPurchasesRecyclerview?.apply {
            adapter = installmentPurchaseAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }

        installmentPurchaseAdapter.setInstallmentPurchasesList(list)
        binding?.creditCardDetailInstallmentPurchasePlaceholder?.visibility = View.GONE
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[CreditCardDetailViewModel::class.java]

    }

    private fun setupUI(creditCard: CreditCard) {
        binding?.creditCardDetailCardName?.text = creditCard.description
        binding?.creditCardDetailDueDate?.text = creditCard.dueDate.toString()
        binding?.creditCardDetailLimitAvailableValue?.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(creditCard.availableLimit)
            .toString()
        Glide.with(requireContext()).load(creditCard.user?.image).into(binding?.creditCardDetailUserImage!!)
        loadStateUI(LoadState.ON_FINISH)
    }

    private fun bindView(view: View) {
        binding = FragmentCreditCardDetailBinding.bind(view)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}