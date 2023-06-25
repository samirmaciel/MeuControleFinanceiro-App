package com.samirmaciel.nossocontrolefinanceiro.model

import com.samirmaciel.nossocontrolefinanceiro.util.FilterTransactionType

data class FilterTransaction(
    var valueFilterType: FilterTransactionType? = null,
    var userNameListSelected: List<String>? = null,
    var categoryListSelected: List<String>? = null,
    var paymentTypeListSelected: List<String>? = null
)
