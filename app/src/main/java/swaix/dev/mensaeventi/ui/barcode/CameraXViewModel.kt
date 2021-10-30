package swaix.dev.mensaeventi.ui.barcode

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import swaix.dev.mensaeventi.MensaApp
import timber.log.Timber
import java.util.concurrent.ExecutionException
import javax.inject.Inject

/**
 * View model for interacting with CameraX.
 * Create an instance which interacts with the camera service via the given application context.
 */

@HiltViewModel
class CameraXViewModel @Inject constructor(application: MensaApp) : AndroidViewModel(application) {
    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    // Handle any errors (including cancellation) here.
    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            if (cameraProviderLiveData == null) {
                cameraProviderLiveData = MutableLiveData()
                val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())
                cameraProviderFuture.addListener(
                    {
                        try {
                            cameraProviderLiveData!!.setValue(cameraProviderFuture.get())
                        } catch (e: ExecutionException) {
                            // Handle any errors (including cancellation) here.
                            Timber.e(e, "Unhandled exception")
                        } catch (e: InterruptedException) {
                            Timber.e(e, "Unhandled exception")
                        }
                    },
                    ContextCompat.getMainExecutor(getApplication())
                )
            }
            return cameraProviderLiveData!!
        }

    companion object {
        private const val TAG = "CameraXViewModel"
    }
}