package swaix.dev.mensaeventi.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import swaix.dev.mensaeventi.api.ApiHelper
import swaix.dev.mensaeventi.api.BaseApiResponse
import swaix.dev.mensaeventi.api.LoadingManager
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.model.MensaEvent

@ActivityRetainedScoped
class EventRepository(private val apiHelper: ApiHelper) : BaseApiResponse() {

    suspend fun getEvents(): Flow<NetworkResult<MensaEvent>> {
        return flow {
            emit(NetworkResult.Loading(true))
            emit(safeApiCall { apiHelper.getEvents() })
            emit(NetworkResult.Loading(false))
        }.flowOn(Dispatchers.IO)
    }
}