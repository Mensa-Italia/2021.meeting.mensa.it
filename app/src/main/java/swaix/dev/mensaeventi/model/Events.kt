package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

// region as-is
//
//open class _Events(
//    @SerializedName("eventmail") val mail: String = "",
//    @SerializedName("eventname") val name: String = "",
//    @SerializedName("events") val items: List<_EventsDetails> = listOf(),
//    val lastUpdate: String = ""
//)
//
//
//data class _EventsDetails(
//    private val description: String,
//    val link: String,
//    @SerializedName("title") private val name: String,
//    val position: Position,
//    @SerializedName("date") val date: Date
//) : EventActivityPresenter, Serializable {
//    override fun getName(): String = name
//    override fun getDescription(): String = description
//    override fun getDateFrom(): Date = date
//    override fun getDateTo(): Date = date
//    override fun getImageUrl(): String = ""
//}

// endregion

// region proposition


//EVENTO
//// ID
//// NOME
//// DESCRIZIONE
//// DATA DA
//// DATA A
//// IMMAGINE
//// LATLONG
//// DETTAGLLI LOCATION
//// LISTA ATTIVITA'
//////// ID ?
//////// NOME
//////// DESCRIZIONE
//////// DATETIME DA
//////// DATETIME A
//////// IMMAGINE
//////// LATLONG ?
//////// DETTAGLLI LOCATION ?
//// LISTA SUGGERIMENTI (DA NON PERDERE)
//////// ID ?
//////// NOME
//////// DESCRIZIONE
//////// IMMAGINE ?
//////// LATLONG ?
//////// DETTAGLLI LOCATION ?
//////// PREZZO
//////// CONTATTI?
//////// CONVENZIONI?
//// LISTA VITTO E ALLOGGIO
//////// ID ?
//////// NOME
//////// DESCRIZIONE
//////// IMMAGINE ?
//////// LATLONG ?
//////// DETTAGLLI LOCATION ?
//////// PREZZO
//////// CONTATTI?
//////// CONVENZIONI?


data class ResponseGetEvents(val events: List<MensaEvent> = listOf())

data class MensaEvent(
    val id: Int,
    @SerializedName("eventname") val name: String,
    val description: String,
    val dateFrom: Date,
    val dateTo: Date,
    val imageURL: String,
    val position: Position,
    @SerializedName("eventmail") val mail: String = ""
) : Serializable


// endregion