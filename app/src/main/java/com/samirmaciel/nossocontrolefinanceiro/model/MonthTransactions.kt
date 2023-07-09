package com.samirmaciel.nossocontrolefinanceiro.model

import com.samirmaciel.nossocontrolefinanceiro.model.firebase.Transaction

data class MonthTransactions(
    var monthName: String? = null,
    var year: String? = null,
    var transactions: List<Transaction?>? = null,
    var totalValue: Double? = null
)
