package swaix.dev.mensaeventi.model

import java.io.Serializable
import java.util.*

open class EventItemWithDate(
    id: Int,
    name: String,
    description: String,
    val dateFrom: Date,
    val dateTo: Date,
    imageURL: String,
    position: Position,
    mail: String,
    link: String,
    telephoneNumber: String,
) : EventItem(id, name, description, imageURL, position, mail, link, telephoneNumber), Serializable