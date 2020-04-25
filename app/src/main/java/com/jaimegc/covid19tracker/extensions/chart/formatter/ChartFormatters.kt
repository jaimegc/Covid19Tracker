package com.jaimegc.covid19tracker.extensions.chart.formatter

import com.github.mikephil.charting.formatter.ValueFormatter

class ChartDayMonthFormatter(
    private val listDates: List<String>
) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return listDates[value.toInt()].substring(5, listDates[value.toInt()].length)
    }
}