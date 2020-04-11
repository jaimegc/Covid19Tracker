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
import com.jaimegc.covid19tracker.ui.model.CovidTrackerDateCountryUI


class WorldTotalCountryAdapter : ListAdapter<CovidTrackerDateCountryUI, WorldTotalCountryAdapter.WorldTotalCountryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WorldTotalCountryViewHolder(ItemWorldTotalCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holderCountry: WorldTotalCountryViewHolder, position: Int) =
        holderCountry.bind(getItem(position))

    class WorldTotalCountryViewHolder(private val binding: ItemWorldTotalCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(covidTrackerTotalUI: CovidTrackerDateCountryUI) {
            val constraintSetCollapse = ConstraintSet()
            val constraintSetExpand = ConstraintSet()
            lateinit var layoutCard: ConstraintLayout

            with (binding) {
                constraintSetCollapse.clone(itemView.context, R.layout.item_world_total_country)
                constraintSetExpand.clone(itemView.context, R.layout.item_world_total_country_expanded)
                textPlace.text = covidTrackerTotalUI.name
                textConfirmed.text = covidTrackerTotalUI.total.todayConfirmed
                textOpenCases.text = covidTrackerTotalUI.total.todayOpenCases
                textRecovered.text = covidTrackerTotalUI.total.todayNewRecovered
                textDeaths.text = covidTrackerTotalUI.total.todayDeaths

                binding.textNewConfirmed.text = itemView.context.getString(R.string.text_trending,
                    covidTrackerTotalUI.total.todayNewConfirmed, (covidTrackerTotalUI.total.todayVsYesterdayConfirmed))
                binding.textNewOpenCases.text = itemView.context.getString(R.string.text_trending,
                    covidTrackerTotalUI.total.todayNewOpenCases, (covidTrackerTotalUI.total.todayVsYesterdayOpenCases))
                binding.textNewRecovered.text = itemView.context.getString(R.string.text_trending,
                    covidTrackerTotalUI.total.todayNewRecovered, (covidTrackerTotalUI.total.todayVsYesterdayRecovered))
                binding.textNewDeaths.text = itemView.context.getString(R.string.text_trending,
                    covidTrackerTotalUI.total.todayNewDeaths, (covidTrackerTotalUI.total.todayVsYesterdayDeaths))

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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CovidTrackerDateCountryUI>() {
            override fun areItemsTheSame(oldItem: CovidTrackerDateCountryUI, newItem: CovidTrackerDateCountryUI): Boolean =
                oldItem.name == newItem.name && oldItem.isExpanded == newItem.isExpanded

            override fun areContentsTheSame(oldItem: CovidTrackerDateCountryUI, newItem: CovidTrackerDateCountryUI): Boolean =
                oldItem == newItem
        }

        private const val TEXT_SIZE_EXPANDED = 20f
        private const val TEXT_SIZE_COLLAPSED = 15f
    }
}