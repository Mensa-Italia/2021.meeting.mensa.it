package swaix.dev.mensaeventi.repository

import android.util.Log
import com.google.gson.GsonBuilder
import swaix.dev.mensaeventi.model.*
import swaix.dev.mensaeventi.utils.TAG
import java.text.SimpleDateFormat
import java.util.*

var dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

fun <T> T.log(): T {
    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm Z").create()
    val json = gson.toJson(this)
    Log.d(TAG, "*** TO JSON *** $json")
    return this
}

fun mockGetEventResponse() = ResponseGetEvents(
    listOf(
        EventItemWithDate(
            1, "Roma", "lorem ipsum",
            dateFormat.parse("23/11/2022") ?: Date(),
            dateFormat.parse("25/11/2022") ?: Date(),
            "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            ""
        ),
        EventItemWithDate(
            2, "Milano", "lorem ipsum",
            dateFormat.parse("30/01/2023") ?: Date(),
            dateFormat.parse("02/02/2023") ?: Date(),
            "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            ""
        ),
        EventItemWithDate(
            3, "Torino", "lorem ipsum",
            dateFormat.parse("31/12/2023") ?: Date(),
            dateFormat.parse("01/01/2024") ?: Date(),
            "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            ""
        ),
    )
).log()


fun mockGetEventDetailsResponse() = ResponseGetEventDetails(
    1, "Roma", "lorem ipsum",
    dateFormat.parse("23/11/2022") ?: Date(),
    dateFormat.parse("25/11/2022") ?: Date(),
    "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
    Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
    "",
    "",
    "",
    listOf(
        EventItemWithDate(
            1,
            "Attività 1",
            "prima attività",
            dateTimeFormat.parse("23/11/2022 09:00") ?: Date(),
            dateTimeFormat.parse("23/11/2022 18:00") ?: Date(),
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            "",
        ),
        EventItemWithDate(
            2,
            "Attività 2",
            "seconda attività",
            dateTimeFormat.parse("23/11/2022 09:00") ?: Date(),
            dateTimeFormat.parse("23/11/2022 18:00") ?: Date(),
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            "",
        ),
        EventItemWithDate(
            3,
            "Attività 3",
            "terza attività",
            dateTimeFormat.parse("23/11/2022 09:00") ?: Date(),
            dateTimeFormat.parse("23/11/2022 18:00") ?: Date(),
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            "",
        ),
    ),
    listOf(
        EventItem(
            123123,
            "Albergo 1",
            "prima albergo",
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "albergo@asdf.it",
            "www.albergo.it",
            "32054191",
        ),
        EventItem(
            123124,
            "Albergo 2",
            "secondo albergo",
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "albergo@asdf.it",
            "www.albergo.it",
            "065246131",
        ),
        EventItem(
            123125,
            "Albergo 3",
            "terzo albergo",
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "albergo@asdf.it",
            "www.albergo.it",
            "094161822",
        ),
    ),
    listOf(
        EventItem(
            123125,
            "Ristorante 1",
            "ristorante",
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "albergo@asdf.it",
            "www.albergo.it",
            "0646490",
        ),
    ),
    listOf(
        EventItem(
            123125,
            "Albergo 3",
            "terzo albergo",
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "albergo@asdf.it",
            "www.albergo.it",
            "066598461",
        ),
    ),
).log()