package swaix.dev.mensaeventi.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.model.ResponseGetEvents
import swaix.dev.mensaeventi.model.ResponseIsUserCheckedIn
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    private val _eventDetails: MutableLiveData<NetworkResult<ResponseGetEventDetails>> = MutableLiveData()
    private val _isUserCheckedIn: MutableLiveData<NetworkResult<ResponseIsUserCheckedIn>> = MutableLiveData()

    val eventDetails: LiveData<NetworkResult<ResponseGetEventDetails>> = _eventDetails
    val isUserCheckedIn: LiveData<NetworkResult<ResponseIsUserCheckedIn>> = _isUserCheckedIn

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


    val eventsFlow: StateFlow<NetworkResult<ResponseGetEvents>> = repository.getEvents()
        .stateIn(
            initialValue = NetworkResult.Loading(),
            scope = viewModelScope,
            started = WhileSubscribed(500),
        )

    fun getEventDetails(id: String): Flow<NetworkResult<ResponseGetEventDetails>> = repository.getEventDetails(id)
    fun isUserCheckedIn(eventId: String): Flow<NetworkResult<ResponseIsUserCheckedIn>> = repository.isUserCheckedIn(eventId)
}