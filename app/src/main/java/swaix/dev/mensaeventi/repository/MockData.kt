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
            1, "Roma", "Porca Putténa! Nel mio ufficio non tollero uranisti te l'ho detto mille volte di non chiamarmi vecchio; abbiamo mangiato salame e prosciutto affrettato? M'avete preso per un coglione sono nichilista, nun me rompe li cojoni. I ricchioni si dividono in due grandi categorie è una famiglia schifosa; io vado a messa ogni domenica mattina? Marcolino un giorno ti dò una bottiglieta d'amaro qua a tracolla te la dò io beneficio benissimo così; che cagachezzo che è questo.",
            dateFormat.parse("23/11/2022") ?: Date(),
            dateFormat.parse("25/11/2022") ?: Date(),
            ItemType.EVENT,
            "https://vittoriosavoia.it/new/wp-content/uploads/2020/01/Colosseo.jpg",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            ""
        ),
        EventItemWithDate(
            2, "Milano", "lorem ipsum",
            dateFormat.parse("30/01/2023") ?: Date(),
            dateFormat.parse("02/02/2023") ?: Date(),
            ItemType.EVENT,
            "https://imbruttito.com/images/milan_cathedral_duomo_di_milano_evening_760x430-1.jpg?p=16x9&s=92cd3ea39351a606a373ea144c685eaa",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            ""
        ),
        EventItemWithDate(
            3, "Torino", "lorem ipsum",
            dateFormat.parse("31/12/2023") ?: Date(),
            dateFormat.parse("01/01/2024") ?: Date(),
            ItemType.EVENT,
            "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            ""
        ),
    )
).log()


fun mockGetEventDetailsResponse() = ResponseGetEventDetails(
    1, "Roma", "Porca Putténa! Nel mio ufficio non tollero uranisti te l'ho detto mille volte di non chiamarmi vecchio; abbiamo mangiato salame e prosciutto affrettato? M'avete preso per un coglione sono nichilista, nun me rompe li cojoni. I ricchioni si dividono in due grandi categorie è una famiglia schifosa; io vado a messa ogni domenica mattina? Marcolino un giorno ti dò una bottiglieta d'amaro qua a tracolla te la dò io beneficio benissimo così; che cagachezzo che è questo.",
    dateFormat.parse("23/11/2022") ?: Date(),
    dateFormat.parse("25/11/2022") ?: Date(),
    ItemType.EVENT,
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

            ItemType.ACTIVITY,
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
            ItemType.ACTIVITY,
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
            ItemType.ACTIVITY,
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
            ItemType.HOTEL,
            "https://onemag.it/wp-content/uploads/2018/04/Wynn-Palace-macao.jpg",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "albergo@asdf.it",
            "www.albergo.it",
            "32054191",
        ),
        EventItem(
            123124,
            "Albergo 2",
            "secondo albergo",
            ItemType.HOTEL,
            "https://www.visitcalifornia.com/sites/visitcalifornia.com/files/styles/welcome_image/public/VCW_SI_T9_NanPalmero_USGrant_1280x642.jpg",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "albergo@asdf.it",
            "www.albergo.it",
            "065246131",
        ),
        EventItem(
            123125,
            "Albergo 3",
            "terzo albergo",
            ItemType.HOTEL,
            "https://www.rollingstone.it/wp-content/uploads/2020/01/Atari-hotel-2.jpg",
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
            ItemType.RESTAURANT,
            "https://www.padovaoggi.it/~media/horizontal-hi/47016861713351/home-restaurant-2.jpg",
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
            ItemType.CONTACT,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "albergo@asdf.it",
            "www.albergo.it",
            "066598461",
        ),
        EventItem(
            123125,
            "Emergenza",
            "numero di emergenza",
            ItemType.CONTACT,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "",
            "056585695",
        ),
    ),
).log()