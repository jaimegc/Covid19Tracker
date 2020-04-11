package com.jaimegc.covid19tracker.ui.adapter

import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ItemWorldTotalCountryBinding
import com.jaimegc.covid19tracker.databinding.ItemWorldTotalCountryExpandedBinding
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
                        constraintSetExpand.applyTo(layoutCard)
                        val rotate = RotateAnimation(
                            0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                        rotate.duration = 250
                        rotate.fillAfter = true

                        icExpandCollapse.startAnimation(rotate)
                        //textConfirmed.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
                    } else {
                        val rotate = RotateAnimation(
                            180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                        rotate.duration = 250
                        rotate.fillAfter = true

                        icExpandCollapse.startAnimation(rotate)
                        constraintSetCollapse.applyTo(layoutCard)
                        //textConfirmed.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                    }

                    covidTrackerTotalUI.isExpanded = covidTrackerTotalUI.isExpanded.not()
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CovidTrackerDateCountryUI>() {
            override fun areItemsTheSame(oldItem: CovidTrackerDateCountryUI, newItem: CovidTrackerDateCountryUI): Boolean =
                oldItem.name == newItem.name && oldItem.isExpanded == newItem.isExpanded

            override fun areContentsTheSame(oldItem: CovidTrackerDateCountryUI, newItem: CovidTrackerDateCountryUI): Boolean =
                oldItem == newItem
        }
    }
}