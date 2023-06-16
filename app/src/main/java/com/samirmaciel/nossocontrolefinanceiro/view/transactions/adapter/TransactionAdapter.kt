package com.samirmaciel.nossocontrolefinanceiro.view.transactions.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction
import de.hdodenhof.circleimageview.CircleImageView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    private var transactionList = mutableListOf<Transaction?>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionAdapter.MyViewHolder, position: Int) {
        holder.bindItem(transactionList[position])
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView.findViewById<CircleImageView>(R.id.item_transaction_user_image)
        val description = itemView.findViewById<TextView>(R.id.item_transaction_description)
        val category = itemView.findViewById<TextView>(R.id.item_transaction_category)
        val date = itemView.findViewById<TextView>(R.id.item_transaction_date)
        val value = itemView.findViewById<TextView>(R.id.item_transaction_value)

        fun bindItem(transaction: Transaction?) {
            Glide.with(itemView).load(transaction?.user?.image).into(userImage)
            description.text = transaction?.description
            category.text = transaction?.category
            value.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(transaction?.value).toString()
            val dateFormatted = SimpleDateFormat("dd MMMM yy", Locale.getDefault()).format(transaction?.date).capitalize()
            val capitalizeMonth = dateFormatted.replaceFirstChar {
                if(it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            date.text = capitalizeMonth
        }
    }

    fun setTransactionList(list: MutableList<Transaction?>){
        transactionList = list
    }
}