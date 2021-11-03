package swaix.dev.mensaeventi.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseViewModel : ViewModel() {

    val buttonShareText  = MutableLiveData<SharePositionData>()
    val showLoading = MutableLiveData(false)

    val hasCheckedIn : MutableLiveData<String> = MutableLiveData()

}

data class SharePositionData(
    val title: String,
    val selected: Boolean,
)