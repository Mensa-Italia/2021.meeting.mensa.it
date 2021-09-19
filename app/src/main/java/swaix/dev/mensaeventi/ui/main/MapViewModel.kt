package swaix.dev.mensaeventi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.MensaEvent
import swaix.dev.mensaeventi.repository.EventRepository
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(private val repository: EventRepository) : ViewModel() {

    private val _response: MutableLiveData<NetworkResult<MensaEvent>> = MutableLiveData()
    val response: LiveData<NetworkResult<MensaEvent>> = _response

    fun fetchMensaEventResponse() {
        viewModelScope.launch {
            repository.getEvents().collect { values->
                _response.value = values
            }
        }
    }

}