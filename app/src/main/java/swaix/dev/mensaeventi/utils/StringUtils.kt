package swaix.dev.mensaeventi.utils

import android.content.Context
import swaix.dev.mensaeventi.R
import java.text.SimpleDateFormat
import java.util.*

fun Date.yearString(): String {
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    return yearFormat.format(this)
}

fun Context.formatDateRange(dateFrom: Date, dateTo: Date): String {
    val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dayMonthYearFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val dayMonthFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
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