package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemTotalBinding
import com.jaimegc.covid19tracker.ui.model.PlaceStatsUI

class PlaceTotalAdapter : ListAdapter<PlaceStatsUI, PlaceTotalAdapter.PlaceTotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceTotalViewHolder(
            ItemTotalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PlaceTotalViewHolder, position: Int) =
        holder.bind(getItem(position))

    class PlaceTotalViewHolder(
        private val binding: ItemTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeStatsUI: PlaceStatsUI) {
            binding.textLastUpdatedSource.text =
                itemView.context.getString(R.string.source, placeStatsUI.stats.source)
            binding.textConfirmed.text = placeStatsUI.stats.confirmed
            binding.textOpenCases.text = placeStatsUI.stats.openCases
            binding.textRecovered.text = placeStatsUI.stats.recovered
            binding.textDeaths.text = placeStatsUI.stats.deaths

            binding.textNewConfirmed.text = itemView.context.getString(
                R.string.text_trending,
                placeStatsUI.stats.newConfirmed,
                (placeStatsUI.stats.vsYesterdayConfirmed)
            )
            binding.textNewOpenCases.text = itemView.context.getString(
                R.string.text_trending,
                placeStatsUI.stats.newOpenCases,
                (placeStatsUI.stats.vsYesterdayOpenCases)
            )
            binding.textNewRecovered.text = itemView.context.getString(
                R.string.text_trending,
                placeStatsUI.stats.newRecovered,
                (placeStatsUI.stats.vsYesterdayRecovered)
            )
            binding.textNewDeaths.text = itemView.context.getString(
                R.string.text_trending,
                placeStatsUI.stats.newDeaths,
                (placeStatsUI.stats.vsYesterdayDeaths)
            )
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaceStatsUI>() {
            override fun areItemsTheSame(oldItem: PlaceStatsUI, newItem: PlaceStatsUI): Boolean =
                oldItem.code == newItem.code

            override fun areContentsTheSame(oldItem: PlaceStatsUI, newItem: PlaceStatsUI): Boolean =
                oldItem == newItem
        }
    }
}