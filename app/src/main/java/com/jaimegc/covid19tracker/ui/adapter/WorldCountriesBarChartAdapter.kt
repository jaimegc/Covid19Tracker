package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemBarChartCountryTotalBinding
import com.jaimegc.covid19tracker.extensions.chart.configure
import com.jaimegc.covid19tracker.extensions.chart.setValues
import com.jaimegc.covid19tracker.ui.model.CountryListStatsChartUI

class WorldCountriesBarChartAdapter : ListAdapter<CountryListStatsChartUI, WorldCountriesBarChartAdapter.CountriesListStatsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountriesListStatsViewHolder(ItemBarChartCountryTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CountriesListStatsViewHolder, position: Int) =
        holder.bind(getItem(position))

    class CountriesListStatsViewHolder(
        private val binding: ItemBarChartCountryTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(countryStatsChartUI: CountryListStatsChartUI) {
            val ctx = itemView.context
            val chartConfirmed = binding.chartConfirmed
            val chartDeaths = binding.chartDeaths

            chartConfirmed.configure(countryStatsChartUI.stats.map { countryStats -> countryStats.date },
                countryStatsChartUI.stats.minBy { countryStats -> countryStats.confirmed }!!.confirmed)
            chartDeaths.configure(countryStatsChartUI.stats.map { countryStats -> countryStats.date },
                countryStatsChartUI.stats.minBy { countryStats -> countryStats.deaths }!!.deaths)

            chartConfirmed.setValues(ctx, countryStatsChartUI.stats.map { countryStats ->
                countryStats.confirmed }, R.string.total_confirmed, R.color.dark_red)
            chartDeaths.setValues(ctx, countryStatsChartUI.stats.map { countryStats ->
                countryStats.deaths }, R.string.total_deaths, R.color.dark_grey)

            binding.textPlace.text = countryStatsChartUI.name
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CountryListStatsChartUI>() {
            override fun areItemsTheSame(oldItem: CountryListStatsChartUI, newItem: CountryListStatsChartUI): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CountryListStatsChartUI, newItem: CountryListStatsChartUI): Boolean =
                oldItem == newItem
        }
    }
}