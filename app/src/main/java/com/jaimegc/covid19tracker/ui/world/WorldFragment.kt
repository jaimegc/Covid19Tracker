package com.jaimegc.covid19tracker.ui.world

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.FragmentWorldBinding
import com.jaimegc.covid19tracker.extensions.hide
import com.jaimegc.covid19tracker.extensions.show
import com.jaimegc.covid19tracker.extensions.updateAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountriesBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountryAdapter
import com.jaimegc.covid19tracker.ui.states.ScreenState
import com.jaimegc.covid19tracker.ui.states.BaseViewScreenState
import com.jaimegc.covid19tracker.ui.states.WorldStateScreen
import org.koin.android.viewmodel.ext.android.viewModel

class WorldFragment : Fragment(R.layout.fragment_world),
    BaseViewScreenState<WorldViewModel, WorldStateScreen> {

    override val viewModel: WorldViewModel by viewModel()
    private val worldAdapter = WorldAdapter()
    private val worldCountryAdapter = WorldCountryAdapter()
    private val worldBarChartAdapter = WorldBarChartAdapter()
    private val worldBarCountriesChartAdapter = WorldCountriesBarChartAdapter()
    private val mergeAdapter = MergeAdapter(worldAdapter, worldCountryAdapter)
    private val mergeAdapterGraphs = MergeAdapter(worldBarChartAdapter, worldBarCountriesChartAdapter)
    private lateinit var binding: FragmentWorldBinding
    private lateinit var menu: Menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorldBinding.bind(view)

        binding.recyclerWorld.adapter = mergeAdapter

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                ScreenState.Loading -> if (binding.recyclerWorld.isEmpty()) binding.loading.show()
                is ScreenState.Render<WorldStateScreen> -> {
                    binding.loading.hide()
                    handleRenderState(screenState.renderState)
                }
            }
        })

        viewModel.getCovidTrackerLast()
        setHasOptionsMenu(true)
    }

    override fun handleRenderState(renderState: WorldStateScreen) {
        when (renderState) {
            is WorldStateScreen.SuccessCovidTracker -> {
                binding.recyclerWorld.updateAdapter(mergeAdapter)
                worldCountryAdapter.submitList(renderState.data.countriesStats)
                worldAdapter.submitList(listOf(renderState.data.worldStats))
            }
            is WorldStateScreen.SuccessWorldStatsCharts -> {
                binding.recyclerWorld.updateAdapter(mergeAdapterGraphs)
                worldBarChartAdapter.submitList(listOf(renderState.data))
            }
            is WorldStateScreen.SuccessCountriesStatsCharts -> {
                binding.recyclerWorld.updateAdapter(mergeAdapterGraphs)
                //println("RUINA: ${renderState.data}")
                worldBarCountriesChartAdapter.submitList(renderState.data)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_world, menu).also {
            this.menu = menu
            menu.getItem(1).isVisible = false
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bar_chart_view -> {
                menu.getItem(0).isVisible = false
                menu.getItem(1).isVisible = true
                viewModel.getWorldAllStats()
                viewModel.getCountriesStatsOrderByConfirmed()
                true
            }
            R.id.list_view -> {
                menu.getItem(0).isVisible = true
                menu.getItem(1).isVisible = false
                viewModel.getCovidTrackerLast()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
