package com.samirmaciel.nossocontrolefinanceiro.model

data class CreditCard(

    var id: String? = null,
    var name: String? = null,
    var limitTotal: Double? = null,
    var availableLimit: Double? = null,
    var installmentPayments: List<InstallmentPayment>? = null,
    var user: User? = null

)
