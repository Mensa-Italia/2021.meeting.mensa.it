package swaix.dev.mensaeventi.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import swaix.dev.mensaeventi.BuildConfig.MOCK_DATA
import swaix.dev.mensaeventi.api.ApiHelper
import swaix.dev.mensaeventi.api.BaseApiResponse
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.*

@ActivityRetainedScoped
class DataRepository(private val apiHelper: ApiHelper) : BaseApiResponse() {


    fun getEvents(): Flow<NetworkResult<ResponseGetEvents>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(mockGetEventResponse()))
            } else {
                emit(safeApiCall { apiHelper.getEvents() })
            }
        }
    }

    fun getEventDetails(id: String): Flow<NetworkResult<ResponseGetEventDetails>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(mockGetEventDetailsResponse()))
            } else {
                emit(safeApiCall { apiHelper.getEventDetails(id) })
            }
        }
    }

    fun putUser(name: String, surname: String, eventId: String, mensaId: String): Flow<NetworkResult<AckResponse>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(mockPutUser()))
            } else {
                emit(safeApiCall { apiHelper.putUser(name, surname, eventId, mensaId) })
            }
        }
    }

    fun isUserCheckedIn(mensaId: String, eventId: Int): Flow<NetworkResult<ResponseIsUserCheckedIn>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(mockIsUserCheckedIn()))
            } else {
                emit(safeApiCall { apiHelper.isUserCheckedIn(mensaId, eventId) })
            }
        }
    }

    fun pushPosition(eventId: String, mensaId: String, latitude: Double, longitude: Double): Flow<NetworkResult<AckResponse>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(mockPutUser()))
            } else {
                emit(safeApiCall { apiHelper.putUserPosition(eventId, mensaId, latitude, longitude) })
            }
        }
    }


    fun getUserPositions(eventId: String, mensaId: String): Flow<NetworkResult<ResponseGetUserPositions>> {
        return flow {
            if (MOCK_DATA) {
                emit(NetworkResult.Success(muckGetUserPositions()))
            } else {
                while (true) {
                    emit(safeApiCall { apiHelper.getUsersPositions(eventId, mensaId) })
                    delay(10000)
                }
            }
        }
    }

}
