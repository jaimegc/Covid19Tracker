package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.chart.configure
import com.jaimegc.covid19tracker.common.extensions.chart.setValues
import com.jaimegc.covid19tracker.common.extensions.percentageSymbol
import com.jaimegc.covid19tracker.databinding.ItemPieChartPlaceTotalBinding
import com.jaimegc.covid19tracker.ui.model.PlaceStatsChartUI

class PlacePieChartAdapter :
    ListAdapter<PlaceStatsChartUI, PlacePieChartAdapter.PlaceListStatsViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceListStatsViewHolder(
            ItemPieChartPlaceTotalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PlaceListStatsViewHolder, position: Int) =
        holder.bind(getItem(position))

    class PlaceListStatsViewHolder(
        private val binding: ItemPieChartPlaceTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeStatsChart: PlaceStatsChartUI) {
            val ctx = itemView.context
            val chartTotal = binding.chartTotal

            chartTotal.configure()

            chartTotal.setValues(
                ctx,
                listOf(
                    placeStatsChart.stats.confirmed,
                    placeStatsChart.stats.deaths,
                    placeStatsChart.stats.recovered,
                    placeStatsChart.stats.openCases
                ),
                listOf(R.string.confirmed, R.string.deaths, R.string.recovered, R.string.open_cases),
                listOf(R.color.dark_red, R.color.dark_grey, R.color.dark_green, R.color.dark_blue)
            )

            placeStatsChart.statsParent?.let { statsParent ->
                binding.percentageConfirmed.text =
                    placeStatsChart.stats.confirmed.percentageSymbol(statsParent.confirmed)
                binding.percentageDeaths.text =
                    placeStatsChart.stats.deaths.percentageSymbol(statsParent.deaths)
                binding.percentageRecovered.text =
                    placeStatsChart.stats.recovered.percentageSymbol(statsParent.recovered)
                binding.percentageOpenCases.text =
                    placeStatsChart.stats.openCases.percentageSymbol(statsParent.openCases)
            }

            binding.textPlace.text = placeStatsChart.place.name
        }
    }

    private object DiffUtilCallback : DiffUtil.ItemCallback<PlaceStatsChartUI>() {
        override fun areItemsTheSame(oldItem: PlaceStatsChartUI, newItem: PlaceStatsChartUI): Boolean =
            oldItem.place.id == newItem.place.id

        override fun areContentsTheSame(oldItem: PlaceStatsChartUI, newItem: PlaceStatsChartUI): Boolean =
            oldItem == newItem
    }
}