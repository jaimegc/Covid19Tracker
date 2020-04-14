package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemWorldTotalBinding
import com.jaimegc.covid19tracker.ui.model.TodayStatsUI

class WorldTotalAdapter : ListAdapter<TodayStatsUI, WorldTotalAdapter.WorldTotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalViewHolder(ItemWorldTotalBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldTotalViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldTotalViewHolder(private val binding: ItemWorldTotalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(worldStatsUI: TodayStatsUI) {
            binding.textConfirmed.text = worldStatsUI.confirmed
            binding.textOpenCases.text = worldStatsUI.openCases
            binding.textRecovered.text = worldStatsUI.recovered
            binding.textDeaths.text = worldStatsUI.deaths

            binding.textNewConfirmed.text = itemView.context.getString(R.string.text_trending,
                worldStatsUI.newConfirmed, (worldStatsUI.vsYesterdayConfirmed))
            binding.textNewOpenCases.text = itemView.context.getString(R.string.text_trending,
                worldStatsUI.newOpenCases, (worldStatsUI.vsYesterdayOpenCases))
            binding.textNewRecovered.text = itemView.context.getString(R.string.text_trending,
                worldStatsUI.newRecovered, (worldStatsUI.vsYesterdayRecovered))
            binding.textNewDeaths.text = itemView.context.getString(R.string.text_trending,
                worldStatsUI.newDeaths, (worldStatsUI.vsYesterdayDeaths))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TodayStatsUI>() {
            override fun areItemsTheSame(oldItem: TodayStatsUI, newItem: TodayStatsUI): Boolean =
                oldItem.date == newItem.date

            override fun areContentsTheSame(oldItem: TodayStatsUI, newItem: TodayStatsUI): Boolean =
                oldItem == newItem
        }
    }
}