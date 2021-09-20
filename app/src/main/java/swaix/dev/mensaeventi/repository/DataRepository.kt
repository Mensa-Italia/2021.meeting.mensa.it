package swaix.dev.mensaeventi.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import swaix.dev.mensaeventi.api.ApiHelper
import swaix.dev.mensaeventi.api.BaseApiResponse
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.Contacts
import swaix.dev.mensaeventi.model.Events
import swaix.dev.mensaeventi.model.FreeTime
import swaix.dev.mensaeventi.model.Hotels

@ActivityRetainedScoped
class DataRepository(private val apiHelper: ApiHelper) : BaseApiResponse() {

    suspend fun getEvents(): Flow<NetworkResult<Events>> {
        return flow {
            emit(NetworkResult.Loading(true))
            emit(safeApiCall { apiHelper.getEvents() })
            emit(NetworkResult.Loading(false))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getHotels(): Flow<NetworkResult<Hotels>> {
        return flow {
            emit(NetworkResult.Loading(true))
            emit(safeApiCall { apiHelper.getHotels() })
            emit(NetworkResult.Loading(false))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getContacts(): Flow<NetworkResult<Contacts>> {
        return flow {
            emit(NetworkResult.Loading(true))
            emit(safeApiCall { apiHelper.getContacts() })
            emit(NetworkResult.Loading(false))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFreeTime(): Flow<NetworkResult<FreeTime>> {
        return flow {
            emit(NetworkResult.Loading(true))
            emit(safeApiCall { apiHelper.getFreeTime() })
            emit(NetworkResult.Loading(false))
        }.flowOn(Dispatchers.IO)
    }
}