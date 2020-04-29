package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemPieChartWorldTotalBinding
import com.jaimegc.covid19tracker.extensions.chart.configure
import com.jaimegc.covid19tracker.extensions.chart.setValues
import com.jaimegc.covid19tracker.ui.model.WorldStatsChartUI

class WorldPieChartAdapter : ListAdapter<WorldStatsChartUI, WorldPieChartAdapter.WorldViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldViewHolder(ItemPieChartWorldTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldViewHolder(
        private val binding: ItemPieChartWorldTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(worldStatsChartUI: WorldStatsChartUI) {
            val ctx = itemView.context
            val chartTotal = binding.chartTotal

            chartTotal.configure()

            chartTotal.setValues(
                ctx,
                listOf(worldStatsChartUI.stats.confirmed,
                    worldStatsChartUI.stats.deaths, worldStatsChartUI.stats.recovered,
                    worldStatsChartUI.stats.openCases),
                listOf(R.string.confirmed, R.string.deaths, R.string.recovered, R.string.open_cases),
                listOf(R.color.dark_red, R.color.dark_grey, R.color.dark_green, R.color.dark_blue))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorldStatsChartUI>() {
            override fun areItemsTheSame(oldItem: WorldStatsChartUI, newItem: WorldStatsChartUI): Boolean =
                oldItem.date == newItem.date

            override fun areContentsTheSame(oldItem: WorldStatsChartUI, newItem: WorldStatsChartUI): Boolean =
                oldItem == newItem
        }
    }
}