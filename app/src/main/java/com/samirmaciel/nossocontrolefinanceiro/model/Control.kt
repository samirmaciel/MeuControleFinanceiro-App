package com.samirmaciel.nossocontrolefinanceiro.model

import java.util.Date

data class Control(

    var id: String? = null,
    var description: String? = null,
    var categories: List<String>? = null,
    var creditCards: List<CreditCard>? = null,
    var transactions: List<Transaction>? = null,
    var members: List<User>? = null,
    var date: Date? = null,
    var createdBy: User? = null
)
