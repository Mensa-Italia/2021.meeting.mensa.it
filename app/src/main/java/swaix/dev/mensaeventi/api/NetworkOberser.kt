package swaix.dev.mensaeventi.api

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import swaix.dev.mensaeventi.utils.TAG

abstract class NetworkObserver<T>(private val loadingManager: LoadingManager?= null, private val onNullResponse: ()->Unit = {}) : Observer<NetworkResult<T>> {
    override fun onChanged(value: NetworkResult<T>) {
        when (value) {
            is NetworkResult.Success -> {
                if (value.data != null)
                    onSuccess(value.data)
                else
                    onNullResponse
            }
            is NetworkResult.Error -> {
                onError(value.message)
            }
            is NetworkResult.Loading -> {
                loadingManager?.onLoading(value.isLoading)
            }
        }
    }


    abstract fun onSuccess(value: T)

    @CallSuper
    fun onError(message: String?) {
        Log.d(TAG, "** NETWORK ** onError: $message")
    }




}