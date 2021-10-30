package swaix.dev.mensaeventi.ui.checkIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.AckResponse
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class CheckInViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {


    fun putUser(name: String, surname: String, eventId: String, mensaId: String) = repository.putUser(name, surname, eventId, mensaId)
//    private val _putUserResp: MutableLiveData<NetworkResult<AckResponse>> = MutableLiveData()
//
//    val putUserResp: LiveData<NetworkResult<AckResponse>> = _putUserResp
//
//    fun saveUser(name: String, surname: String, eventId: String, mensaId: String) {
//        viewModelScope.launch {
//            repository.putUser(name, surname, eventId, mensaId).collect { values->
//                _putUserResp.value = values
//            }
//        }
//    }
}