package com.samirmaciel.nossocontrolefinanceiro.view.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Filter

class FilterHistoryAdapter(val filterSelected: (Filter) -> Unit): RecyclerView.Adapter<FilterHistoryAdapter.MyViewHolder>() {

    private var filterList = listOf<Filter?>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val name = itemView.findViewById<TextView>(R.id.item_filter_title)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.item_filter_cardview)

        fun bindItem(position: Int){
            if (filterList[position]?.isActive == true) {
                name.setTextColor(itemView.resources.getColor(R.color.white))
                linearLayout.background = itemView.resources.getDrawable(R.drawable.filter_selected)
                filterSelected(filterList[position]!!)
            } else {
                name.setTextColor(itemView.resources.getColor(R.color.darkBlue))
                linearLayout.background =
                    itemView.resources.getDrawable(R.drawable.filter_unselected)
            }

            name.text = filterList[position]?.name

            linearLayout.setOnClickListener {
                activeOrInactiveFilter(position)
            }
        }
    }

    private fun activeOrInactiveFilter(position: Int) {

        if(filterList.size > 1){
            filterList[position]?.isActive = !filterList[position]?.isActive!!

            filterList.forEach {
                if(it?.name?.equals(filterList[position]?.name) == false){
                    it.isActive = false
                }
            }

            notifyDataSetChanged()
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

    fun setList(list: List<Filter?>){
        filterList = list
        notifyDataSetChanged()
    }
}