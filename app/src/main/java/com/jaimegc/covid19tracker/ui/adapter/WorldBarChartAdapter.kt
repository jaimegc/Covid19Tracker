package com.jaimegc.covid19tracker.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemBarchartWorldTotalBinding
import com.jaimegc.covid19tracker.extensions.toPx
import com.jaimegc.covid19tracker.ui.model.WorldStatsChartUI

class WorldBarChartAdapter : ListAdapter<List<WorldStatsChartUI>, WorldBarChartAdapter.WorldTotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalViewHolder(ItemBarchartWorldTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldTotalViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldTotalViewHolder(
        private val binding: ItemBarchartWorldTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listWorldStatsChartUI: List<WorldStatsChartUI>) {
            val ctx = itemView.context
            val chartConfirmed = binding.chartConfirmed
            val chartRecovered = binding.chartRecovered
            val chartDeaths = binding.chartDeaths
            val chartOpenCases = binding.chartOpenCases
            val chartNewConfirmed = binding.chartNewConfirmed
            val chartNewRecovered = binding.chartNewRecovered
            val chartNewDeaths = binding.chartNewDeaths
            val chartNewOpenCases = binding.chartNewOpenCases

            configureBarChart(chartConfirmed, listWorldStatsChartUI,
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.confirmed }!!.stats.confirmed)
            configureBarChart(chartRecovered, listWorldStatsChartUI,
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.recovered }!!.stats.recovered)
            configureBarChart(chartDeaths, listWorldStatsChartUI,
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.deaths }!!.stats.deaths)
            configureBarChart(chartOpenCases, listWorldStatsChartUI,
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.openCases }!!.stats.openCases)
            configureBarChart(chartNewConfirmed, listWorldStatsChartUI,
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.newConfirmed }!!.stats.newConfirmed)
            configureBarChart(chartNewRecovered, listWorldStatsChartUI,
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.newRecovered }!!.stats.newRecovered)
            configureBarChart(chartNewDeaths, listWorldStatsChartUI,
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.newDeaths }!!.stats.newDeaths)
            configureBarChart(chartNewOpenCases, listWorldStatsChartUI,
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.newOpenCases }!!.stats.newOpenCases)

            setValuesChart(ctx, chartConfirmed, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.confirmed }, R.string.total_confirmed, R.color.dark_red)
            setValuesChart(ctx, chartDeaths, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.deaths }, R.string.total_deaths, R.color.dark_grey)
            setValuesChart(ctx, chartOpenCases, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.openCases }, R.string.total_open_cases, R.color.dark_blue)
            setValuesChart(ctx, chartRecovered, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.recovered }, R.string.total_recovered, R.color.dark_green)
            setValuesChart(ctx, chartNewConfirmed, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.newConfirmed }, R.string.total_new_confirmed, R.color.dark_red)
            setValuesChart(ctx, chartNewDeaths, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.newDeaths }, R.string.total_new_deaths, R.color.dark_grey)
            setValuesChart(ctx, chartNewOpenCases, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.newOpenCases }, R.string.total_new_open_cases, R.color.dark_blue)
            setValuesChart(ctx, chartNewRecovered, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.newRecovered }, R.string.total_new_recovered, R.color.dark_green)
        }

        private fun configureBarChart(
            chart: BarChart,
            listWorldStatsChartUI: List<WorldStatsChartUI>,
            minAxisLeftValue: Float
        ) {
            with(chart) {
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
                    valueFormatter = ChartDayMonthFormatter(
                        listWorldStatsChartUI.map { worldStats -> worldStats.date }
                    )
                    labelRotationAngle = -45f
                }

                with(chart.axisLeft) {
                    setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                    axisMinimum = if (minAxisLeftValue >= 0) 0f else minAxisLeftValue
                }

                with(chart.legend) {
                    form = LegendForm.SQUARE
                    textSize = 12f
                    verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                }
            }
        }

        private fun setValuesChart(
            ctx: Context,
            chart: BarChart,
            values: List<Float>,
            legendStringRes: Int,
            legendColorRes: Int
        ) {
            val valuesChartConfirmed = mutableListOf<BarEntry>()

            values.mapIndexed { index, value ->
                valuesChartConfirmed.add(BarEntry(index.toFloat(), value))
            }

            val dataSetsConfirmed = BarDataSet(valuesChartConfirmed, ctx.getString(legendStringRes))
            dataSetsConfirmed.setDrawValues(false)
            dataSetsConfirmed.color = ContextCompat.getColor(ctx, legendColorRes)

            val dataConfirmed = BarData(listOf<IBarDataSet>(dataSetsConfirmed))
            dataConfirmed.setValueTextSize(10f.toPx())
            chart.data = dataConfirmed
        }
    }

    class ChartDayMonthFormatter(
        private val listDates: List<String>
    ) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return listDates[value.toInt()].substring(5, listDates[value.toInt()].length)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<List<WorldStatsChartUI>>() {
            override fun areItemsTheSame(oldItem: List<WorldStatsChartUI>, newItem: List<WorldStatsChartUI>): Boolean =
                oldItem.size == newItem.size

            override fun areContentsTheSame(oldItem: List<WorldStatsChartUI>, newItem: List<WorldStatsChartUI>): Boolean =
                oldItem == newItem
        }
    }
}