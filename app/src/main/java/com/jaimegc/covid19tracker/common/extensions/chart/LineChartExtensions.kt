package com.jaimegc.covid19tracker.common.extensions.chart

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jaimegc.covid19tracker.common.extensions.chart.formatter.ChartDayMonthFormatter

fun LineChart.configure(xAxisValues: List<String>, minAxisLeftValue: Float = 0f) {
    with(this) {
        description.isEnabled = false
        axisRight.isEnabled = false

        with(xAxis) {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
            textSize = 10f
            typeface = Typeface.DEFAULT_BOLD
            valueFormatter = ChartDayMonthFormatter(xAxisValues)
            labelRotationAngle = -45f
        }

        with(this.axisLeft) {
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            axisMinimum = minAxisLeftValue
        }

        with(this.legend) {
            form = Legend.LegendForm.SQUARE
            textSize = 10f
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        }
    }
}

fun LineChart.setValues(
    ctx: Context,
    listValues: List<List<Float>>,
    legendColorRes: List<Int>,
    legendValues: List<String>,
    sizeMaxListValues: Int,
    legend: Legend
) {
    val valuesLineChart = mutableListOf<List<Entry>>()
    optimizeLegendTextSize(legendValues, legend)

    listValues.map { values ->
        val valuesDiff = sizeMaxListValues - values.size
        val listEntries = mutableListOf<Entry>()
        if (valuesDiff != 0) {
            for (i in 0..valuesDiff) listEntries.add(Entry(i.toFloat(), 0f))
        }

        values.mapIndexed { index, value -> listEntries.add(Entry(index.toFloat() + valuesDiff, value)) }

        valuesLineChart.add(listEntries)
    }

    val listLinesDataSet = mutableListOf<LineDataSet>()

    valuesLineChart.mapIndexed { index, value ->
        val lineDataSet = LineDataSet(value, legendValues[index])
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 2f
        lineDataSet.color = ContextCompat.getColor(ctx, legendColorRes[index])
        listLinesDataSet.add(lineDataSet)
    }

    val lineData = LineData(listLinesDataSet.toList())
    lineData.setValueTextColor(Color.WHITE)
    lineData.setValueTextSize(12f)
    lineData.isHighlightEnabled = false

    data = lineData
}

private fun optimizeLegendTextSize(legendValues: List<String>, legend: Legend) {
    when (legendValues.sumBy { it.length }) {
        in 0..40 -> legend.textSize = 10f
        in 41..45 -> legend.textSize = 9f
        in 46..50 -> legend.textSize = 8f
        in 51..60 -> legend.textSize = 7.5f
        in 61..70 -> legend.textSize = 7f
        in 70..80 -> legend.textSize = 6f
        else -> legend.textSize = 5f
    }
}