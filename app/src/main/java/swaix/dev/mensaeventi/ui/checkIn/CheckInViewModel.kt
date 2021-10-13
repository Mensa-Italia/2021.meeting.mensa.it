package swaix.dev.mensaeventi.ui.checkIn

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import swaix.dev.mensaeventi.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class CheckInViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
}