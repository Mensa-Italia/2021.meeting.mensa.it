package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.*

interface ApiHelper {
    suspend fun getEvents(): Response<ResponseGetEvents>
    suspend fun getEventActivities(id: String): Response<ResponseGetEventActivities>
    suspend fun getHotels(id: String): Response<Hotels>
    suspend fun getFreeTime(id: String): Response<Suggestions>
    suspend fun getContacts(id: String): Response<Contacts>
}