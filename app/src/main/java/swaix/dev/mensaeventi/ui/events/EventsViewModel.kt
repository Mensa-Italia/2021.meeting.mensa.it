package swaix.dev.mensaeventi.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.model.ResponseGetEvents
import swaix.dev.mensaeventi.model.ResponseGetUserPositions
import swaix.dev.mensaeventi.model.ResponseIsUserCheckedIn
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    private val _events: MutableLiveData<NetworkResult<ResponseGetEvents>> = MutableLiveData()
    private val _eventDetails: MutableLiveData<NetworkResult<ResponseGetEventDetails>> = MutableLiveData()
    private val _isUserCheckedIn: MutableLiveData<NetworkResult<ResponseIsUserCheckedIn>> = MutableLiveData()

    val events: LiveData<NetworkResult<ResponseGetEvents>> = _events
    val eventDetails: LiveData<NetworkResult<ResponseGetEventDetails>> = _eventDetails
    val isUserCheckedIn: LiveData<NetworkResult<ResponseIsUserCheckedIn>> = _isUserCheckedIn

    fun fetchEventsResponse() {
        viewModelScope.launch {
            repository.getEvents().collect { values ->
                _events.value = values
            }
        }
    }

    fun fetchEventDetails(id: String) {
        viewModelScope.launch {
            repository.getEventDetails(id).collect {
                _eventDetails.value = it
            }
        }
    }

    fun fetchIsUserCheckedIn(eventId: String) {
        viewModelScope.launch {
            repository.isUserCheckedIn(eventId).collect {
                _isUserCheckedIn.value = it
            }
        }
    }
}