package com.example.android.politicalpreparedness.ui.representatives

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.models.Address
import com.example.android.politicalpreparedness.models.Representative
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepresentativesViewModel(
    private val civicsApi: CivicsApiService
) : ViewModel() {

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

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

    fun getRepresentatives(address: Address) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        _networkState.postValue(NetworkState.Loading)

        val response = civicsApi.getRepresentatives(address.toFormattedString())
        if (response.isSuccessful) {
            response.body()?.let { representative ->
                val (offices, officials) = representative
                _representatives.postValue(
                    offices.flatMap { office ->
                        office.getRepresentatives(officials)
                    }
                )
            }
            _networkState.postValue(NetworkState.Loaded)
            return@launch
        }

        _networkState.postValue(
            NetworkState.Error(
                messageRes = R.string.error_failed_to_fetch_representatives
            )
        )
    }

    fun updateAddress(address: Address) {
        _address.postValue(address)
    }
}
