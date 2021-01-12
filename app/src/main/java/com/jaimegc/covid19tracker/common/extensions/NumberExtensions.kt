package com.jaimegc.covid19tracker.common.extensions

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ln

private const val PERCENTAGE = 100.0
private const val MILLION = 1000000.0

fun Double.formatDecimals(): String = numberFormatDecimals(this).format(this)

fun Long.formatValue(): String = numberFormat().format(this)

fun Float.formatValue(): String = numberFormat().format(this)

fun Long.formatCompactValue(): String = compactValue(this)

fun Double.percentage(): String = (this * PERCENTAGE).formatDecimals()

fun Float.percentageSymbol(value: Float): String =
    "${(this / value * PERCENTAGE).toDouble().formatDecimals()}%"

private fun numberFormatDecimals(value: Double, totalDecimals: Int = 2): NumberFormat {
    val locale = Locale.getDefault()
    val result = NumberFormat.getInstance(locale)
    result.minimumFractionDigits = totalDecimals
    result.maximumFractionDigits = totalDecimals

    result.roundingMode = if (value >= 1) RoundingMode.HALF_UP else RoundingMode.CEILING

    if (result is DecimalFormat && totalDecimals > 0) result.isDecimalSeparatorAlwaysShown = true

    return result
}

private fun compactValue(value: Long): String {
    if (value < MILLION) return numberFormat().format(value)
    val exp = (ln(value.toDouble()) / ln(MILLION)).toInt()
    val compactValue = value.toDouble() / MILLION
    val format = numberFormatDecimals(value = compactValue, totalDecimals = 2).format(compactValue)

    return "$format${"MGTPE"[exp - 1]}"
}

private fun numberFormat(): NumberFormat = NumberFormat.getInstance(Locale.getDefault())