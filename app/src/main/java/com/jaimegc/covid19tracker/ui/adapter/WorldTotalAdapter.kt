package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemWorldTotalBinding
import com.jaimegc.covid19tracker.ui.model.CovidTrackerWorldStatsUI

class WorldTotalAdapter : ListAdapter<CovidTrackerWorldStatsUI, WorldTotalAdapter.WorldTotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalViewHolder(ItemWorldTotalBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldTotalViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldTotalViewHolder(private val binding: ItemWorldTotalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(covidTrackerWorldStatsUI: CovidTrackerWorldStatsUI) {
            binding.textConfirmed.text = covidTrackerWorldStatsUI.todayConfirmed
            binding.textOpenCases.text = covidTrackerWorldStatsUI.todayOpenCases
            binding.textRecovered.text = covidTrackerWorldStatsUI.todayRecovered
            binding.textDeaths.text = covidTrackerWorldStatsUI.todayDeaths

            binding.textNewConfirmed.text = itemView.context.getString(R.string.text_trending,
                covidTrackerWorldStatsUI.todayNewConfirmed, (covidTrackerWorldStatsUI.todayVsYesterdayConfirmed))
            binding.textNewOpenCases.text = itemView.context.getString(R.string.text_trending,
                covidTrackerWorldStatsUI.todayNewOpenCases, (covidTrackerWorldStatsUI.todayVsYesterdayOpenCases))
            binding.textNewRecovered.text = itemView.context.getString(R.string.text_trending,
                covidTrackerWorldStatsUI.todayNewRecovered, (covidTrackerWorldStatsUI.todayVsYesterdayRecovered))
            binding.textNewDeaths.text = itemView.context.getString(R.string.text_trending,
                covidTrackerWorldStatsUI.todayNewDeaths, (covidTrackerWorldStatsUI.todayVsYesterdayDeaths))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CovidTrackerWorldStatsUI>() {
            override fun areItemsTheSame(oldItem: CovidTrackerWorldStatsUI, newItem: CovidTrackerWorldStatsUI): Boolean =
                oldItem.date == newItem.date

            override fun areContentsTheSame(oldItem: CovidTrackerWorldStatsUI, newItem: CovidTrackerWorldStatsUI): Boolean =
                oldItem == newItem
        }
    }
}