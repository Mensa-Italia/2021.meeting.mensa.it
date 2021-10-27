package swaix.dev.mensaeventi.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import swaix.dev.mensaeventi.model.*

interface ApiService {
    @Headers("Content-Type:application/json; charset=ISO-8859-1")
    @GET("getevents.asp")
    suspend fun getEvents(): Response<ResponseGetEvents>

    @Headers("Content-Type:application/json; charset=ISO-8859-1")
    @GET("geteventdetails.asp")
    suspend fun getEventDetails(@Query("idevento") id: String): Response<ResponseGetEventDetails>

    @Headers("Content-Type:application/json; charset=ISO-8859-1")
    @GET("putUser.asp")
    suspend fun putUser(@Query("nome") name: String, @Query("cognome") surname: String, @Query("idevento") eventId: String, @Query("idunivoco") mensaId: String, @Query("dateTime") dateTime: String): Response<AckResponse>

    @Headers("Content-Type:application/json; charset=ISO-8859-1")
    @GET("isUserCheckedIn.asp")
    suspend fun isUserCheckedIn(@Query("idunivoco") mensaId: String): Response<ResponseIsUserCheckedIn>

    @Headers("Content-Type:application/json; charset=ISO-8859-1")
    @GET("putUserPosition.asp")
    suspend fun putUserPosition(@Query("idevento") eventId: String, @Query("idutenteunivoco") mensaId: String, @Query("lat") latitude: Double, @Query("long") longitude: Double): Response<AckResponse>

    @Headers("Content-Type:application/json; charset=ISO-8859-1")
    @GET("getUsersPositions.asp")
    suspend fun getUsersPositions(@Query("idevento") eventId: String, @Query("idutenteunivoco") mensaId: String): Response<ResponseGetUserPositions>
}