package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName
import java.util.*

open class Events(
    @SerializedName("eventmail") val mail: String,
    @SerializedName("eventname") val name: String,
    @SerializedName("events") val items: List<EventsDetails>,
    val lastUpdate: String
)

data class EventsDetails(
    val description: String,
    val link: String,
    @SerializedName("title") val title: String,
    val position: Position,
    @SerializedName("date") val date: Date
)
