package swaix.dev.mensaeventi.ui.events

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.*
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    private val _events: MutableLiveData<NetworkResult<ResponseGetEvents>> = MutableLiveData()
    private val _eventDetails: MutableLiveData<NetworkResult<ResponseGetEventDetails>> = MutableLiveData()

    val events: LiveData<NetworkResult<ResponseGetEvents>> = _events
    val eventDetails: LiveData<NetworkResult<ResponseGetEventDetails>> = _eventDetails

    fun fetchEventsResponse() {
        viewModelScope.launch {
            repository.getEvents().collect { values->
                _events.value = values
            }
        }
    }

    fun fetEventDetails(id: String){
        viewModelScope.launch {
            repository.getEventDetails(id).collect {
                _eventDetails.value = it
            }
        }
    }

}