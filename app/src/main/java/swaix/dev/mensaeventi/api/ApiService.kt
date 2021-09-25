package swaix.dev.mensaeventi.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import swaix.dev.mensaeventi.model.*

interface ApiService {
    @GET("events.asp")
    suspend fun getEvents(): Response<ResponseGetEvents>


    // PROPOSTA MIA PER FARE UNA STRUTTURA TIPO REST
    @GET("events.asp")
    suspend fun getEventActivities(@Path("eventID") id: String): Response<ResponseGetEventActivities>

    @GET("hotels.asp")
    suspend fun getHotels(@Path("eventID") id: String): Response<Hotels>

    @GET("tempolibero.asp")
    suspend fun getFreeTime(@Path("eventID") id: String): Response<Suggestions>

    @GET("numeriutili.asp")
    suspend fun getContacts(@Path("eventID") id: String): Response<Contacts>
}