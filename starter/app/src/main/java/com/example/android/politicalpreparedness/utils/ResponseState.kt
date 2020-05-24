package com.husseinelfeky.githubpaging.common.paging.state

import androidx.annotation.StringRes

sealed class ResponseState {

    object Loading : ResponseState()

    object Loaded : ResponseState()

    data class Error(
        val error: Throwable? = null,
        @StringRes val messageRes: Int? = null
    ) : ResponseState() {
        override fun toString(): String {
            return error?.localizedMessage ?: "Unknown Error"
        }
    }
}
