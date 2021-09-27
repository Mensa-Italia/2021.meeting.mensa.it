package swaix.dev.mensaeventi.api

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val isLoading: Boolean = false
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T>(isLoading: Boolean) : NetworkResult<T>(isLoading = isLoading)
}