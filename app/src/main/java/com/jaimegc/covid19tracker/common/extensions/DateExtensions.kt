package com.jaimegc.covid19tracker.common.extensions

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMATTER = "yyyy-MM-dd"

fun String.dateToMilliseconds(): Long =
    SimpleDateFormatUTC(DATE_FORMATTER, Locale.US).parse(this)?.time ?: 0L

fun Long.millisecondsToDate(): String =
    SimpleDateFormatUTC(DATE_FORMATTER, Locale.US).format(this)

fun String.toLastUpdated(): String =
    this.replace("UTC", " UTC")

class SimpleDateFormatUTC(
    dateFormatter: String,
    locale: Locale
) : SimpleDateFormat(dateFormatter, locale) {
    init {
        timeZone = TimeZone.getTimeZone("UTC")
    }
}