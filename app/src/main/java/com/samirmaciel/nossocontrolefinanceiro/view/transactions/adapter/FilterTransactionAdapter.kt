package com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.FilterTransaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.util.FilterTransactionType

class FilterTransactionAdapter(val activatedFiltersResult: (FilterTransaction) -> Unit) :
    RecyclerView.Adapter<FilterTransactionAdapter.MyViewHolder>() {

    private var filterList = mutableListOf<Filter>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.item_filter_title)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.item_filter_cardview)

        fun bindItem(position: Int) {

            if (filterList[position].isActive) {
                name.setTextColor(itemView.resources.getColor(R.color.white))
                linearLayout.background = itemView.resources.getDrawable(R.drawable.filter_selected)
            } else {
                name.setTextColor(itemView.resources.getColor(R.color.darkBlue))
                linearLayout.background =
                    itemView.resources.getDrawable(R.drawable.filter_unselected)
            }

            name.text = filterList[position].name
            linearLayout.setOnClickListener {
                filterList[position].isActive = !filterList[position].isActive
                activateOrInactivateFilter(filterList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(position)
    }

    private fun activateOrInactivateFilter(filter: Filter) {
        val filterTransaction = FilterTransaction()

        val filterUserName = mutableListOf<String>()
        val filterPaymentType = mutableListOf<String>()
        val filterCategory = mutableListOf<String>()

        filterList.forEach { sourceFilter ->

            if(filter.filterType == FilterTransactionType.HIGHESTVALUE && filter.isActive){
                filterTransaction.valueFilterType = FilterTransactionType.HIGHESTVALUE
                if(sourceFilter.filterType == FilterTransactionType.SMALLESTVALUE){
                    sourceFilter.isActive = false
                }
            }

            if(filter.filterType == FilterTransactionType.SMALLESTVALUE && filter.isActive){
                filterTransaction.valueFilterType = FilterTransactionType.SMALLESTVALUE
                if(sourceFilter.filterType == FilterTransactionType.HIGHESTVALUE){
                    sourceFilter.isActive = false
                }
            }

            when (sourceFilter.filterType) {
                FilterTransactionType.USERNAME -> {
                    if (sourceFilter.isActive)
                        filterUserName.add(sourceFilter.name)
                }

                FilterTransactionType.PAYMENTTYPE -> {
                    if (sourceFilter.isActive)
                        filterPaymentType.add(sourceFilter.name)
                }

                FilterTransactionType.CATEGORY -> {
                    if (sourceFilter.isActive)
                        filterCategory.add(sourceFilter.name)
                }

                else -> {}
            }

        }

        filterTransaction.categoryListSelected = filterCategory
        filterTransaction.paymentTypeListSelected = filterPaymentType
        filterTransaction.userNameListSelected = filterUserName
        activatedFiltersResult(filterTransaction)

        notifyDataSetChanged()
    }

    fun setFilterList(filterList: MutableList<Filter>) {
        this.filterList = filterList
    }
}