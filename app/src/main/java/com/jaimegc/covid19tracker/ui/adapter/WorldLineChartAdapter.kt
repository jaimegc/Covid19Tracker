package com.jaimegc.covid19tracker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemLineChartWorldTotalBinding
import com.jaimegc.covid19tracker.extensions.chart.configure
import com.jaimegc.covid19tracker.extensions.chart.setValues
import com.jaimegc.covid19tracker.ui.model.CountryListStatsChartUI
import com.jaimegc.covid19tracker.ui.states.WorldStateCountriesStatsLineChartType

class WorldLineChartAdapter :
    ListAdapter<Map<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>,
    WorldLineChartAdapter.WorldTotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalViewHolder(ItemLineChartWorldTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldTotalViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldTotalViewHolder(
        private val binding: ItemLineChartWorldTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mapCountriesStatsChartUI: Map<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>) {
            val ctx = itemView.context

            mapCountriesStatsChartUI.keys.map { type ->
                when (type) {
                    is WorldStateCountriesStatsLineChartType.MostConfirmed ->
                        configureLineChart(
                            ctx, binding.chartConfirmed, mapCountriesStatsChartUI.getValue(type), type, 2000f)
                    is WorldStateCountriesStatsLineChartType.MostDeaths ->
                        configureLineChart(
                            ctx, binding.chartDeaths, mapCountriesStatsChartUI.getValue(type), type, 100f)
                }
            }
        }

        private fun configureLineChart(
            ctx: Context,
            chart: LineChart,
            listCountriesStatsChartUI: List<CountryListStatsChartUI>,
            type: WorldStateCountriesStatsLineChartType,
            minAxisLeftValue: Float) {

            val countryStatsMaxDays = listCountriesStatsChartUI.maxBy { it.stats.size }
            chart.configure(countryStatsMaxDays!!.stats.map { it.date }, minAxisLeftValue)

            val countryStatsValues = mutableListOf<List<Float>>()

            listCountriesStatsChartUI.map { countryStats ->
                val listCountryStats = mutableListOf<Float>()
                countryStats.stats.map { stats ->
                    when (type) {
                        is WorldStateCountriesStatsLineChartType.MostConfirmed ->
                            listCountryStats.add(stats.confirmed)
                        is WorldStateCountriesStatsLineChartType.MostDeaths ->
                            listCountryStats.add(stats.deaths)
                    }
                }
                countryStatsValues.add(listCountryStats)
            }

            chart.setValues(ctx, countryStatsValues, listOf(
                R.color.dark_purple, R.color.dark_blue, R.color.dark_green, R.color.dark_orange, R.color.dark_red),
                listCountriesStatsChartUI.map { it.name }, countryStatsMaxDays.stats.size)
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Map<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>>() {
                override fun areItemsTheSame(
                    oldItem: Map<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>,
                    newItem: Map<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>
                ): Boolean =
                    oldItem.size == newItem.size

                override fun areContentsTheSame(
                    oldItem: Map<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>,
                    newItem: Map<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>
                ): Boolean =
                    oldItem == newItem
        }
    }
}