package com.samirmaciel.nossocontrolefinanceiro.model


data class FilterCreditCard(
    var userNameListSelected: List<String>? = null,
    var categoryListSelected: List<String>? = null,
    var paymentTypeListSelected: List<String>? = null

)
