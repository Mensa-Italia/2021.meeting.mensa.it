package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.*

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getEvents(): Response<ResponseGetEvents> = apiService.getEvents()
    override suspend fun getEventActivities(id: String): Response<ResponseGetEventActivities> = apiService.getEventActivities(id)
    override suspend fun getHotels(id: String): Response<Hotels> =apiService.getHotels(id)
    override suspend fun getFreeTime(id: String): Response<Suggestions> = apiService.getFreeTime(id)
    override suspend fun getContacts(id: String): Response<Contacts> = apiService.getContacts(id)
}