package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.MensaEvent

interface ApiHelper {
    suspend fun getEvents(): Response<MensaEvent>
}