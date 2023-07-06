package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.FilterCreditCard
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import de.hdodenhof.circleimageview.CircleImageView
import java.text.NumberFormat
import java.util.Locale

class CreditCardsAdapter(val onClickDetail: (CreditCard?) -> Unit) : RecyclerView.Adapter<CreditCardsAdapter.MyViewHolder>() {

    private var currentCreditCardsList: MutableList<CreditCard?> = mutableListOf()
    private var originalCreditCardsList: MutableList<CreditCard?> = mutableListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        val userImageView = itemView.findViewById<CircleImageView>(R.id.item_credit_card_user_image)
        val creditCardName = itemView.findViewById<TextView>(R.id.item_credit_card_title)
        val creditCardLimitAvailable = itemView.findViewById<TextView>(R.id.item_credit_card_limit_available_value)
        val creditCardLimitTotal = itemView.findViewById<TextView>(R.id.item_credit_cards_limit_total_value)
        val creditCardDetailsButton = itemView.findViewById<ImageButton>(R.id.item_credit_card_details_button)


        fun bindItem(creditCard: CreditCard?){
            Glide.with(itemView.context).load(creditCard?.user?.image).into(userImageView)
            creditCardName.text = creditCard?.description
            creditCardLimitAvailable.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(creditCard?.availableLimit).toString()
            creditCardLimitTotal.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(creditCard?.limitTotal).toString()
            creditCardDetailsButton.setOnClickListener {
                onClickDetail(creditCard)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_credit_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return currentCreditCardsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(currentCreditCardsList[position])
    }

    fun setCreditCardsList(list: MutableList<CreditCard?>){
        currentCreditCardsList = list
        originalCreditCardsList = list
        notifyDataSetChanged()
    }

    fun setFilter(filterCreditCard: FilterCreditCard?) {
        filterCreditCard?.let { sourceFilterCrediCard ->

            currentCreditCardsList = mutableListOf()

            if (sourceFilterCrediCard.userNameListSelected.isNullOrEmpty()) {
                currentCreditCardsList = originalCreditCardsList
                notifyDataSetChanged()
                return
            }

            originalCreditCardsList.forEach { sourceTransaction ->

                var isValid = false

                filterCreditCard.userNameListSelected?.forEach {
                    if(isValid == false){
                        isValid = it == sourceTransaction?.user?.name
                    }
                }


                if(isValid){
                    currentCreditCardsList.add(sourceTransaction)
                }

            }
            notifyDataSetChanged()
        }

    }
}