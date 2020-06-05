package com.jaimegc.covid19tracker.common.extensions

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMATTER = "yyyy-MM-dd"

fun String.dateToMilliseconds(): Long =
    SimpleDateFormat(DATE_FORMATTER, Locale.US).parse(this).time

fun Long.millisecondsToDate(): String =
    SimpleDateFormat(DATE_FORMATTER, Locale.US).format(this)