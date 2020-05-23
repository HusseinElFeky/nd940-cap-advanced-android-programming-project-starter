package com.example.android.politicalpreparedness.binding

import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    @InverseMethod("stringToDate")
    @JvmStatic
    fun dateToString(value: Date?): String {
        return value?.toString() ?: ""
    }

    @JvmStatic
    fun stringToDate(value: String): Date? {
        return SimpleDateFormat.getInstance().parse(value)
    }
}
