package com.samirmaciel.nossocontrolefinanceiro.model.firebase

data class CreditCard(

    var id: String? = null,
    var description: String? = null,
    var limitTotal: Double? = null,
    var availableLimit: Double? = null,
    var installmentPurchases: List<InstallmentPurchase>? = null,
    var user: User? = null

){

    override fun toString(): String {
        return description.toString()
    }
}
