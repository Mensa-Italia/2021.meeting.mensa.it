package swaix.dev.mensaeventi.api

import retrofit2.Response
import swaix.dev.mensaeventi.model.*

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getEvents(): Response<ResponseGetEvents> = apiService.getEvents()
    override suspend fun getEventDetails(id: String): Response<ResponseGetEventDetails> = apiService.getEventDetails(id)
}