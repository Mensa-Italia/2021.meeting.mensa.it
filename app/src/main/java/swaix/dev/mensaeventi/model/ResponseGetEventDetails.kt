package swaix.dev.mensaeventi.model

import java.io.Serializable
import java.util.*

class ResponseGetEventDetails(
    id: Int,
    name: String,
    description: String,
    dateFrom: Date,
    dateTo: Date,
    type: ItemType,
    imageURL: String,
    position: Position,
    mail: String,
    link: String,
    telephoneNumber: String,
    val eventActivities: List<EventItemWithDate>,
    val eventHotel: List<EventItem>,
    val eventsSuggestions: List<EventItem>,
    val eventsContacts: List<EventItem>,
) : EventItemWithDate(id, name, description, dateFrom, dateTo, type, imageURL, position, mail, link, telephoneNumber), Serializable