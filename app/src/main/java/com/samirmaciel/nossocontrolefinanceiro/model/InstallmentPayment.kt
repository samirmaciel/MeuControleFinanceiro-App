package com.samirmaciel.nossocontrolefinanceiro.model

data class InstallmentPayment(

    var id: String? = null,
    var description: String? = null,
    var totalInstallment: Int? = null,
    var installmentsPaid: Int? = null,
    var value: Double? = null

)
