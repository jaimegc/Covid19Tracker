package com.jaimegc.covid19tracker.common.extensions.chart

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.jaimegc.covid19tracker.common.extensions.chart.formatter.PercentPieChartFormatter

fun PieChart.configure() {
    with(this) {
        setDrawEntryLabels(false)
        setUsePercentValues(true)
        description.isEnabled = false
        isHighlightPerTapEnabled = false
        isDrawHoleEnabled = true

        with(legend) {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            textSize = 12f
        }
    }
}

fun PieChart.setValues(
    ctx: Context,
    values: List<Float>,
    legendStringsRes: List<Int>,
    legendColorsRes: List<Int>
) {
    val valuesPieChart = mutableListOf<PieEntry>()

    values.mapIndexed { index, value ->
        valuesPieChart.add(PieEntry(value, ctx.getString(legendStringsRes[index])))
    }

    val pieDataSet = PieDataSet(valuesPieChart, "")
    pieDataSet.setDrawValues(false)
    pieDataSet.sliceSpace = 1f
    pieDataSet.colors = legendColorsRes.map { color -> ContextCompat.getColor(ctx, color) }

    val pieData = PieData(pieDataSet)
    pieData.setValueFormatter(PercentPieChartFormatter())
    pieData.setValueTextSize(13f)
    pieData.setValueTextColor(Color.WHITE)
    pieData.setDrawValues(true)
    pieData.isHighlightEnabled = false

    data = pieData
}