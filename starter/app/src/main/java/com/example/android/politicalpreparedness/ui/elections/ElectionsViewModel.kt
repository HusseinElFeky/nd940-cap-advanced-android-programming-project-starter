package com.example.android.politicalpreparedness.ui.elections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.husseinelfeky.githubpaging.common.paging.state.ResponseState
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

    private val _stateUpcomingElections = MutableLiveData<ResponseState>()
    val stateUpcomingElections: LiveData<ResponseState>
        get() = _stateUpcomingElections

    private val _stateSavedElections = MutableLiveData<ResponseState>()
    val stateSavedElections: LiveData<ResponseState>
        get() = _stateSavedElections

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _stateUpcomingElections.postValue(
            ResponseState.Error(
                messageRes = R.string.error_no_internet_connection
            )
        )
    }

    init {
        getUpcomingElections()
        getSavedElections()
    }

    private fun getUpcomingElections() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        _stateUpcomingElections.postValue(ResponseState.Loading)

        val response = civicsApi.getElections()
        if (response.isSuccessful) {
            response.body()?.let { electionsResponse ->
                _upcomingElections.postValue(electionsResponse.elections)
            }
            _stateUpcomingElections.postValue(ResponseState.Loaded)
            return@launch
        }

        _stateUpcomingElections.postValue(
            ResponseState.Error(
                messageRes = R.string.error_no_internet_connection
            )
        )
    }

    private fun getSavedElections() = viewModelScope.launch {
        _stateSavedElections.postValue(ResponseState.Loading)
        savedElections = electionDao.getAllElections()
        _stateSavedElections.postValue(ResponseState.Loaded)
    }
}
