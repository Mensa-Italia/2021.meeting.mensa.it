package swaix.dev.mensaeventi.repository

import android.util.Log
import com.google.gson.GsonBuilder
import swaix.dev.mensaeventi.model.*
import swaix.dev.mensaeventi.utils.TAG
import java.text.SimpleDateFormat
import java.util.*

var dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

fun <T> T.log() : T{
    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm Z").create()
    val json = gson.toJson(this)
    Log.d(TAG, "*** TO JSON *** $json")
    return this
}

fun mockGetEventResponse() = ResponseGetEvents(
    listOf(
        MensaEvent(
            1, "Roma", "lorem ipsum",
            dateFormat.parse("23/11/2022") ?: Date(),
            dateFormat.parse("25/11/2022") ?: Date(),
            "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
            Position("via vedremo", "bla bla bla", 42.1, 39.1)
        ),
        MensaEvent(
            2, "Milano", "lorem ipsum",
            dateFormat.parse("30/01/2023") ?: Date(),
            dateFormat.parse("02/02/2023") ?: Date(),
            "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
            Position("via vedremo", "bla bla bla", 42.1, 39.1)
        ),
        MensaEvent(
            3, "Torino", "lorem ipsum",
            dateFormat.parse("31/12/2023") ?: Date(),
            dateFormat.parse("01/01/2024") ?: Date(),
            "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
            Position("via vedremo", "bla bla bla", 42.1, 39.1)
        ),
    )
).log()


fun mockGetEventActivitiesResponse() =ResponseGetEventActivities(
    listOf(
        MensaEventActivity(
            "Roma ATTIVITa",
            "prima attività a a Roma",
            Position("via vedremo", "bla bla bla", 42.1, 39.1),
            "",
            dateFormat.parse("23/11/2022 09:00") ?: Date(),
            dateFormat.parse("23/11/2022 18:00") ?: Date(),
        ),
        MensaEventActivity(
            "Roma ATTIVITa 2",
            "seconda attività a a Roma",
            Position("via vedremo", "bla bla bla", 42.1, 39.1),
            "",
            dateFormat.parse("24/11/2022 09:00") ?: Date(),
            dateFormat.parse("24/11/2022 18:00") ?: Date(),
        ),
        MensaEventActivity(
            "Roma ATTIVITa 3",
            "terza attività a a Roma",
            Position("via vedremo", "bla bla bla", 42.1, 39.1),
            "",
            dateFormat.parse("25/11/2022 09:00") ?: Date(),
            dateFormat.parse("25/11/2022 18:00") ?: Date(),
        ),
    )
).log()
fun mockGetHotelsResponse() =Hotels().log()
fun mockGetContactsResponse() =Contacts().log()
fun mockGetSuggestions() =Suggestions().log()
