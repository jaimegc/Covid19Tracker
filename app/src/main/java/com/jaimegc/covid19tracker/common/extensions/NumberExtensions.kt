package com.jaimegc.covid19tracker.common.extensions

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun Double.formatDecimals(): String = numberFormatDecimals(this).format(this)

fun Long.formatValue(): String = numberFormat().format(this)

fun Float.formatValue(): String = numberFormat().format(this)

fun Double.percentage(value: Double): String =
    "${(this / value * 100).formatDecimals()}%"

fun Float.percentage(value: Float): String =
    "${(this / value * 100).toDouble().formatDecimals()}%"

private fun numberFormatDecimals(value: Double, totalDecimals: Int = 2): NumberFormat {
    val locale = Locale.getDefault()
    val result = NumberFormat.getInstance(locale)
    result.minimumFractionDigits = totalDecimals
    result.maximumFractionDigits = totalDecimals

    result.roundingMode = if (value >= 1.0) RoundingMode.HALF_UP else RoundingMode.CEILING

    if (result is DecimalFormat && totalDecimals > 0) result.isDecimalSeparatorAlwaysShown = true

    return result
}

private fun numberFormat(): NumberFormat =
    NumberFormat.getInstance(Locale.getDefault())