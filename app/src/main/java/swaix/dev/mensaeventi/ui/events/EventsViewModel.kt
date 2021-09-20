package swaix.dev.mensaeventi.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.Events
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    private val _events: MutableLiveData<NetworkResult<Events>> = MutableLiveData()
    val events: LiveData<NetworkResult<Events>> = _events

    fun fetchEventsResponse() {
        viewModelScope.launch {
            repository.getEvents().collect { values->
                _events.value = values
            }
        }
    }

}