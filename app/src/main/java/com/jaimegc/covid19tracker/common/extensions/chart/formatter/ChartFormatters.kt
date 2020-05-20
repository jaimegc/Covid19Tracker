package com.jaimegc.covid19tracker.common.extensions.chart.formatter

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jaimegc.covid19tracker.common.extensions.formatDecimals

class ChartDayMonthFormatter(
    private val listDates: List<String>
) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String =
        try {
            listDates[value.toInt()].substring(5, listDates[value.toInt()].length)
        } catch(e: Exception) {
            ""
        }
}

class PercentPieChartFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String =
        "${value.toDouble().formatDecimals()}%"

    override fun getPieLabel(value: Float, pieEntry: PieEntry): String =
        getFormattedValue(value)
}