package swaix.dev.mensaeventi.api

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import swaix.dev.mensaeventi.utils.TAG

abstract class NetworkObserver<T>(private val loadingManager: LoadingManager?= null, private val onNullResponse: ()->Unit = {}) : Observer<NetworkResult<T>> {
    override fun onChanged(t: NetworkResult<T>) {
        when (t) {
            is NetworkResult.Success -> {
                if (t.data != null)
                    onSuccess(t.data)
                else
                    onNullResponse
            }
            is NetworkResult.Error -> {
                onError(t.message)
            }
            is NetworkResult.Loading -> {
                loadingManager?.onLoading(t.isLoading)
            }
        }
    }


    abstract fun onSuccess(t: T)

    @CallSuper
    fun onError(message: String?) {
        Log.d(TAG, "** NETWORK ** onError: $message")
    }




}