package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.*

interface ApiHelper {
    suspend fun getEvents(): Response<ResponseGetEvents>
    suspend fun getEventDetails(id: String): Response<ResponseGetEventDetails>
    suspend fun putUser(name: String, surname: String, eventId: String, mensaId: String): Response<AckResponse>
    suspend fun isUserCheckedIn(mensaId: String): Response<ResponseIsUserCheckedIn>
    suspend fun putUserPosition(eventId: String,  mensaId: String,latitude: Double, longitude: Double): Response<AckResponse>
    suspend fun getUsersPositions(eventId: String, mensaId: String): Response<ResponseGetEventDetails>
}