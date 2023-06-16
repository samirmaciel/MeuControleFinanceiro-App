package com.samirmaciel.nossocontrolefinanceiro.view.creditCards.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samirmaciel.nossocontrolefinanceiro.R
import com.samirmaciel.nossocontrolefinanceiro.model.firebase.CreditCard
import de.hdodenhof.circleimageview.CircleImageView
import java.text.NumberFormat
import java.util.Locale

class CreditCardsAdapter : RecyclerView.Adapter<CreditCardsAdapter.MyViewHolder>() {

    private var creditCardsList: MutableList<CreditCard> = mutableListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        val userImageView = itemView.findViewById<CircleImageView>(R.id.item_credit_card_user_image)
        val creditCardName = itemView.findViewById<TextView>(R.id.item_credit_card_title)
        val creditCardLimitAvailable = itemView.findViewById<TextView>(R.id.item_credit_card_limit_available_value)
        val creditCardLimitTotal = itemView.findViewById<TextView>(R.id.item_credit_cards_limit_total_value)
        val creditCardDetailsButton = itemView.findViewById<ImageButton>(R.id.item_credit_card_details_button)


        fun bindItem(creditCard: CreditCard){
            Glide.with(itemView.context).load(creditCard.user?.image).into(userImageView)
            creditCardName.text = creditCard.description
            creditCardLimitAvailable.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(creditCard.availableLimit).toString()
            creditCardLimitTotal.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(creditCard.limitTotal).toString()
            creditCardDetailsButton.setOnClickListener {
                Toast.makeText(itemView.context, "Click Details", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_credit_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return creditCardsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(creditCardsList[position])
    }

    fun setCreditCardsList(list: MutableList<CreditCard>){
        creditCardsList = list
        notifyDataSetChanged()
    }
}