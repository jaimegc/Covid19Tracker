package com.jaimegc.covid19tracker.ui.world

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.containsAdapter
import com.jaimegc.covid19tracker.common.extensions.enableItem
import com.jaimegc.covid19tracker.common.extensions.hide
import com.jaimegc.covid19tracker.common.extensions.isCurrentItemChecked
import com.jaimegc.covid19tracker.common.extensions.removeAllAdapters
import com.jaimegc.covid19tracker.common.extensions.show
import com.jaimegc.covid19tracker.databinding.FragmentWorldBinding
import com.jaimegc.covid19tracker.ui.adapter.WorldAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountriesBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountriesPieChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountryAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldLineChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldPieChartAdapter
import com.jaimegc.covid19tracker.ui.base.BaseFragment
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.base.states.WorldStateScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class WorldFragment : BaseFragment<WorldViewModel, WorldStateScreen>(R.layout.fragment_world) {

    override val viewModel: WorldViewModel by viewModel()

    private val worldAdapter = WorldAdapter()
    private val worldCountryAdapter = WorldCountryAdapter()
    private val worldBarChartAdapter = WorldBarChartAdapter()
    private val worldBarCountriesChartAdapter = WorldCountriesBarChartAdapter()
    private val worldLineChartAdapter = WorldLineChartAdapter()
    private val worldCountriesPieChartAdapter = WorldCountriesPieChartAdapter()
    private val worldPieChartAdapter = WorldPieChartAdapter()
    private val mergeAdapter = MergeAdapter()

    private lateinit var binding: FragmentWorldBinding
    private lateinit var menu: Menu

    private var currentMenuItem = menuItemList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorldBinding.bind(view)

        binding.recyclerWorld.adapter = mergeAdapter

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                ScreenState.Loading ->
                    if (mergeAdapter.adapters.isEmpty()) binding.loading.layout.show()
                is ScreenState.Render<WorldStateScreen> -> {
                    binding.loading.layout.hide()
                    handleRenderState(screenState.renderState)
                }
                is ScreenState.Error<WorldStateScreen> -> {
                    // Not implemented
                }
            }
        })

        viewModel.getListChartStats()
        setHasOptionsMenu(true)
    }

    override fun handleRenderState(renderState: WorldStateScreen) {
        when (renderState) {
            is WorldStateScreen.SuccessCovidTracker -> {
                if (menu.isCurrentItemChecked(menuItemList)) {
                    mergeAdapter.removeAllAdapters()
                    mergeAdapter.addAdapter(worldAdapter)
                    mergeAdapter.addAdapter(worldCountryAdapter)
                    worldAdapter.submitList(listOf(renderState.data.worldStats))
                    worldCountryAdapter.submitList(renderState.data.countriesStats)
                }
            }
            is WorldStateScreen.SuccessWorldStatsBarCharts -> {
                if (menu.isCurrentItemChecked(menuItemBarChart)) {
                    mergeAdapter.addAdapter(0, worldBarChartAdapter)
                    if (mergeAdapter.containsAdapter(worldBarCountriesChartAdapter)) {
                        binding.recyclerWorld.scrollToPosition(0)
                    }
                    worldBarChartAdapter.submitList(listOf(renderState.data))
                }
            }
            is WorldStateScreen.SuccessCountriesStatsBarCharts -> {
                if (menu.isCurrentItemChecked(menuItemBarChart)) {
                    if (mergeAdapter.containsAdapter(worldBarChartAdapter)) {
                        mergeAdapter.addAdapter(1, worldBarCountriesChartAdapter)
                    } else {
                        mergeAdapter.addAdapter(0, worldBarCountriesChartAdapter)
                    }
                    worldBarCountriesChartAdapter.submitList(renderState.data)
                }
            }
            is WorldStateScreen.SuccessCountriesStatsLineCharts -> {
                if (menu.isCurrentItemChecked(menuItemLineChart)) {
                    mergeAdapter.addAdapter(worldLineChartAdapter)
                    worldLineChartAdapter.submitList(listOf(renderState.data))
                    worldLineChartAdapter.notifyDataSetChanged()
                }
            }
            is WorldStateScreen.SuccessCountriesStatsPieCharts -> {
                if (menu.isCurrentItemChecked(menuItemPieChart)) {
                    mergeAdapter.addAdapter(worldPieChartAdapter)
                    mergeAdapter.addAdapter(worldCountriesPieChartAdapter)
                    worldPieChartAdapter.submitList(listOf(renderState.data[0].worldStats))
                    worldCountriesPieChartAdapter.submitList(renderState.data)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_world, menu).also {
            this.menu = menu
            menu.enableItem(currentMenuItem)
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.list_view -> {
                if (menu.isCurrentItemChecked(menuItemList).not()) {
                    menu.enableItem(menuItemList)
                    currentMenuItem = menuItemList
                    mergeAdapter.removeAllAdapters()
                    viewModel.getListChartStats()
                }
                true
            }
            R.id.bar_chart_view -> {
                if (menu.isCurrentItemChecked(menuItemBarChart).not()) {
                    menu.enableItem(menuItemBarChart)
                    currentMenuItem = menuItemBarChart
                    mergeAdapter.removeAllAdapters()
                    viewModel.getBarChartStats()
                }
                true
            }
            R.id.line_chart_view -> {
                if (menu.isCurrentItemChecked(menuItemLineChart).not()) {
                    menu.enableItem(menuItemLineChart)
                    currentMenuItem = menuItemLineChart
                    mergeAdapter.removeAllAdapters()
                    viewModel.getLineChartStats()
                }
                true
            }
            R.id.pie_chart_view -> {
                if (menu.isCurrentItemChecked(menuItemPieChart).not()) {
                    menu.enableItem(menuItemPieChart)
                    currentMenuItem = menuItemPieChart
                    mergeAdapter.removeAllAdapters()
                    viewModel.getPieChartStats()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
