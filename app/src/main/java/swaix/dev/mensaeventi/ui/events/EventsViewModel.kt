package swaix.dev.mensaeventi.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.model.ResponseGetEvents
import swaix.dev.mensaeventi.model.ResponseIsUserCheckedIn
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    val eventsFlow: Flow<NetworkResult<ResponseGetEvents>> = repository.getEvents()


    fun getEventDetails(id: String): StateFlow<NetworkResult<ResponseGetEventDetails>> = repository.getEventDetails(id)
        .stateIn(
            initialValue = NetworkResult.Loading(),
            scope = viewModelScope,
            started = WhileSubscribed(5000),
        )

    fun isUserCheckedIn(mensaId: String, eventId: Int): Flow<NetworkResult<ResponseIsUserCheckedIn>> = repository.isUserCheckedIn(mensaId, eventId)

}