package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.Contacts
import swaix.dev.mensaeventi.model.Events
import swaix.dev.mensaeventi.model.FreeTime
import swaix.dev.mensaeventi.model.Hotels

interface ApiHelper {
    suspend fun getEvents(): Response<Events>
    suspend fun getHotels(): Response<Hotels>
    suspend fun getFreeTime(): Response<FreeTime>
    suspend fun getContacts(): Response<Contacts>
}