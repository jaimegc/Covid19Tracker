package com.jaimegc.covid19tracker.extensions.chart.formatter

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jaimegc.covid19tracker.extensions.formatDecimals

class ChartDayMonthFormatter(
    private val listDates: List<String>
) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String =
        listDates[value.toInt()].substring(5, listDates[value.toInt()].length)
}

class PercentPieChartFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String =
        "${value.toDouble().formatDecimals()}%"

    override fun getPieLabel(value: Float, pieEntry: PieEntry): String =
        getFormattedValue(value)
}