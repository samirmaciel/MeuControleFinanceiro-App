package com.samirmaciel.nossocontrolefinanceiro.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.lang.NumberFormatException
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(private val editText : EditText, val locale : Locale) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }


    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if(s.toString().isNotEmpty()){
            editText.removeTextChangedListener(this)

            val monetarySignal = Currency.getInstance(locale).symbol

            var cleanString = s.toString().replace("[${monetarySignal},.]".toRegex(), "").replace("\\s+".toRegex(), "")

            if(cleanString.length != 0){

                try {
                    var parsed : Double
                    var formatted : String

                    parsed = cleanString.toDouble()

                    formatted = NumberFormat.getCurrencyInstance(locale).format(parsed / 100)

                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                }catch (e : NumberFormatException){
                    Log.d("MoneyTextWatcher", "onTextChanged: " + e.printStackTrace().toString())
                }
            }
            editText.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }

}