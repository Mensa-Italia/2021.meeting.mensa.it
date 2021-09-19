package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.MensaEvent

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getEvents(): Response<MensaEvent> = apiService.getEvents()
}