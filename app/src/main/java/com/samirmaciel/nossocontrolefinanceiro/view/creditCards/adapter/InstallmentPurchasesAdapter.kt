package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.InstallmentPurchase

class InstallmentPurchasesAdapter : RecyclerView.Adapter<InstallmentPurchasesAdapter.MyViewHolder>() {

    private var installmentPurchasesList: MutableList<InstallmentPurchase> = mutableListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description = itemView.findViewById<TextView>(R.id.item_installment_purchase_description)
        val installments = itemView.findViewById<TextView>(R.id.item_installment_purchases_quantity_payment)
        val value = itemView.findViewById<TextView>(R.id.item_installment_purchase_invoice_value)

        fun bindItem(installmentPurchase: InstallmentPurchase){
            description.text = installmentPurchase.description
            installments.text = "${installmentPurchase.installmentsPaid}/${installmentPurchase.totalInstallment}"
            value.text = installmentPurchase.value.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cash_purchases, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return installmentPurchasesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(installmentPurchasesList[position])
    }

    fun setInstallmentPurchasesList(list: MutableList<InstallmentPurchase>){
        installmentPurchasesList = list
        notifyDataSetChanged()
    }
}