package com.example.android.politicalpreparedness.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Division(
    val id: String,
    val country: String,
    val state: String
) : Parcelable {

    fun toFormattedString(): String {
        var output = ""
        if (state.isNotEmpty()) output = output.plus(state)
        if (output.isNotEmpty() && country.isNotEmpty()) {
            output = output.plus(", ")
        }
        if (country.isNotEmpty()) output = output.plus(country)
        return output
    }
}
