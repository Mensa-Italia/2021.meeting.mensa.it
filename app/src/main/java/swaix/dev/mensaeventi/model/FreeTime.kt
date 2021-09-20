package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName

open class FreeTime(
    @SerializedName("eventmail") val mail: String,
    @SerializedName("eventname") val name: String,
    @SerializedName("tempolibero") val items: List<ItemDetails>,
    val lastUpdate: String
)
