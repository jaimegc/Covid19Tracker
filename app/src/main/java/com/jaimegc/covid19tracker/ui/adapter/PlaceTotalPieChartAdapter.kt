package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.chart.configure
import com.jaimegc.covid19tracker.common.extensions.chart.setValues
import com.jaimegc.covid19tracker.databinding.ItemPieChartTotalBinding
import com.jaimegc.covid19tracker.ui.model.StatsChartUI

class PlaceTotalPieChartAdapter : ListAdapter<StatsChartUI, PlaceTotalPieChartAdapter.PlacePieChartViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlacePieChartViewHolder(ItemPieChartTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PlacePieChartViewHolder, position: Int) =
        holder.bind(getItem(position))

    class PlacePieChartViewHolder(
        private val binding: ItemPieChartTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(statsChartUI: StatsChartUI) {
            val ctx = itemView.context
            val chartTotal = binding.chartTotal

            chartTotal.configure()

            chartTotal.setValues(
                ctx,
                listOf(statsChartUI.confirmed,
                    statsChartUI.deaths, statsChartUI.recovered,
                    statsChartUI.openCases),
                listOf(R.string.confirmed, R.string.deaths, R.string.recovered, R.string.open_cases),
                listOf(R.color.dark_red, R.color.dark_grey, R.color.dark_green, R.color.dark_blue))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StatsChartUI>() {
            override fun areItemsTheSame(oldItem: StatsChartUI, newItem: StatsChartUI): Boolean =
                oldItem.date == newItem.date

            override fun areContentsTheSame(oldItem: StatsChartUI, newItem: StatsChartUI): Boolean =
                oldItem == newItem
        }
    }
}