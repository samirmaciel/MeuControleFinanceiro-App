package com.samirmaciel.nossocontrolefinanceiro.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstallmentPurchase(

    var id: String? = null,
    var description: String? = null,
    var totalInstallment: Int? = null,
    var installmentsPaid: Int? = null,
    var value: Double? = null

): Parcelable
