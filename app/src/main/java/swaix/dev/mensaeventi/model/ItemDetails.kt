package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName
import java.util.*

open class ItemDetails(
    val description: String,
    val link: String,
    val name: String,
    val position: Position
) {
}