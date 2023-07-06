package com.samirmaciel.nossocontrolefinanceiro.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class CreditCard(

    var id: String? = null,
    var description: String? = null,
    var limitTotal: Double? = null,
    var availableLimit: Double? = null,
    var dueDate: Int? = null,
    var installmentPurchases: List<InstallmentPurchase>? = null,
    var user: User? = null

): Parcelable{

    override fun toString(): String {
        return description.toString()
    }
}
