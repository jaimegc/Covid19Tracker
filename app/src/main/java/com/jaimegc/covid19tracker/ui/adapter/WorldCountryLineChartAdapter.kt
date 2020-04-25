package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemLineChartCountryTotalBinding
import com.jaimegc.covid19tracker.extensions.chart.configure
import com.jaimegc.covid19tracker.extensions.chart.setValuesChart
import com.jaimegc.covid19tracker.ui.model.CountryListStatsChartUI

class WorldCountryLineChartAdapter : ListAdapter<List<CountryListStatsChartUI>, WorldCountryLineChartAdapter.WorldTotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalViewHolder(ItemLineChartCountryTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldTotalViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldTotalViewHolder(
        private val binding: ItemLineChartCountryTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listCountriesStatsChartUI: List<CountryListStatsChartUI>) {
            val ctx = itemView.context
            val chartConfirmed = binding.chartConfirmed

            val countryStatsMaxDays = listCountriesStatsChartUI.maxBy { it.stats.size }
            chartConfirmed.configure(countryStatsMaxDays!!.stats.map { it.date })

            val countryStatsValues = mutableListOf<List<Float>>()

            listCountriesStatsChartUI.map { countryStats ->
                val listCountryStats = mutableListOf<Float>()
                countryStats.stats.map { stats -> listCountryStats.add(stats.confirmed) }
                countryStatsValues.add(listCountryStats)
            }

            chartConfirmed.setValuesChart(ctx, countryStatsValues, listOf(
                R.color.dark_purple, R.color.dark_blue, R.color.dark_green, R.color.dark_orange, R.color.dark_red),
                listCountriesStatsChartUI.map { it.name }, countryStatsMaxDays.stats.size)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<List<CountryListStatsChartUI>>() {
            override fun areItemsTheSame(oldItem: List<CountryListStatsChartUI>, newItem: List<CountryListStatsChartUI>): Boolean =
                oldItem.size == newItem.size

            override fun areContentsTheSame(oldItem: List<CountryListStatsChartUI>, newItem: List<CountryListStatsChartUI>): Boolean =
                oldItem == newItem
        }
    }
}