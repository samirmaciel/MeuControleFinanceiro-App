package com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.Filter

class FilterAdapter(val onSelect: (Filter) -> Unit) :
    RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

    private var filterList = mutableListOf<Filter>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.item_filter_title)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.item_filter_cardview)

        fun bindItem(filter: Filter, onSelect: (Filter) -> Unit) {

            if(filter.isSelected){
                name.setTextColor(itemView.resources.getColor(R.color.white))
                linearLayout.background = itemView.resources.getDrawable(R.drawable.filter_selected)
            }else{
                name.setTextColor(itemView.resources.getColor(R.color.darkBlue))
                linearLayout.background = itemView.resources.getDrawable(R.drawable.filter_unselected)
            }

            name.text = filter.name
            linearLayout.setOnClickListener {
                onSelect(filter)
                setSelectedFilter(filter)
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
        holder.bindItem(filterList[position], onSelect)
    }

    private fun setSelectedFilter(filter: Filter){
        filterList.forEach {
            it.isSelected = filter.name == it.name
        }

        notifyDataSetChanged()
    }

    fun setFilterList(filterList: MutableList<Filter>) {
        this.filterList = filterList
    }
}