package swaix.dev.mensaeventi.ui.events

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.ResponseGetEvents
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    private val _events: MutableLiveData<NetworkResult<ResponseGetEvents>> = MutableLiveData()
    val events: LiveData<NetworkResult<ResponseGetEvents>> = _events

    private val details: MediatorLiveData<Boolean> = MediatorLiveData()

    fun fetchEventsResponse() {
        viewModelScope.launch {
            repository.getEvents().collect { values->
                _events.value = values
            }
        }
    }

    fun fetchEventDetails(id: String){
        viewModelScope.launch {

        }
    }

}