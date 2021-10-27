package swaix.dev.mensaeventi.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import swaix.dev.mensaeventi.BuildConfig.MOCK_DATA
import swaix.dev.mensaeventi.api.ApiHelper
import swaix.dev.mensaeventi.api.BaseApiResponse
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.*

@ActivityRetainedScoped
class DataRepository(private val apiHelper: ApiHelper) : BaseApiResponse() {

    suspend fun getEvents(): Flow<NetworkResult<ResponseGetEvents>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(mockGetEventResponse()))
            } else {
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getEvents() })
                emit(NetworkResult.Loading(false))
            }
        }
    }

    suspend fun getEventDetails(id: String): Flow<NetworkResult<ResponseGetEventDetails>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(mockGetEventDetailsResponse()))
            } else {
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getEventDetails(id) })
                emit(NetworkResult.Loading(false))
            }
        }
    }

    suspend fun putUser(name: String, surname: String, eventId: String, mensaId: String): Flow<NetworkResult<AckResponse>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(mockPutUser()))
            } else {
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.putUser(name, surname, eventId, mensaId) })
                emit(NetworkResult.Loading(false))
            }
        }
    }

    suspend fun isUserCheckedIn(eventId: String): Flow<NetworkResult<ResponseIsUserCheckedIn>> {
        return flow {
            if (MOCK_DATA) {
//                emit(NetworkResult.Success(mockGetEventDetailsResponse()))
            } else {
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.isUserCheckedIn(eventId) })
                emit(NetworkResult.Loading(false))
            }
        }
    }

    suspend fun pushPosition(eventId: String, mensaId: String, latitude: Double, longitude: Double): Flow<NetworkResult<AckResponse>> {
        return flow {
            if (MOCK_DATA) {
//                emit(NetworkResult.Success(mockGetEventDetailsResponse()))
            } else {
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.putUserPosition(eventId, mensaId, latitude, longitude) })
                emit(NetworkResult.Loading(false))
            }
        }
    }
}
