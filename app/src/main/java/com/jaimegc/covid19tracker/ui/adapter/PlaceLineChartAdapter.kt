package com.jaimegc.covid19tracker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.chart.configure
import com.jaimegc.covid19tracker.common.extensions.chart.setValues
import com.jaimegc.covid19tracker.common.extensions.hide
import com.jaimegc.covid19tracker.databinding.ItemLineChartTotalBinding
import com.jaimegc.covid19tracker.ui.model.PlaceListStatsChartUI
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType

class PlaceLineChartAdapter :
    ListAdapter<Map<MenuItemViewType, List<PlaceListStatsChartUI>>,
    PlaceLineChartAdapter.PlaceLineChartViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceLineChartViewHolder(ItemLineChartTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PlaceLineChartViewHolder, position: Int) =
        holder.bind(getItem(position))

    class PlaceLineChartViewHolder(
        private val binding: ItemLineChartTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mapPlacesStatsChartUI: Map<MenuItemViewType, List<PlaceListStatsChartUI>>) {
            val ctx = itemView.context

            mapPlacesStatsChartUI.keys.map { type ->
                when (type) {
                    is MenuItemViewType.LineChartMostConfirmed ->
                        configureLineChart(
                            ctx, binding.chartConfirmed, mapPlacesStatsChartUI.getValue(type), type).also {
                            binding.loadingConfirmed.hide()
                        }
                    is MenuItemViewType.LineChartMostDeaths ->
                        configureLineChart(
                            ctx, binding.chartDeaths, mapPlacesStatsChartUI.getValue(type), type).also {
                            binding.loadingDeaths.hide()
                        }
                    is MenuItemViewType.LineChartMostRecovered ->
                        configureLineChart(
                            ctx, binding.chartRecovered, mapPlacesStatsChartUI.getValue(type), type).also {
                            binding.loadingRecovered.hide()
                        }
                    is MenuItemViewType.LineChartMostOpenCases ->
                        configureLineChart(
                            ctx, binding.chartOpenCases, mapPlacesStatsChartUI.getValue(type), type).also {
                            binding.loadingOpenCases.hide()
                        }
                }
            }

            clearNoDataText()
        }

        private fun clearNoDataText() {
            binding.chartConfirmed.setNoDataText("")
            binding.chartDeaths.setNoDataText("")
            binding.chartRecovered.setNoDataText("")
            binding.chartOpenCases.setNoDataText("")
        }

        private fun configureLineChart(
            ctx: Context,
            chart: LineChart,
            listPlacesStatsChartUI: List<PlaceListStatsChartUI>,
            viewType: MenuItemViewType,
            minAxisLeftValue: Float = 0f) {

            val placeStatsMaxDays = listPlacesStatsChartUI.maxBy { it.stats.size }
            chart.configure(
                placeStatsMaxDays!!.stats.sortedBy { it.date }.map { it.date }, minAxisLeftValue
            )

            val placeStatsValues = mutableListOf<List<Float>>()

            listPlacesStatsChartUI.map { placeStats ->
                val listPlaceStats = mutableListOf<Float>()
                placeStats.stats.map { stats ->
                    when (viewType) {
                        is MenuItemViewType.LineChartMostConfirmed ->
                            listPlaceStats.add(stats.confirmed)
                        is MenuItemViewType.LineChartMostDeaths ->
                            listPlaceStats.add(stats.deaths)
                        is MenuItemViewType.LineChartMostRecovered ->
                            listPlaceStats.add(stats.recovered)
                        is MenuItemViewType.LineChartMostOpenCases ->
                            listPlaceStats.add(stats.openCases)
                        else -> Unit
                    }
                }
                placeStatsValues.add(listPlaceStats)
            }

            chart.setValues(ctx, placeStatsValues, listOf(
                R.color.dark_purple, R.color.dark_blue, R.color.dark_green, R.color.dark_orange, R.color.dark_red),
                listPlacesStatsChartUI.map { it.place.name }, placeStatsMaxDays.stats.size, chart.legend)
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Map<MenuItemViewType, List<PlaceListStatsChartUI>>>() {
                override fun areItemsTheSame(
                    oldItem: Map<MenuItemViewType, List<PlaceListStatsChartUI>>,
                    newItem: Map<MenuItemViewType, List<PlaceListStatsChartUI>>
                ): Boolean =
                    oldItem.size == newItem.size

                override fun areContentsTheSame(
                    oldItem: Map<MenuItemViewType, List<PlaceListStatsChartUI>>,
                    newItem: Map<MenuItemViewType, List<PlaceListStatsChartUI>>
                ): Boolean =
                    oldItem == newItem
        }
    }
}