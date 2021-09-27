package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.*

interface ApiHelper {
    suspend fun getEvents(): Response<ResponseGetEvents>
    suspend fun getEventDetails(id: String): Response<ResponseGetEventDetails>
}