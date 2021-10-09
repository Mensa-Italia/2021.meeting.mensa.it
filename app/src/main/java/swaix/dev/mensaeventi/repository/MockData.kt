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

// fun mockEmpty() = ResponseGetEvents(listOf())



//fun mockNoEventItemWithDate() = ResponseGetEventDetails(
//    1, "Roma", "",
//    dateFormat.parse("23/11/2022") ?: Date(),
//    dateFormat.parse("25/11/2022") ?: Date(),
//    ItemType.EVENT,
//    "https://lh3.googleusercontent.com/proxy/NlEMIJcoTJjFSzTI6qWThU_ok-wFibpowhD-gGcL3DPcCKLALL_KK47lNAdMQ6OJxwBQzx_r3fCKZw7yAT98CYBJZn2y3CgwDH7WC8fYeVNbdVpWgMJSe3WykQW0szozjRE",
//    Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
//    "",
//    "",
//    "",
//    listOf(), listOf(), listOf(), listOf()).log()



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
            "https://media.leroymerlin.it/media/45993/.jpg",
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
            "Porca <b>Putténa</b>! Io vado a messa ogni domenica mattina tutti i danni li pago io neh: prezzemolo e finocchio. è scappata da Canosa: ti spezzo la noce del capocollo come dicono i francesi a frappé. Ti protegge la Madonna dello Sterpeto io ti odio a te è un fatto di pelle; non c'hanno sesso sono ricchione. Vogliono un po' di utopia un goccio abbiamo mangiato salame e prosciutto affrettato, ho afferreto benissimo.",
            dateTimeFormat.parse("23/11/2022 06:21") ?: Date(),
            dateTimeFormat.parse("23/11/2022 9:00") ?: Date(),

            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "www.linnk1.it",
            "320254214",
        ),
        EventItemWithDate(
            1,
            "Attività 1",
            "Porca <b>Putténa</b>! Io vado a messa ogni domenica mattina tutti i danni li pago io neh: prezzemolo e finocchio. è scappata da Canosa: ti spezzo la noce del capocollo come dicono i francesi a frappé. Ti protegge la Madonna dello Sterpeto io ti odio a te è un fatto di pelle; non c'hanno sesso sono ricchione. Vogliono un po' di utopia un goccio abbiamo mangiato salame e prosciutto affrettato, ho afferreto benissimo.",
            dateTimeFormat.parse("23/11/2022 9:00") ?: Date(),
            dateTimeFormat.parse("23/11/2022 12:50") ?: Date(),

            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "",
            "www.linnk1.it",
            "320254214",
        ),
        EventItemWithDate(
            3,
            "Attività 1",
            "<p>Porca Putténa! Io ti odio a te è un fatto di pelle terrone maledetto; un caffè con utopia? Canà indovini chi le ho preso dovrei avere il premio per il self control dopo diciott'anni che ti sopporto; come dicono i francesi a frappé. </p>\n",
            dateTimeFormat.parse("24/11/2022 5:50") ?: Date(),
            dateTimeFormat.parse("24/11/2022 11:00") ?: Date(),
            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "asdf@asdfi.re",
            "www.link2.it",
            "",
        ),
        EventItemWithDate(
            3,
            "Attività 2",
            "<p>Porca Putténa! Io ti odio a te è un fatto di pelle terrone maledetto; un caffè con utopia? Canà indovini chi le ho preso dovrei avere il premio per il self control dopo diciott'anni che ti sopporto; come dicono i francesi a frappé. </p>\n",
            dateTimeFormat.parse("24/11/2022 12:00") ?: Date(),
            dateTimeFormat.parse("24/11/2022 22:00") ?: Date(),
            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "asdf@asdfi.re",
            "www.link2.it",
            "",
        ),
        EventItemWithDate(
            3,
            "Attività 3",
            "<p>Porca Putténa! Io ti odio a te è un fatto di pelle terrone maledetto; un caffè con utopia? Canà indovini chi le ho preso dovrei avere il premio per il self control dopo diciott'anni che ti sopporto; come dicono i francesi a frappé. </p>\n",
            dateTimeFormat.parse("24/11/2022 7:00") ?: Date(),
            dateTimeFormat.parse("24/11/2022 17:00") ?: Date(),
            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "asdf@asdfi.re",
            "www.link2.it",
            "",
        ),
        EventItemWithDate(
            3,
            "Attività 4",
            "<p>Porca Putténa! Io ti odio a te è un fatto di pelle terrone maledetto; un caffè con utopia? Canà indovini chi le ho preso dovrei avere il premio per il self control dopo diciott'anni che ti sopporto; come dicono i francesi a frappé. </p>\n",
            dateTimeFormat.parse("24/11/2022 16:00") ?: Date(),
            dateTimeFormat.parse("24/11/2022 22:00") ?: Date(),
            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "asdf@asdfi.re",
            "www.link2.it",
            "",
        ),
        EventItemWithDate(
            3,
            "Attività 5",
            "<p>Porca Putténa! Io ti odio a te è un fatto di pelle terrone maledetto; un caffè con utopia? Canà indovini chi le ho preso dovrei avere il premio per il self control dopo diciott'anni che ti sopporto; come dicono i francesi a frappé. </p>\n",
            dateTimeFormat.parse("24/11/2022 18:00") ?: Date(),
            dateTimeFormat.parse("24/11/2022 23:00") ?: Date(),
            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "asdf@asdfi.re",
            "www.link2.it",
            "",
        ),
        EventItemWithDate(
            2,
            "Attività 2",
            "<p>Porca Putténa! Certo che tu metti un'allegria,  io ti odio a te è un fatto di pelle è una guerra psicologica la nostra. Cambia Cavallo che l'erba cresce cerca di non gufare continuamente; la sua soddisfazione è il nostro miglior premio. Che chezzo ne so una torta di mare perché mettono le vongole vereci sopra ; ti spezzo i menischi! Non c'hanno sesso sono ricchione spogliati o ti spoglio io da morto: muto come un pesce che si è operato alle corde vocheli. </p>\n",
            dateTimeFormat.parse("25/11/2022 06:16") ?: Date(),
            dateTimeFormat.parse("25/11/2022 12:00") ?: Date(),
            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "asdf@asdfi.re",
            "www.link2.it",
            "213412341",
        ),
        EventItemWithDate(
            4,
            "<i>Attività</i> 5",
            "<p>Porca Putténa! Lei è un cornuto lo sa:  sono una persona che gira i mari di tutto il mondo tutta la iella buttala a mare! Occhio e malocchio tu non sei né figlio d'emigrante né figlio di preta pura, lei è un cornuto lo sa. Ti spezzo la noce del capocollo:  Filomegna donde estas lei è un cornuto lo sa. In senso epidermico;  nel mio ufficio non tollero uranisti sono volatili per diabetici. </p>",
            dateTimeFormat.parse("25/11/2022 14:00") ?: Date(),
            dateTimeFormat.parse("25/11/2022 19:00") ?: Date(),
            ItemType.ACTIVITY,
            "",
            Position("via vedremo", "bla bla bla", 41.9109, 12.4818),
            "asdf@asdfi.re",
            "www.link2.it",
            "213412341",
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