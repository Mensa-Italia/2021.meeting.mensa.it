package swaix.dev.mensaeventi.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import swaix.dev.mensaeventi.model.*

interface ApiService {
    @GET("events.asp")
    suspend fun getEvents(): Response<ResponseGetEvents>

    @GET("eventDetails.asp")
    suspend fun getEventDetails(@Path("eventID") id: String): Response<ResponseGetEventDetails>
}