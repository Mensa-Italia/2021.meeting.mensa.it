package swaix.dev.mensaeventi.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseViewModel : ViewModel() {

    val locationServiceEnable = MutableLiveData(false)

}