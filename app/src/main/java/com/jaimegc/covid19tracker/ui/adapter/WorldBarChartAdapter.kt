package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemBarChartWorldTotalBinding
import com.jaimegc.covid19tracker.extensions.chart.configure
import com.jaimegc.covid19tracker.extensions.chart.setValuesChart
import com.jaimegc.covid19tracker.ui.model.WorldStatsChartUI

class WorldBarChartAdapter : ListAdapter<List<WorldStatsChartUI>, WorldBarChartAdapter.WorldTotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalViewHolder(ItemBarChartWorldTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldTotalViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldTotalViewHolder(
        private val binding: ItemBarChartWorldTotalBinding
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

            chartConfirmed.configure(listWorldStatsChartUI.map { worldStats -> worldStats.date },
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.confirmed }!!.stats.confirmed)
            chartRecovered.configure(listWorldStatsChartUI.map { worldStats -> worldStats.date },
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.recovered }!!.stats.recovered)
            chartDeaths.configure(listWorldStatsChartUI.map { worldStats -> worldStats.date },
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.deaths }!!.stats.deaths)
            chartOpenCases.configure(listWorldStatsChartUI.map { worldStats -> worldStats.date },
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.openCases }!!.stats.openCases)
            chartNewConfirmed.configure(listWorldStatsChartUI.map { worldStats -> worldStats.date },
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.newConfirmed }!!.stats.newConfirmed)
            chartNewRecovered.configure(listWorldStatsChartUI.map { worldStats -> worldStats.date },
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.newRecovered }!!.stats.newRecovered)
            chartNewDeaths.configure(listWorldStatsChartUI.map { worldStats -> worldStats.date },
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.newDeaths }!!.stats.newDeaths)
            chartNewOpenCases.configure(listWorldStatsChartUI.map { worldStats -> worldStats.date },
                listWorldStatsChartUI.minBy { worldStats -> worldStats.stats.newOpenCases }!!.stats.newOpenCases)

            chartConfirmed.setValuesChart(ctx, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.confirmed }, R.string.total_confirmed, R.color.dark_red)
            chartDeaths.setValuesChart(ctx, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.deaths }, R.string.total_deaths, R.color.dark_grey)
            chartOpenCases.setValuesChart(ctx, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.openCases }, R.string.total_open_cases, R.color.dark_blue)
            chartRecovered.setValuesChart(ctx, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.recovered }, R.string.total_recovered, R.color.dark_green)
            chartNewConfirmed.setValuesChart(ctx, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.newConfirmed }, R.string.total_new_confirmed, R.color.dark_red)
            chartNewDeaths.setValuesChart(ctx, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.newDeaths }, R.string.total_new_deaths, R.color.dark_grey)
            chartNewOpenCases.setValuesChart(ctx, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.newOpenCases }, R.string.total_new_open_cases, R.color.dark_blue)
            chartNewRecovered.setValuesChart(ctx, listWorldStatsChartUI.map { worldStats ->
                worldStats.stats.newRecovered }, R.string.total_new_recovered, R.color.dark_green)
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