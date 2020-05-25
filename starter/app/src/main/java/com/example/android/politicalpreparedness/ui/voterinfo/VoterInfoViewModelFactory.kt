package com.example.android.politicalpreparedness.ui.voterinfo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService

class VoterInfoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            CivicsApiService::class.java,
            ElectionDao::class.java
        ).newInstance(
            CivicsApi.retrofitService,
            ElectionDatabase.getInstance(context).electionDao
        )
    }
}
