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
            val chartConfirmed = binding.chartDeaths

            chartConfirmed.configure()

            /*chartConfirmed.setValues(ctx, listOf(worldCountryStatsUI.worldStats.stats.confirmed,
                worldCountryStatsUI.worldStats.stats.deaths, worldCountryStatsUI.worldStats.stats.recovered,
                worldCountryStatsUI.worldStats.stats.openCases), R.string.total_confirmed,
                listOf(R.color.dark_red, R.color.dark_grey, R.color.dark_green, R.color.dark_blue))*/

            binding.textPlace.text = worldCountryStatsUI.countryStats.name
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