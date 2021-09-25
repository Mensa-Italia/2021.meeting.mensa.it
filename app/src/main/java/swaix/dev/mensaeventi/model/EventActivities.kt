package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


data class ResponseGetEventActivities(val eventActivity: List<MensaEventActivity> = listOf())

data class MensaEventActivity(
    @SerializedName("title") val name: String,
    val description: String,
    val position: Position,
    val link: String,
//    @SerializedName("date") val date: Date,
    val dateFrom: Date,
    val dateTo: Date,
) : Serializable
