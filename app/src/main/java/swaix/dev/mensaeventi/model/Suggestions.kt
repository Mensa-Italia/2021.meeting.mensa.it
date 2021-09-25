package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName

open class Suggestions(
    @SerializedName("eventmail") val mail: String = "",
    @SerializedName("eventname") val name: String = "",
    @SerializedName("tempolibero") val items: List<ItemDetails> = listOf(),
    val lastUpdate: String = ""
)
