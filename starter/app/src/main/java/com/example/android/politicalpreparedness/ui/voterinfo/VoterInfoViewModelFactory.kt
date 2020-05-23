package com.example.android.politicalpreparedness.ui.voterinfo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase

class VoterInfoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ElectionDao::class.java).newInstance(
            ElectionDatabase.getInstance(context).electionDao
        )
    }
}
