package swaix.dev.mensaeventi.api

import retrofit2.Response
import retrofit2.http.GET
import swaix.dev.mensaeventi.model.MensaEvent

interface ApiService {
    @GET("2c478291-45bc-47a0-8682-a3e56d55b570")
    suspend fun getEvents(): Response<MensaEvent>
}