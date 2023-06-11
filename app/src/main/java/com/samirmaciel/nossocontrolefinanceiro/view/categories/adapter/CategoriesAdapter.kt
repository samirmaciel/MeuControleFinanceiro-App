package com.samirmaciel.nossocontrolefinanceiro.view.categories.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.nossocontrolefinanceiro.R

class CategoriesAdapter(val onDelete: (String?) -> Unit): RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {

    private var categoriesList: MutableList<String> = mutableListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName = itemView.findViewById<TextView>(R.id.item_category_title)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.item_category_delete_button)

        fun bindItem(categorie: String?){
            categoryName.text = categorie
            deleteButton.setOnClickListener {
                AlertDialog.Builder(itemView.context).apply {
                    setTitle("Deseja remover esta categoria?")
                    setPositiveButton("Sim") { _, _ ->
                        onDelete(categorie)
                    }
                    setNegativeButton("Não", null)
                }.show()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(categoriesList[position])
    }

    fun setCategorieList(list: MutableList<String>){
        categoriesList = list
        notifyDataSetChanged()
    }
}