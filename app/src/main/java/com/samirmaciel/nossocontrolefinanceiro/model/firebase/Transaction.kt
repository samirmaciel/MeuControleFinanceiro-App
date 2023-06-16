package com.samirmaciel.nossocontrolefinanceiro.model.firebase

import java.util.Date

data class Transaction(

    var id: String? = null,
    var description: String? = null,
    var category: String? = null,
    var type: String? = null,
    var creditCardId: String? = null,
    var day: String? = null,
    var month: String? = null,
    var year: String? = null,
    var date: Date? = null,
    var value: Double? = null,
    var userID: String? = null,
    var user: User? = null

)
