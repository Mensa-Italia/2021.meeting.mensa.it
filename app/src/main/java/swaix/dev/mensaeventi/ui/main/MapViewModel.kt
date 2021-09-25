package swaix.dev.mensaeventi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.ResponseGetEvents
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    private val _response: MutableLiveData<NetworkResult<ResponseGetEvents>> = MutableLiveData()
    val response: LiveData<NetworkResult<ResponseGetEvents>> = _response

    fun fetchEventsResponse() {
        viewModelScope.launch {
            repository.getEvents().collect { values ->
                _response.value = values
            }
//            repository.getContacts().collect { values ->
////                _response.value = values
//            }
//            repository.getFreeTime().collect { values ->
////                _response.value = values
//            }
//            repository.getHotels().collect { values ->
////                _response.value = values
//            }
        }
    }

}