package com.jaimegc.covid19tracker.ui.adapter

import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemWorldTotalCountryBinding
import com.jaimegc.covid19tracker.databinding.ItemWorldTotalCountryExpandedBinding
import com.jaimegc.covid19tracker.extensions.rotateLeftAnimation
import com.jaimegc.covid19tracker.extensions.rotateRightAnimation
import com.jaimegc.covid19tracker.extensions.setTextSizeSp
import com.jaimegc.covid19tracker.ui.model.CountryStatsUI


class WorldCountryAdapter : ListAdapter<CountryStatsUI, WorldCountryAdapter.WorldTotalCountryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalCountryViewHolder(ItemWorldTotalCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holderCountry: WorldTotalCountryViewHolder, position: Int) =
        holderCountry.bind(getItem(position))

    class WorldTotalCountryViewHolder(private val binding: ItemWorldTotalCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(totalStatsUI: CountryStatsUI) {
            val constraintSetCollapse = ConstraintSet()
            val constraintSetExpand = ConstraintSet()
            lateinit var layoutCard: ConstraintLayout

            with (binding) {
                constraintSetCollapse.clone(itemView.context, R.layout.item_world_total_country)
                constraintSetExpand.clone(itemView.context, R.layout.item_world_total_country_expanded)
                textPlace.text = totalStatsUI.name
                textConfirmed.text = totalStatsUI.stats.confirmed
                textOpenCases.text = totalStatsUI.stats.openCases
                textRecovered.text = totalStatsUI.stats.newRecovered
                textDeaths.text = totalStatsUI.stats.deaths

                binding.textNewConfirmed.text = itemView.context.getString(R.string.text_trending,
                    totalStatsUI.stats.newConfirmed, (totalStatsUI.stats.vsYesterdayConfirmed))
                binding.textNewOpenCases.text = itemView.context.getString(R.string.text_trending,
                    totalStatsUI.stats.newOpenCases, (totalStatsUI.stats.vsYesterdayOpenCases))
                binding.textNewRecovered.text = itemView.context.getString(R.string.text_trending,
                    totalStatsUI.stats.newRecovered, (totalStatsUI.stats.vsYesterdayRecovered))
                binding.textNewDeaths.text = itemView.context.getString(R.string.text_trending,
                    totalStatsUI.stats.newDeaths, (totalStatsUI.stats.vsYesterdayDeaths))

                if (totalStatsUI.isExpanded.not()) {
                    layoutCard = layout
                    constraintSetCollapse.applyTo(layoutCard)
                    icVirus.setImageResource(R.drawable.ic_virus)
                    icExpandCollapse.rotation = 0f
                } else {
                    val bindingExpanded = ItemWorldTotalCountryExpandedBinding.bind(itemView)
                    layoutCard = bindingExpanded.layout
                    constraintSetExpand.applyTo(layoutCard)
                    icExpandCollapse.rotation = 180f
                }

                layout.setOnClickListener {
                    val changeBounds = ChangeBounds()
                    changeBounds.interpolator = OvershootInterpolator()
                    TransitionManager.beginDelayedTransition(layoutCard, changeBounds)

                    if (totalStatsUI.isExpanded.not()) {
                        icExpandCollapse.rotateLeftAnimation()
                        constraintSetExpand.applyTo(layoutCard)
                        applyTextSizes(textConfirmed, textOpenCases, textRecovered, textDeaths, size = TEXT_SIZE_EXPANDED)
                    } else {
                        icExpandCollapse.rotateRightAnimation()
                        constraintSetCollapse.applyTo(layoutCard)
                        applyTextSizes(textConfirmed, textOpenCases, textRecovered, textDeaths, size = TEXT_SIZE_COLLAPSED)
                    }

                    totalStatsUI.isExpanded = totalStatsUI.isExpanded.not()
                }
            }
        }

        private fun applyTextSizes(vararg textViews: TextView, size: Float) =
            textViews.map { text -> text.setTextSizeSp(size) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CountryStatsUI>() {
            override fun areItemsTheSame(oldItem: CountryStatsUI, newItem: CountryStatsUI): Boolean =
                oldItem.name == newItem.name && oldItem.isExpanded == newItem.isExpanded

            override fun areContentsTheSame(oldItem: CountryStatsUI, newItem: CountryStatsUI): Boolean =
                oldItem == newItem
        }

        private const val TEXT_SIZE_EXPANDED = 20f
        private const val TEXT_SIZE_COLLAPSED = 15f
    }
}