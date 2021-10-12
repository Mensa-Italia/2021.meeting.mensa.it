package swaix.dev.mensaeventi.api

import retrofit2.Response
import retrofit2.http.*
import swaix.dev.mensaeventi.model.*

interface ApiService {
    @Headers("Content-Type:application/json; charset=UTF-8")
    @GET("getevents.asp")
    suspend fun getEvents(): Response<ResponseGetEvents>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @GET("geteventdetails.asp")
    suspend fun getEventDetails(@Query("eventID") id: String): Response<ResponseGetEventDetails>
}