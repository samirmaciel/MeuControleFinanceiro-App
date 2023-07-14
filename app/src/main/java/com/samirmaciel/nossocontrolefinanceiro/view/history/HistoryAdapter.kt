package com.samirmaciel.nossocontrolefinanceiro.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.MonthTransactions
import com.samirmaciel.nossocontrolefinanceiro.util.LocaleUtils
import java.text.NumberFormat
import java.util.Locale

class HistoryAdapter(val onSelect: (MonthTransactions) -> Unit) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    private var itemList = listOf<MonthTransactions>()

    inner class MyViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(position: Int){

            itemView.findViewById<TextView>(R.id.itemMonth_name).text = LocaleUtils.getMonthNameByNumber(itemList[position].month?.toInt(), itemView.resources)
            itemView.findViewById<TextView>(R.id.itemMonth_totalValue).text = NumberFormat.getCurrencyInstance(
                Locale.getDefault()).format(itemList[position].totalValue)
                .toString()
            itemView.findViewById<ImageButton>(R.id.itemMonth_button).setOnClickListener {
                onSelect(itemList[position])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(position)
    }

    fun setItemList(list: List<MonthTransactions>){
        itemList = list
        notifyDataSetChanged()
    }
}