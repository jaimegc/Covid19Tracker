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
import com.jaimegc.covid19tracker.databinding.ItemLineChartTotalBinding
import com.jaimegc.covid19tracker.ui.model.PlaceListStatsChartUI
import com.jaimegc.covid19tracker.ui.states.MenuItemViewType

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
                            ctx, binding.chartConfirmed, mapPlacesStatsChartUI.getValue(type), type)
                    is MenuItemViewType.LineChartMostDeaths ->
                        configureLineChart(
                            ctx, binding.chartDeaths, mapPlacesStatsChartUI.getValue(type), type)
                    is MenuItemViewType.LineChartMostRecovered ->
                        configureLineChart(
                            ctx, binding.chartRecovered, mapPlacesStatsChartUI.getValue(type), type)
                    is MenuItemViewType.LineChartMostOpenCases ->
                        configureLineChart(
                            ctx, binding.chartOpenCases, mapPlacesStatsChartUI.getValue(type), type)
                }
            }
        }

        private fun configureLineChart(
            ctx: Context,
            chart: LineChart,
            listCountriesStatsChartUI: List<PlaceListStatsChartUI>,
            viewType: MenuItemViewType,
            minAxisLeftValue: Float = 0f) {

            val countryStatsMaxDays = listCountriesStatsChartUI.maxBy { it.stats.size }
            chart.configure(
                countryStatsMaxDays!!.stats.sortedBy { it.date }.map { it.date }, minAxisLeftValue
            )

            val countryStatsValues = mutableListOf<List<Float>>()

            listCountriesStatsChartUI.map { countryStats ->
                val listCountryStats = mutableListOf<Float>()
                countryStats.stats.map { stats ->
                    when (viewType) {
                        is MenuItemViewType.LineChartMostConfirmed ->
                            listCountryStats.add(stats.confirmed)
                        is MenuItemViewType.LineChartMostDeaths ->
                            listCountryStats.add(stats.deaths)
                        is MenuItemViewType.LineChartMostRecovered ->
                            listCountryStats.add(stats.recovered)
                        is MenuItemViewType.LineChartMostOpenCases ->
                            listCountryStats.add(stats.openCases)
                        else -> Unit
                    }
                }
                countryStatsValues.add(listCountryStats)
            }

            chart.setValues(ctx, countryStatsValues, listOf(
                R.color.dark_purple, R.color.dark_blue, R.color.dark_green, R.color.dark_orange, R.color.dark_red),
                listCountriesStatsChartUI.map { it.place.name }, countryStatsMaxDays.stats.size, chart.legend)
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