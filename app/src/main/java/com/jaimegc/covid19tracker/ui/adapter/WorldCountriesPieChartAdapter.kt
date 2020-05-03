package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemPieChartCountryTotalBinding
import com.jaimegc.covid19tracker.extensions.chart.configure
import com.jaimegc.covid19tracker.extensions.chart.setValues
import com.jaimegc.covid19tracker.extensions.percentage
import com.jaimegc.covid19tracker.extensions.setEmojiCountry
import com.jaimegc.covid19tracker.ui.model.WorldCountryStatsUI

class WorldCountriesPieChartAdapter : ListAdapter<WorldCountryStatsUI, WorldCountriesPieChartAdapter.CountriesListStatsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountriesListStatsViewHolder(ItemPieChartCountryTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CountriesListStatsViewHolder, position: Int) =
        holder.bind(getItem(position))

    class CountriesListStatsViewHolder(
        private val binding: ItemPieChartCountryTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(worldCountryStatsUI: WorldCountryStatsUI) {
            val ctx = itemView.context
            val chartTotal = binding.chartTotal

            chartTotal.configure()

            chartTotal.setValues(
                ctx,
                listOf(worldCountryStatsUI.countryStats.stats.confirmed,
                    worldCountryStatsUI.countryStats.stats.deaths,
                    worldCountryStatsUI.countryStats.stats.recovered,
                    worldCountryStatsUI.countryStats.stats.openCases),
                listOf(R.string.confirmed, R.string.deaths, R.string.recovered, R.string.open_cases),
                listOf(R.color.dark_red, R.color.dark_grey, R.color.dark_green, R.color.dark_blue))

            binding.percentageConfirmed.text =
                worldCountryStatsUI.countryStats.stats.confirmed.percentage(
                    worldCountryStatsUI.worldStats.stats.confirmed)
            binding.percentageDeaths.text =
                worldCountryStatsUI.countryStats.stats.deaths.percentage(
                    worldCountryStatsUI.worldStats.stats.deaths)
            binding.percentageRecovered.text =
                worldCountryStatsUI.countryStats.stats.recovered.percentage(
                    worldCountryStatsUI.worldStats.stats.recovered)
            binding.percentageOpenCases.text =
                worldCountryStatsUI.countryStats.stats.openCases.percentage(
                    worldCountryStatsUI.worldStats.stats.openCases)

            binding.textPlace.text = worldCountryStatsUI.countryStats.name

            binding.icCountryEmoji.setEmojiCountry(worldCountryStatsUI.countryStats.code)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorldCountryStatsUI>() {
            override fun areItemsTheSame(oldItem: WorldCountryStatsUI, newItem: WorldCountryStatsUI): Boolean =
                oldItem.countryStats.id == newItem.countryStats.id

            override fun areContentsTheSame(oldItem: WorldCountryStatsUI, newItem: WorldCountryStatsUI): Boolean =
                oldItem == newItem
        }
    }
}