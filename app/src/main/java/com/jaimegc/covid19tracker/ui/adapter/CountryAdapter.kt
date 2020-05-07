package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemTotalBinding
import com.jaimegc.covid19tracker.ui.model.WorldStatsUI

class CountryAdapter : ListAdapter<WorldStatsUI, CountryAdapter.WorldViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldViewHolder(ItemTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WorldViewHolder, position: Int) =
        holder.bind(getItem(position))

    class WorldViewHolder(
        private val binding: ItemTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(worldStatsUI: WorldStatsUI) {
            binding.textConfirmed.text = worldStatsUI.stats.confirmed
            binding.textOpenCases.text = worldStatsUI.stats.openCases
            binding.textRecovered.text = worldStatsUI.stats.recovered
            binding.textDeaths.text = worldStatsUI.stats.deaths

            binding.textNewConfirmed.text = itemView.context.getString(R.string.text_trending,
                worldStatsUI.stats.newConfirmed, (worldStatsUI.stats.vsYesterdayConfirmed))
            binding.textNewOpenCases.text = itemView.context.getString(R.string.text_trending,
                worldStatsUI.stats.newOpenCases, (worldStatsUI.stats.vsYesterdayOpenCases))
            binding.textNewRecovered.text = itemView.context.getString(R.string.text_trending,
                worldStatsUI.stats.newRecovered, (worldStatsUI.stats.vsYesterdayRecovered))
            binding.textNewDeaths.text = itemView.context.getString(R.string.text_trending,
                worldStatsUI.stats.newDeaths, (worldStatsUI.stats.vsYesterdayDeaths))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorldStatsUI>() {
            override fun areItemsTheSame(oldItem: WorldStatsUI, newItem: WorldStatsUI): Boolean =
                oldItem.date == newItem.date

            override fun areContentsTheSame(oldItem: WorldStatsUI, newItem: WorldStatsUI): Boolean =
                oldItem == newItem
        }
    }
}