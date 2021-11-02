package swaix.dev.mensaeventi.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseViewModel : ViewModel() {

    val showLoading = MutableLiveData(false)

    val hasCheckedIn : MutableLiveData<String> = MutableLiveData()

}