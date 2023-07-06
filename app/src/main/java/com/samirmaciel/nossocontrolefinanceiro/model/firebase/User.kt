package com.samirmaciel.nossocontrolefinanceiro.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class User(

    var id: String? = null,
    var name: String? = null,
    var image: String? = null,
    var currentControlId: String? = null

): Parcelable
