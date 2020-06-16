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
import com.jaimegc.covid19tracker.common.extensions.formatValue
import com.jaimegc.covid19tracker.common.extensions.hide
import com.jaimegc.covid19tracker.databinding.ItemLineChartTotalBinding
import com.jaimegc.covid19tracker.ui.model.CountryListStatsChartUI
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType

class WorldLineChartAdapter :
    ListAdapter<Map<MenuItemViewType, List<CountryListStatsChartUI>>,
    WorldLineChartAdapter.WorldLineChartViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldLineChartViewHolder(ItemLineChartTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldLineChartViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldLineChartViewHolder(
        private val binding: ItemLineChartTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mapCountriesStatsChartUI: Map<MenuItemViewType, List<CountryListStatsChartUI>>) {
            val ctx = itemView.context
            val minAxisOther = 2000f
            val minAxisDeaths = 100f

            binding.moreMostConfirmed.text =
                itemView.context.getString(R.string.countries_with_more_confirmed, minAxisOther.formatValue())
            binding.moreMostDeaths.text =
                itemView.context.getString(R.string.countries_with_more_deaths, minAxisDeaths.formatValue())
            binding.moreMostRecovered.text =
                itemView.context.getString(R.string.countries_with_more_recovered, minAxisOther.formatValue())
            binding.moreMostOpenCases.text =
                itemView.context.getString(R.string.countries_with_more_open_cases, minAxisOther.formatValue())

            mapCountriesStatsChartUI.keys.map { type ->
                when (type) {
                    is MenuItemViewType.LineChartMostConfirmed ->
                        configureLineChart(
                            ctx, binding.chartConfirmed,
                            mapCountriesStatsChartUI.getValue(type), type, minAxisOther).also {
                                binding.loadingConfirmed.hide()
                            }
                    is MenuItemViewType.LineChartMostDeaths ->
                        configureLineChart(
                            ctx, binding.chartDeaths,
                            mapCountriesStatsChartUI.getValue(type), type, minAxisDeaths).also {
                                binding.loadingDeaths.hide()
                            }
                    is MenuItemViewType.LineChartMostRecovered ->
                        configureLineChart(
                            ctx, binding.chartRecovered,
                            mapCountriesStatsChartUI.getValue(type), type, minAxisOther).also {
                                binding.loadingRecovered.hide()
                            }
                    is MenuItemViewType.LineChartMostOpenCases ->
                        configureLineChart(
                            ctx, binding.chartOpenCases,
                            mapCountriesStatsChartUI.getValue(type), type, minAxisOther).also {
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
            listCountriesStatsChartUI: List<CountryListStatsChartUI>,
            viewType: MenuItemViewType,
            minAxisLeftValue: Float
        ) {

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
                listCountriesStatsChartUI.map { it.country.name }, countryStatsMaxDays.stats.size, chart.legend)
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Map<MenuItemViewType, List<CountryListStatsChartUI>>>() {
                override fun areItemsTheSame(
                    oldItem: Map<MenuItemViewType, List<CountryListStatsChartUI>>,
                    newItem: Map<MenuItemViewType, List<CountryListStatsChartUI>>
                ): Boolean =
                    oldItem.size == newItem.size

                override fun areContentsTheSame(
                    oldItem: Map<MenuItemViewType, List<CountryListStatsChartUI>>,
                    newItem: Map<MenuItemViewType, List<CountryListStatsChartUI>>
                ): Boolean =
                    oldItem == newItem
        }
    }
}