package com.samirmaciel.nossocontrolefinanceiro.util

import android.content.res.Resources
import com.samirmaciel.nossocontrolefinanceiro.R
import io.grpc.internal.SharedResourceHolder.Resource

object LocaleUtils {

    fun getMonthNameByNumber(number: Int?, resource: Resources): String? {
        return when(number){
            1 -> resource.getString(R.string.janeiro)
            2 -> resource.getString(R.string.fevereiro)
            3 -> resource.getString(R.string.março)
            4 -> resource.getString(R.string.abril)
            5 -> resource.getString(R.string.maio)
            6 -> resource.getString(R.string.junho)
            7 -> resource.getString(R.string.julho)
            8 -> resource.getString(R.string.agosto)
            9 -> resource.getString(R.string.setembro)
            10 -> resource.getString(R.string.outubro)
            11 -> resource.getString(R.string.novembro)
            12 -> resource.getString(R.string.dezembro)
            else -> null
        }
    }
}