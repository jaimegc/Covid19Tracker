package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.chart.configure
import com.jaimegc.covid19tracker.common.extensions.chart.setValues
import com.jaimegc.covid19tracker.databinding.ItemBarChartPlaceBinding
import com.jaimegc.covid19tracker.ui.model.PlaceListStatsChartUI

class PlaceBarChartAdapter :
    ListAdapter<PlaceListStatsChartUI, PlaceBarChartAdapter.PlaceListStatsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceListStatsViewHolder(
            ItemBarChartPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PlaceListStatsViewHolder, position: Int) =
        holder.bind(getItem(position))

    class PlaceListStatsViewHolder(
        private val binding: ItemBarChartPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeStatsChartUI: PlaceListStatsChartUI) {
            val ctx = itemView.context
            val chartConfirmed = binding.chartConfirmed
            val chartDeaths = binding.chartDeaths
            val chartRecovered = binding.chartRecovered
            val chartOpenCases = binding.chartOpenCases

            chartConfirmed.configure(
                placeStatsChartUI.stats.map { placeStats -> placeStats.date },
                placeStatsChartUI.stats.minBy { placeStats -> placeStats.confirmed }!!.confirmed
            )
            chartDeaths.configure(
                placeStatsChartUI.stats.map { placeStats -> placeStats.date },
                placeStatsChartUI.stats.minBy { placeStats -> placeStats.deaths }!!.deaths
            )
            chartRecovered.configure(
                placeStatsChartUI.stats.map { placeStats -> placeStats.date },
                placeStatsChartUI.stats.minBy { placeStats -> placeStats.recovered }!!.recovered
            )
            chartOpenCases.configure(
                placeStatsChartUI.stats.map { placeStats -> placeStats.date },
                placeStatsChartUI.stats.minBy { placeStats -> placeStats.openCases }!!.openCases
            )

            chartConfirmed.setValues(
                ctx,
                placeStatsChartUI.stats.map { placeStats ->
                    placeStats.confirmed },
                R.string.total_confirmed,
                R.color.dark_red
            )
            chartDeaths.setValues(
                ctx,
                placeStatsChartUI.stats.map { placeStats ->
                    placeStats.deaths },
                R.string.total_deaths,
                R.color.dark_grey
            )
            chartRecovered.setValues(
                ctx,
                placeStatsChartUI.stats.map { placeStats ->
                    placeStats.recovered },
                R.string.total_recovered,
                R.color.dark_green
            )
            chartOpenCases.setValues(
                ctx,
                placeStatsChartUI.stats.map { placeStats ->
                    placeStats.openCases },
                R.string.total_open_cases,
                R.color.dark_blue
            )

            binding.textPlace.text = placeStatsChartUI.place.name
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaceListStatsChartUI>() {
            override fun areItemsTheSame(oldItem: PlaceListStatsChartUI, newItem: PlaceListStatsChartUI): Boolean =
                oldItem.place.id == newItem.place.id

            override fun areContentsTheSame(oldItem: PlaceListStatsChartUI, newItem: PlaceListStatsChartUI): Boolean =
                oldItem == newItem
        }
    }
}