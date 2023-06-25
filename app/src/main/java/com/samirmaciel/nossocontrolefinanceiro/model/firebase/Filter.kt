package com.samirmaciel.nossocontrolefinanceiro.model.firebase

import com.samirmaciel.nossocontrolefinanceiro.util.FilterTransactionType

data class Filter(
    var name: String,
    var filterType: FilterTransactionType,
    var isActive: Boolean
)