package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


data class ResponseGetEventActivities(val eventActivity: List<MensaEventActivity> = listOf())

data class MensaEventActivity(
    val link: String,
    @SerializedName("title") private val name: String,
    val description: String,
    val position: Position,
    @SerializedName("date") val date: Date
) : Serializable
