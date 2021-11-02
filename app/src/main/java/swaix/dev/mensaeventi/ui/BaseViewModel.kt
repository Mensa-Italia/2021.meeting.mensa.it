package swaix.dev.mensaeventi.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseViewModel : ViewModel() {

    val locationServiceEnable = MutableLiveData(false)
    val showLoading = MutableLiveData(false)

    val hasCheckedIn_ : MutableLiveData<String> = MutableLiveData()

}