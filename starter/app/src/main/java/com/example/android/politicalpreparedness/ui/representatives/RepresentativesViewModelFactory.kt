package com.example.android.politicalpreparedness.ui.representatives

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService

class RepresentativesViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CivicsApiService::class.java).newInstance(
            CivicsApi.retrofitService
        )
    }
}
