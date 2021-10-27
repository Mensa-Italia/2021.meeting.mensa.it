package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.*
import swaix.dev.mensaeventi.utils.dateString
import java.util.*

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getEvents(): Response<ResponseGetEvents> = apiService.getEvents()
    override suspend fun getEventDetails(id: String): Response<ResponseGetEventDetails> = apiService.getEventDetails(id)
    override suspend fun putUser(name: String, surname: String, eventId: String, mensaId: String): Response<AckResponse> = apiService.putUser(name, surname, eventId, mensaId, Date().dateString())
    override suspend fun isUserCheckedIn(mensaId: String): Response<ResponseIsUserCheckedIn> = apiService.isUserCheckedIn(mensaId)
    override suspend fun putUserPosition(eventId: String,  mensaId: String,latitude: Double, longitude: Double): Response<AckResponse> = apiService.putUserPosition(eventId, mensaId, latitude, longitude)
    override suspend fun getUsersPositions(eventId: String, mensaId: String): Response<ResponseGetEventDetails> = apiService.getUsersPositions(eventId, mensaId)
}
