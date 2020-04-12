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
import com.jaimegc.covid19tracker.ui.model.CovidTrackerCountryUI


class WorldTotalCountryAdapter : ListAdapter<CovidTrackerCountryUI, WorldTotalCountryAdapter.WorldTotalCountryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalCountryViewHolder(ItemWorldTotalCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holderCountry: WorldTotalCountryViewHolder, position: Int) =
        holderCountry.bind(getItem(position))

    class WorldTotalCountryViewHolder(private val binding: ItemWorldTotalCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(covidTrackerTotalUI: CovidTrackerCountryUI) {
            val constraintSetCollapse = ConstraintSet()
            val constraintSetExpand = ConstraintSet()
            lateinit var layoutCard: ConstraintLayout

            with (binding) {
                constraintSetCollapse.clone(itemView.context, R.layout.item_world_total_country)
                constraintSetExpand.clone(itemView.context, R.layout.item_world_total_country_expanded)
                textPlace.text = covidTrackerTotalUI.name
                textConfirmed.text = covidTrackerTotalUI.worldStats.todayConfirmed
                textOpenCases.text = covidTrackerTotalUI.worldStats.todayOpenCases
                textRecovered.text = covidTrackerTotalUI.worldStats.todayNewRecovered
                textDeaths.text = covidTrackerTotalUI.worldStats.todayDeaths

                binding.textNewConfirmed.text = itemView.context.getString(R.string.text_trending,
                    covidTrackerTotalUI.worldStats.todayNewConfirmed, (covidTrackerTotalUI.worldStats.todayVsYesterdayConfirmed))
                binding.textNewOpenCases.text = itemView.context.getString(R.string.text_trending,
                    covidTrackerTotalUI.worldStats.todayNewOpenCases, (covidTrackerTotalUI.worldStats.todayVsYesterdayOpenCases))
                binding.textNewRecovered.text = itemView.context.getString(R.string.text_trending,
                    covidTrackerTotalUI.worldStats.todayNewRecovered, (covidTrackerTotalUI.worldStats.todayVsYesterdayRecovered))
                binding.textNewDeaths.text = itemView.context.getString(R.string.text_trending,
                    covidTrackerTotalUI.worldStats.todayNewDeaths, (covidTrackerTotalUI.worldStats.todayVsYesterdayDeaths))

                if (covidTrackerTotalUI.isExpanded.not()) {
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

                    if (covidTrackerTotalUI.isExpanded.not()) {
                        icExpandCollapse.rotateLeftAnimation()
                        constraintSetExpand.applyTo(layoutCard)
                        applyTextSizes(textConfirmed, textOpenCases, textRecovered, textDeaths, size = TEXT_SIZE_EXPANDED)
                    } else {
                        icExpandCollapse.rotateRightAnimation()
                        constraintSetCollapse.applyTo(layoutCard)
                        applyTextSizes(textConfirmed, textOpenCases, textRecovered, textDeaths, size = TEXT_SIZE_COLLAPSED)
                    }

                    covidTrackerTotalUI.isExpanded = covidTrackerTotalUI.isExpanded.not()
                }
            }
        }

        private fun applyTextSizes(vararg textViews: TextView, size: Float) =
            textViews.map { text -> text.setTextSizeSp(size) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CovidTrackerCountryUI>() {
            override fun areItemsTheSame(oldItem: CovidTrackerCountryUI, newItem: CovidTrackerCountryUI): Boolean =
                oldItem.name == newItem.name && oldItem.isExpanded == newItem.isExpanded

            override fun areContentsTheSame(oldItem: CovidTrackerCountryUI, newItem: CovidTrackerCountryUI): Boolean =
                oldItem == newItem
        }

        private const val TEXT_SIZE_EXPANDED = 20f
        private const val TEXT_SIZE_COLLAPSED = 15f
    }
}