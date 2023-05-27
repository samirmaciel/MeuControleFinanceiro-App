package com.samirmaciel.nossocontrolefinanceiro.model

import java.util.Date

data class Transaction(

    var id: String? = null,
    var description: String? = null,
    var category: String? = null,
    var type: String? = null,
    var creditCardId: String? = null,
    var date: Date? = null,
    var value: Double? = null,
    var user: User? = null

)
