package swaix.dev.mensaeventi.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
                emit(NetworkResult.Success(mockEmpty()))
            } else {
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getEvents() })
                emit(NetworkResult.Loading(false))
            }
        }
    }

    suspend fun getEventDetails(id: String): Flow<NetworkResult<ResponseGetEventDetails>> {
        return flow {
            if (MOCK_DATA){
                emit(NetworkResult.Success(mockGetEventDetailsResponse()))
            }
            else{
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getEventDetails(id) })
                emit(NetworkResult.Loading(false))
            }
        }
    }
}