package swaix.dev.mensaeventi.model

import java.io.Serializable

data class Position(
    val address: String,
    val locationInfo: String, // for example floor
    val latitude: Double,
    val longitude: Double
): Serializable