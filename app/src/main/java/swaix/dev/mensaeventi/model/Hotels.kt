package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName


data class Hotels(
    @SerializedName("eventmail") val mail: String ="",
    @SerializedName("eventname") val name: String ="",
    @SerializedName("hotels") val items: List<ItemDetails> = listOf(),
    val lastUpdate: String = ""
)
