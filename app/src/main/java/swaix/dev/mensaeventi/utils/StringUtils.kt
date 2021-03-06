package swaix.dev.mensaeventi.utils

import android.content.Context
import android.text.Html
import android.widget.TextView
import androidx.core.text.HtmlCompat
import swaix.dev.mensaeventi.R
import java.text.SimpleDateFormat
import java.util.*

fun Date.yearString(): String {
    val yearFormat = SimpleDateFormat("yyyy", Locale.ITALIAN)
    return yearFormat.format(this)
}

fun Date.hourMinuteString(): String {
    val yearFormat = SimpleDateFormat("HH:mm", Locale.ITALIAN)
    return yearFormat.format(this)
}
fun Date.hour(): Int {
    val yearFormat = SimpleDateFormat("HH", Locale.ITALIAN)
    return yearFormat.format(this).toInt()
}
fun Date.minutes(): Int {
    val yearFormat = SimpleDateFormat("mm", Locale.ITALIAN)
    return yearFormat.format(this).toInt()
}
fun Date.dateString(): String {
    val yearFormat = SimpleDateFormat("dd MMMM yyyy HH:ss", Locale.ITALIAN)
    return yearFormat.format(this)
}
fun Date.shortDayString(): String {
    val yearFormat = SimpleDateFormat("EEE dd/MM", Locale.ITALIAN)
    return yearFormat.format(this)
}
fun Date.dayString(): String {
    val yearFormat = SimpleDateFormat("EEEE dd", Locale.ITALIAN)
    return yearFormat.format(this)
}

fun String.asHtml()= HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)


fun Context.formatDateRange(dateFrom: Date, dateTo: Date): String {
    val dayFormat = SimpleDateFormat("dd", Locale.ITALIAN)
    val monthFormat = SimpleDateFormat("MMMM", Locale.ITALIAN)
    val yearFormat = SimpleDateFormat("yyyy", Locale.ITALIAN)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN)
    val dayMonthYearFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ITALIAN)
    val dayMonthFormat = SimpleDateFormat("dd MMMM", Locale.ITALIAN)
    val monthFrom = monthFormat.format(dateFrom)
    val monthTo = monthFormat.format(dateTo)
    val yearFrom = yearFormat.format(dateFrom)
    val yearTo = yearFormat.format(dateTo)
    val message =
        if (yearFrom == yearTo && monthFrom == monthTo) {
            val from = dayFormat.format(dateFrom)
            val to = dayMonthYearFormat.format(dateTo)
            getString(R.string.date_label, from, to)
        } else if (yearFrom == yearTo && monthFrom != monthTo) {
            val from = dayMonthFormat.format(dateFrom)
            val to = dayMonthYearFormat.format(dateTo)
            getString(R.string.date_label, from, to)
        } else if (yearFrom != yearTo) {
            val from = dateFormat.format(dateFrom)
            val to = dateFormat.format(dateTo)
            getString(R.string.date_label, from, to)
        } else
            "ERRORE"
    return message
}