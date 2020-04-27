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
import com.jaimegc.covid19tracker.extensions.*
import com.jaimegc.covid19tracker.ui.adapter.*
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
    private val worldLineChartAdapter = WorldLineChartAdapter()
    private val mergeAdapter = MergeAdapter()

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
                mergeAdapter.removeAllAdapters()
                mergeAdapter.addAdapter(worldAdapter)
                mergeAdapter.addAdapter(worldCountryAdapter)
                worldAdapter.submitList(listOf(renderState.data.worldStats))
                worldCountryAdapter.submitList(renderState.data.countriesStats)
            }
            is WorldStateScreen.SuccessWorldStatsBarCharts -> {
                mergeAdapter.addAdapter(0, worldBarChartAdapter)
                if (mergeAdapter.containsAdapter(worldBarCountriesChartAdapter)) {
                    binding.recyclerWorld.scrollToPosition(0)
                }
                worldBarChartAdapter.submitList(listOf(renderState.data))
            }
            is WorldStateScreen.SuccessCountriesStatsBarCharts -> {
                if (mergeAdapter.containsAdapter(worldBarChartAdapter)) {
                    mergeAdapter.addAdapter(1, worldBarCountriesChartAdapter)
                } else {
                    mergeAdapter.addAdapter(0, worldBarCountriesChartAdapter)
                }
                worldBarCountriesChartAdapter.submitList(renderState.data)
            }
            is WorldStateScreen.SuccessCountriesStatsLineCharts -> {
                mergeAdapter.addAdapter(worldLineChartAdapter)
                worldLineChartAdapter.submitList(listOf(renderState.data))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_world, menu).also {
            this.menu = menu
            menu.showItems(1)
            menu.hideItems(0, 2)
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bar_chart_view -> {
                menu.showItems(2)
                menu.hideItems(0, 1)
                mergeAdapter.removeAllAdapters()
                viewModel.getWorldAllStats()
                viewModel.getCountriesStatsOrderByConfirmed()
                true
            }
            R.id.line_chart_view -> {
                menu.showItems(0)
                menu.hideItems(1, 2)
                mergeAdapter.removeAllAdapters()
                viewModel.getWorldMostStats()
                true
            }
            R.id.list_view -> {
                menu.showItems(1)
                menu.hideItems(0, 2)
                mergeAdapter.removeAllAdapters()
                viewModel.getCovidTrackerLast()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
