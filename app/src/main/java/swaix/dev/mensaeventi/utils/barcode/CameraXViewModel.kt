package swaix.dev.mensaeventi.utils.barcode

import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import swaix.dev.mensaeventi.MensaApp
import swaix.dev.mensaeventi.utils.TAG
import java.util.concurrent.ExecutionException
import javax.inject.Inject

@HiltViewModel
class CameraXViewModel @Inject constructor(application: MensaApp) : AndroidViewModel(application) {

    private val _cameraProvider = MutableLiveData<ProcessCameraProvider>()
    val cameraProvider: LiveData<ProcessCameraProvider> = _cameraProvider

    init {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(application)
        cameraProviderFuture.addListener(
            {
                try {
                    _cameraProvider.setValue(cameraProviderFuture.get())
                } catch (e: ExecutionException) {
                    // Handle any errors (including cancellation) here.
                    Log.e(TAG, "Unhandled exception", e)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "Unhandled exception", e)
                }
            },
            ContextCompat.getMainExecutor(application)
        )
    }
}