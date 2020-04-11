package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemWorldTotalBinding
import com.jaimegc.covid19tracker.ui.model.CovidTrackerTotalUI

class WorldTotalAdapter : ListAdapter<CovidTrackerTotalUI, WorldTotalAdapter.WorldTotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalViewHolder(ItemWorldTotalBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldTotalViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldTotalViewHolder(private val binding: ItemWorldTotalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(covidTrackerTotalUI: CovidTrackerTotalUI) {
            binding.textConfirmed.text = covidTrackerTotalUI.todayConfirmed
            binding.textOpenCases.text = covidTrackerTotalUI.todayOpenCases
            binding.textRecovered.text = covidTrackerTotalUI.todayRecovered
            binding.textDeaths.text = covidTrackerTotalUI.todayDeaths

            binding.textNewConfirmed.text = itemView.context.getString(R.string.text_trending,
                covidTrackerTotalUI.todayNewConfirmed, (covidTrackerTotalUI.todayVsYesterdayConfirmed))
            binding.textNewOpenCases.text = itemView.context.getString(R.string.text_trending,
                covidTrackerTotalUI.todayNewOpenCases, (covidTrackerTotalUI.todayVsYesterdayOpenCases))
            binding.textNewRecovered.text = itemView.context.getString(R.string.text_trending,
                covidTrackerTotalUI.todayNewRecovered, (covidTrackerTotalUI.todayVsYesterdayRecovered))
            binding.textNewDeaths.text = itemView.context.getString(R.string.text_trending,
                covidTrackerTotalUI.todayNewDeaths, (covidTrackerTotalUI.todayVsYesterdayDeaths))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CovidTrackerTotalUI>() {
            override fun areItemsTheSame(oldItem: CovidTrackerTotalUI, newItem: CovidTrackerTotalUI): Boolean =
                oldItem.date == newItem.date

            override fun areContentsTheSame(oldItem: CovidTrackerTotalUI, newItem: CovidTrackerTotalUI): Boolean =
                oldItem == newItem
        }
    }
}