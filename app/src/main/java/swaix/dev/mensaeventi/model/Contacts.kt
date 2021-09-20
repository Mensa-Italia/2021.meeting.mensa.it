package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName

open class Contacts(
    @SerializedName("eventmail") val mail: String,
    @SerializedName("eventname") val name: String,
    @SerializedName("numeriutili") val items: List<ItemDetails>,
    val lastUpdate: String
)
