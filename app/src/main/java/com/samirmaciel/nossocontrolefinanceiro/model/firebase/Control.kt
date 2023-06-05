package com.samirmaciel.nossocontrolefinanceiro.model.firebase

import java.util.Date

data class Control(

    var id: String? = null,
    var description: String? = null,
    var categories: MutableList<String>? = null,
    var creditCards: MutableList<CreditCard>? = null,
    var transactions: MutableList<Transaction>? = null,
    var members: MutableList<User>? = null,
    var date: Date? = null,
    var createdBy: User? = null
)
