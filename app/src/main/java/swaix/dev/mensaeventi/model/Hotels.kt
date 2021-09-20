package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName


open class Hotels(
    @SerializedName("eventmail") val mail: String,
    @SerializedName("eventname") val name: String,
    @SerializedName("hotels") val items: List<ItemDetails>,
    val lastUpdate: String
)
