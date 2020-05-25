package com.example.android.politicalpreparedness.ui.voterinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.models.VoterInfoResponse
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.husseinelfeky.githubpaging.common.paging.state.ResponseState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val civicsApi: CivicsApiService,
    private val electionDao: ElectionDao
) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _stateVoterInfo = MutableLiveData<ResponseState>()
    val stateVoterInfo: LiveData<ResponseState>
        get() = _stateVoterInfo

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _stateVoterInfo.postValue(
            ResponseState.Error(
                messageRes = R.string.error_no_internet_connection
            )
        )
    }

    private val _isElectionFollowed = MutableLiveData<Boolean>()
    val isElectionFollowed: LiveData<Boolean>
        get() = _isElectionFollowed

    fun fetchDetails(election: Election) = viewModelScope.launch {
        _isElectionFollowed.postValue(electionDao.getElection(election.id) != null)
        getVoterInfo(election)
    }

    private fun getVoterInfo(election: Election) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        _stateVoterInfo.postValue(ResponseState.Loading)

        val response = civicsApi.getVoterInfo(
            election.division.toFormattedString(),
            election.id
        )
        if (response.isSuccessful) {
            response.body()?.let { voterInfo ->
                _voterInfo.postValue(voterInfo)
            }
            _stateVoterInfo.postValue(ResponseState.Loaded)
            return@launch
        }

        _stateVoterInfo.postValue(
            ResponseState.Error(
                messageRes = R.string.error_server_unknown
            )
        )
    }

    fun followOrUnfollowElection(election: Election) = viewModelScope.launch {
        _isElectionFollowed.postValue(
            if (isElectionFollowed.value == true) {
                electionDao.deleteElection(election)
                false
            } else {
                electionDao.insert(election)
                true
            }
        )
    }
}
