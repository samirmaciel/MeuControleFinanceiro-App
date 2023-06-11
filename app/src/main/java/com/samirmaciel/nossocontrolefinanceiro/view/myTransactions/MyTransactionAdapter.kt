package com.samirmaciel.nossocontrolefinanceiro.view.myTransactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Locale

class MyTransactionAdapter : RecyclerView.Adapter<MyTransactionAdapter.MyViewHolder>() {

    private var transactionsList: MutableList<Transaction> = mutableListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImageView = itemView.findViewById<CircleImageView>(R.id.item_transaction_user_image)
        private val description = itemView.findViewById<TextView>(R.id.item_transaction_description)
        private val category = itemView.findViewById<TextView>(R.id.item_transaction_category)
        private val value = itemView.findViewById<TextView>(R.id.item_transaction_value)
        private val date = itemView.findViewById<TextView>(R.id.item_transaction_date)

        fun bindItem(transaction : Transaction){
            Glide.with(itemView.context).load(transaction.user?.image).into(userImageView)
            description.text = transaction.description
            category.text = transaction.category
            value.text  = transaction.value.toString()
            date.text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(transaction.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(transactionsList[position])
    }

    fun setTransactionList(list: MutableList<Transaction>){
        transactionsList = list
        notifyDataSetChanged()
    }
}