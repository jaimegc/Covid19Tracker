package com.jaimegc.covid19tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.rotateLeftAnimation
import com.jaimegc.covid19tracker.common.extensions.rotateRightAnimation
import com.jaimegc.covid19tracker.common.extensions.setTextSizeSp
import com.jaimegc.covid19tracker.databinding.ItemPlaceTotalBinding
import com.jaimegc.covid19tracker.databinding.ItemPlaceTotalExpandedBinding
import com.jaimegc.covid19tracker.ui.model.PlaceStatsUI

class PlaceAdapter : ListAdapter<PlaceStatsUI, PlaceAdapter.PlaceStatsViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceStatsViewHolder(
            ItemPlaceTotalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holderCountry: PlaceStatsViewHolder, position: Int) =
        holderCountry.bind(getItem(position))

    class PlaceStatsViewHolder(
        private val binding: ItemPlaceTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeStatsUI: PlaceStatsUI) {
            val constraintSetCollapse = ConstraintSet()
            val constraintSetExpand = ConstraintSet()
            lateinit var layoutCard: ConstraintLayout

            with(binding) {
                constraintSetCollapse.clone(itemView.context, R.layout.item_place_total)
                constraintSetExpand.clone(itemView.context, R.layout.item_place_total_expanded)
                textPlace.text = placeStatsUI.name
                setStats(placeStatsUI, placeStatsUI.isExpanded)

                textPosition.text = "${layoutPosition}º"

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
                binding.icCountryEmoji.text = ""

                if (placeStatsUI.isExpanded.not()) {
                    layoutCard = layout
                    constraintSetCollapse.applyTo(layoutCard)
                    icVirus.setImageResource(R.drawable.ic_virus)
                    icExpandCollapse.rotation = 0f
                } else {
                    val bindingExpanded = ItemPlaceTotalExpandedBinding.bind(itemView)
                    layoutCard = bindingExpanded.layout
                    constraintSetExpand.applyTo(layoutCard)
                    icExpandCollapse.rotation = 180f
                }

                layout.setOnClickListener {
                    val changeBounds = ChangeBounds()
                    changeBounds.interpolator = OvershootInterpolator()
                    TransitionManager.beginDelayedTransition(layoutCard, changeBounds)

                    if (placeStatsUI.isExpanded.not()) {
                        icExpandCollapse.rotateLeftAnimation()
                        constraintSetExpand.applyTo(layoutCard)
                    } else {
                        icExpandCollapse.rotateRightAnimation()
                        constraintSetCollapse.applyTo(layoutCard)
                    }

                    placeStatsUI.isExpanded = placeStatsUI.isExpanded.not()
                    setStats(placeStatsUI, placeStatsUI.isExpanded)
                }
            }
        }

        private fun applyTextSizes(vararg textViews: TextView, size: Float) =
            textViews.map { text -> text.setTextSizeSp(size) }

        private fun setStats(placeStatsUI: PlaceStatsUI, isExpanded: Boolean = false) {
            val confirmed: String
            val openCases: String
            val recovered: String
            val deaths: String
            val textSize: Float

            when (isExpanded) {
                false -> {
                    confirmed = placeStatsUI.stats.confirmedCompact
                    openCases = placeStatsUI.stats.openCasesCompact
                    recovered = placeStatsUI.stats.recoveredCompact
                    deaths = placeStatsUI.stats.deathsCompact
                    textSize = TEXT_SIZE_COLLAPSED
                }
                true -> {
                    confirmed = placeStatsUI.stats.confirmed
                    openCases = placeStatsUI.stats.openCases
                    recovered = placeStatsUI.stats.recovered
                    deaths = placeStatsUI.stats.deaths
                    textSize = TEXT_SIZE_EXPANDED
                }
            }

            binding.textConfirmed.text = confirmed
            binding.textOpenCases.text = openCases
            binding.textRecovered.text = recovered
            binding.textDeaths.text = deaths

            applyTextSizes(
                binding.textConfirmed,
                binding.textOpenCases,
                binding.textRecovered,
                binding.textDeaths,
                size = textSize
            )
        }
    }

    private object DiffUtilCallback : DiffUtil.ItemCallback<PlaceStatsUI>() {
        override fun areItemsTheSame(oldItem: PlaceStatsUI, newItem: PlaceStatsUI): Boolean =
            oldItem.name == newItem.name && oldItem.isExpanded == newItem.isExpanded

        override fun areContentsTheSame(oldItem: PlaceStatsUI, newItem: PlaceStatsUI): Boolean =
            oldItem == newItem
    }

    companion object {
        private const val TEXT_SIZE_EXPANDED = 20f
        private const val TEXT_SIZE_COLLAPSED = 15f
    }
}