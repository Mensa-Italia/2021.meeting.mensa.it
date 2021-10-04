package swaix.dev.mensaeventi.model

import java.io.Serializable
import java.util.*

open class EventItemWithDate(
    id: Int,
    name: String,
    description: String,
    val dateFrom: Date,
    val dateTo: Date,
    type: ItemType,
    imageURL: String,
    position: Position,
    mail: String,
    link: String,
    telephoneNumber: String,
) : EventItem(id, name, description, type, imageURL, position, mail, link, telephoneNumber), Serializable