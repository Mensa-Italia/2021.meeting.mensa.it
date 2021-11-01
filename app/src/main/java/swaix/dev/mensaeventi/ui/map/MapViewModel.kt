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
    fun getUsersPositions(eventId: String, mensaId: String) = repository.getUserPositions(eventId, mensaId)
}