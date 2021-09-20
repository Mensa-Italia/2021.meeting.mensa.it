package swaix.dev.mensaeventi.api

import retrofit2.Response
import retrofit2.http.GET
import swaix.dev.mensaeventi.model.Contacts
import swaix.dev.mensaeventi.model.Events
import swaix.dev.mensaeventi.model.FreeTime
import swaix.dev.mensaeventi.model.Hotels

interface ApiService {
    @GET("events.asp")
    suspend fun getEvents(): Response<Events>

    @GET("hotels.asp")
    suspend fun getHotels(): Response<Hotels>

    @GET("tempolibero.asp")
    suspend fun getFreeTime(): Response<FreeTime>

    @GET("numeriutili.asp")
    suspend fun getContacts(): Response<Contacts>
}