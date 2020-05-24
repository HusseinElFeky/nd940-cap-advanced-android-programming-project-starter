package com.example.android.politicalpreparedness.ui.elections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ElectionsViewModel(
    private val civicsApi: CivicsApiService,
    private val electionDao: ElectionDao
) : ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    lateinit var savedElections: LiveData<List<Election>>

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _networkState.postValue(
            NetworkState.Error(
                messageRes = R.string.error_no_internet_connection
            )
        )
    }

    init {
        getUpcomingElections()
        getSavedElections()
    }

    private fun getUpcomingElections() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        _networkState.postValue(NetworkState.Loading)

        val response = civicsApi.getElections()
        if (response.isSuccessful) {
            response.body()?.let { electionsResponse ->
                _upcomingElections.postValue(electionsResponse.elections)
            }
            _networkState.postValue(NetworkState.Loaded)
            return@launch
        }

        _networkState.postValue(
            NetworkState.Error(
                messageRes = R.string.error_no_internet_connection
            )
        )
    }

    private fun getSavedElections() = viewModelScope.launch {
        savedElections = electionDao.getAllElections()
    }
}
