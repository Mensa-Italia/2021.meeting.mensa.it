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
                emit(NetworkResult.Success(mockGetEventResponse()))
            } else {
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getEvents() })
                emit(NetworkResult.Loading(false))
            }
        }
    }

    suspend fun getEventActivities(id: String): Flow<NetworkResult<ResponseGetEventActivities>> {
        return flow {
            if (MOCK_DATA){
                emit(NetworkResult.Success(mockGetEventActivitiesResponse()))
            }
            else{
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getEventActivities(id) })
                emit(NetworkResult.Loading(false))
            }
        }
    }

    suspend fun getHotels(id: String): Flow<NetworkResult<Hotels>> {
        return flow {
            if (MOCK_DATA){
                emit(NetworkResult.Success(mockGetHotelsResponse()))
            }
            else{
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getHotels(id) })
                emit(NetworkResult.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getContacts(id: String): Flow<NetworkResult<Contacts>> {
        return flow {
            if (MOCK_DATA){
                emit(NetworkResult.Success(mockGetContactsResponse()))
            }
            else{
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getContacts(id) })
                emit(NetworkResult.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFreeTime(id: String): Flow<NetworkResult<Suggestions>> {
        return flow {
            if (MOCK_DATA){
                emit(NetworkResult.Success(mockGetSuggestions()))
            }
            else{
                emit(NetworkResult.Loading(true))
                emit(safeApiCall { apiHelper.getFreeTime(id) })
                emit(NetworkResult.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }
}