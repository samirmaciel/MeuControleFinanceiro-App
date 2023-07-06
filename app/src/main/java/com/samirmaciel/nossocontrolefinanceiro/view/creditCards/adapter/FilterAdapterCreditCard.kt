package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.FilterCreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.FilterTransaction
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter
import com.samirmaciel.nossocontrolefinanceiro.util.FilterTransactionType

class FilterAdapterCreditCard(val activatedFiltersResult: (FilterCreditCard) -> Unit) :
    RecyclerView.Adapter<FilterAdapterCreditCard.MyViewHolder>() {

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
        val filterCreditCard = FilterCreditCard()

        val filterUserName = mutableListOf<String>()

        filterList.forEach { sourceFilter ->

            when (sourceFilter.filterType) {
                FilterTransactionType.USERNAME -> {
                    if (sourceFilter.isActive)
                        filterUserName.add(sourceFilter.name)
                }

                else -> {}
            }

        }

        filterCreditCard.userNameListSelected = filterUserName
        activatedFiltersResult(filterCreditCard)

        notifyDataSetChanged()
    }

    fun setFilterList(filterList: MutableList<Filter>) {
        this.filterList = filterList
    }

}