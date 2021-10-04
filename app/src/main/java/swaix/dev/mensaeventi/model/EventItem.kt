package swaix.dev.mensaeventi.model

import java.io.Serializable

open class EventItem(
    val id: Int,
    val name: String,
    val description: String,
    val type: ItemType,
    val imageURL: String,
    val position: Position,
    val mail: String,
    val link: String,
    val telephoneNumber: String,
) : Serializable