package com.example.android.politicalpreparedness.models

data class Address(
    val line1: String?,
    val line2: String?,
    val city: String?,
    val state: String?,
    val zip: String?
) {

    fun toFormattedString(): String {
        var output = ""
        if (!line1.isNullOrEmpty()) output = output.plus(line1).plus("\n")
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")
        if (!city.isNullOrEmpty()) {
            output = output.plus(city)
            if (!state.isNullOrEmpty() || !zip.isNullOrEmpty()) {
                output = output.plus(", ")
            }
        }
        if (!state.isNullOrEmpty()) output = output.plus(state)
        if (output.isNotEmpty() && output.takeLast(2) != ", ") output = output.plus(" ")
        if (!zip.isNullOrEmpty()) output = output.plus(zip)
        return output
    }
}
