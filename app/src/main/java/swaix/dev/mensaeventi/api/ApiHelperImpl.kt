package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.Contacts
import swaix.dev.mensaeventi.model.Events
import swaix.dev.mensaeventi.model.FreeTime
import swaix.dev.mensaeventi.model.Hotels

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getEvents(): Response<Events> = apiService.getEvents()
    override suspend fun getHotels(): Response<Hotels> =apiService.getHotels()
    override suspend fun getFreeTime(): Response<FreeTime> = apiService.getFreeTime()
    override suspend fun getContacts(): Response<Contacts> = apiService.getContacts()
}