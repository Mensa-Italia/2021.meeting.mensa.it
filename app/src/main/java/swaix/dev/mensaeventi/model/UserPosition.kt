package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName

data class UserPosition(
    @SerializedName("nome") val name: String,
    @SerializedName("cognome") val surname: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
)