package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.chart.configure
import com.jaimegc.covid19tracker.common.extensions.chart.setValues
import com.jaimegc.covid19tracker.databinding.ItemBarChartTotalBinding
import com.jaimegc.covid19tracker.ui.model.StatsChartUI

class PlaceTotalBarChartAdapter : ListAdapter<List<StatsChartUI>, PlaceTotalBarChartAdapter.PlaceTotalBarChartViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceTotalBarChartViewHolder(ItemBarChartTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PlaceTotalBarChartViewHolder, position: Int) =
        holder.bind(getItem(position))

    class PlaceTotalBarChartViewHolder(
        private val binding: ItemBarChartTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeStatsChartUI: List<StatsChartUI>) {
            val ctx = itemView.context
            val chartConfirmed = binding.chartConfirmed
            val chartRecovered = binding.chartRecovered
            val chartDeaths = binding.chartDeaths
            val chartOpenCases = binding.chartOpenCases
            val chartNewConfirmed = binding.chartNewConfirmed
            val chartNewRecovered = binding.chartNewRecovered
            val chartNewDeaths = binding.chartNewDeaths
            val chartNewOpenCases = binding.chartNewOpenCases

            chartConfirmed.configure(placeStatsChartUI.map { placeStats -> placeStats.date },
                placeStatsChartUI.minBy { placeStats -> placeStats.confirmed }!!.confirmed)
            chartRecovered.configure(placeStatsChartUI.map { placeStats -> placeStats.date },
                placeStatsChartUI.minBy { placeStats -> placeStats.recovered }!!.recovered)
            chartDeaths.configure(placeStatsChartUI.map { placeStats -> placeStats.date },
                placeStatsChartUI.minBy { placeStats -> placeStats.deaths }!!.deaths)
            chartOpenCases.configure(placeStatsChartUI.map { placeStats -> placeStats.date },
                placeStatsChartUI.minBy { placeStats -> placeStats.openCases }!!.openCases)
            chartNewConfirmed.configure(placeStatsChartUI.map { placeStats -> placeStats.date },
                placeStatsChartUI.minBy { placeStats -> placeStats.newConfirmed }!!.newConfirmed)
            chartNewRecovered.configure(placeStatsChartUI.map { placeStats -> placeStats.date },
                placeStatsChartUI.minBy { placeStats -> placeStats.newRecovered }!!.newRecovered)
            chartNewDeaths.configure(placeStatsChartUI.map { placeStats -> placeStats.date },
                placeStatsChartUI.minBy { placeStats -> placeStats.newDeaths }!!.newDeaths)
            chartNewOpenCases.configure(placeStatsChartUI.map { placeStats -> placeStats.date },
                placeStatsChartUI.minBy { placeStats -> placeStats.newOpenCases }!!.newOpenCases)

            chartConfirmed.setValues(ctx, placeStatsChartUI.map { placeStats ->
                placeStats.confirmed }, R.string.total_confirmed, R.color.dark_red)
            chartDeaths.setValues(ctx, placeStatsChartUI.map { placeStats ->
                placeStats.deaths }, R.string.total_deaths, R.color.dark_grey)
            chartOpenCases.setValues(ctx, placeStatsChartUI.map { placeStats ->
                placeStats.openCases }, R.string.total_open_cases, R.color.dark_blue)
            chartRecovered.setValues(ctx, placeStatsChartUI.map { placeStats ->
                placeStats.recovered }, R.string.total_recovered, R.color.dark_green)
            chartNewConfirmed.setValues(ctx, placeStatsChartUI.map { placeStats ->
                placeStats.newConfirmed }, R.string.total_new_confirmed, R.color.dark_red)
            chartNewDeaths.setValues(ctx, placeStatsChartUI.map { placeStats ->
                placeStats.newDeaths }, R.string.total_new_deaths, R.color.dark_grey)
            chartNewOpenCases.setValues(ctx, placeStatsChartUI.map { placeStats ->
                placeStats.newOpenCases }, R.string.total_new_open_cases, R.color.dark_blue)
            chartNewRecovered.setValues(ctx, placeStatsChartUI.map { placeStats ->
                placeStats.newRecovered }, R.string.total_new_recovered, R.color.dark_green)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<List<StatsChartUI>>() {
            override fun areItemsTheSame(oldItem: List<StatsChartUI>, newItem: List<StatsChartUI>): Boolean =
                oldItem.size == newItem.size

            override fun areContentsTheSame(oldItem: List<StatsChartUI>, newItem: List<StatsChartUI>): Boolean =
                oldItem == newItem
        }
    }
}