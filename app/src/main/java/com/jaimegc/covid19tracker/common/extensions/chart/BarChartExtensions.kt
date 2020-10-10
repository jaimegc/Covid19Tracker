package com.jaimegc.covid19tracker.common.extensions.chart

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.jaimegc.covid19tracker.common.extensions.chart.formatter.ChartDayMonthFormatter

fun BarChart.configure(xAxisValues: List<String>, minAxisLeftValue: Float?) {
    val minAxisLeftNumber = minAxisLeftValue ?: 0f

    with(this) {
        setDrawBarShadow(false)
        setDrawValueAboveBar(false)
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

        with(axisLeft) {
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            axisMinimum = if (minAxisLeftNumber >= 0) 0f else minAxisLeftNumber
        }

        with(legend) {
            form = Legend.LegendForm.SQUARE
            textSize = 12f
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        }
    }
}

fun BarChart.setValues(ctx: Context, values: List<Float>, legendStringRes: Int, legendColorRes: Int) {
    val valuesBarChart = mutableListOf<BarEntry>()

    values.mapIndexed { index, value -> valuesBarChart.add(BarEntry(index.toFloat(), value)) }

    val barDataSet = BarDataSet(valuesBarChart, ctx.getString(legendStringRes))
    barDataSet.setDrawValues(false)
    barDataSet.color = ContextCompat.getColor(ctx, legendColorRes)

    val barData = BarData(listOf<IBarDataSet>(barDataSet))
    barData.setValueTextSize(12f)
    barData.isHighlightEnabled = false

    data = barData
}