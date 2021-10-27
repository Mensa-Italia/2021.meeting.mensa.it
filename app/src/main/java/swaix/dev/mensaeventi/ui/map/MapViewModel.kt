package swaix.dev.mensaeventi.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.ResponseGetUserPositions
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {


    private val _userPositions: MutableLiveData<NetworkResult<ResponseGetUserPositions>> = MutableLiveData()
    val userPositions: LiveData<NetworkResult<ResponseGetUserPositions>> = _userPositions


    fun switchUserPosition(eventId: String, mensaId: String) {
        if (repository.activateUserPositionFetch) {
            stopUserPositions()
        } else {
            fetchUserPositions(eventId, mensaId)
        }
    }

    private fun fetchUserPositions(eventId: String, mensaId: String) {
        viewModelScope.launch {
            repository.getUserPositions(eventId, mensaId).collect {
                _userPositions.value = it
            }
        }
    }

    private fun stopUserPositions() {
        repository.stopGetUserPositions()
    }


}