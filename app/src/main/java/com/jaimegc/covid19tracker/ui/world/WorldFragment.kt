package com.jaimegc.covid19tracker.ui.world

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
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
import org.koin.core.component.inject

class WorldFragment : BaseFragment<WorldViewModel, WorldStateScreen>(R.layout.fragment_world) {

    override val viewModel: WorldViewModel by inject()

    private val worldAdapter = WorldAdapter()
    private val worldCountryAdapter = WorldCountryAdapter()
    private val worldBarChartAdapter = WorldBarChartAdapter()
    private val worldBarCountriesChartAdapter = WorldCountriesBarChartAdapter()
    private val worldLineChartAdapter = WorldLineChartAdapter()
    private val worldCountriesPieChartAdapter = WorldCountriesPieChartAdapter()
    private val worldPieChartAdapter = WorldPieChartAdapter()
    private val concatAdapter = ConcatAdapter()

    private lateinit var binding: FragmentWorldBinding
    private lateinit var menu: Menu

    private var currentMenuItem = menuItemList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorldBinding.bind(view)

        binding.recyclerWorld.adapter = concatAdapter

        viewModel.screenState.observe(
            viewLifecycleOwner,
            { screenState ->
                when (screenState) {
                    is ScreenState.Loading ->
                        if (concatAdapter.adapters.isEmpty()) binding.loading.layout.show()
                    is ScreenState.Render<WorldStateScreen> -> {
                        binding.loading.layout.hide()
                        handleRenderState(screenState.renderState)
                    }
                    is ScreenState.EmptyData -> Unit // Not implemented
                    is ScreenState.Error -> Unit // Not implemented
                }
            }
        )

        viewModel.getListStats()
        setHasOptionsMenu(true)
    }

    override fun handleRenderState(renderState: WorldStateScreen) {
        when (renderState) {
            is WorldStateScreen.SuccessCovidTracker -> {
                if (menu.isCurrentItemChecked(menuItemList)) {
                    concatAdapter.removeAllAdapters()
                    concatAdapter.addAdapter(worldAdapter)
                    concatAdapter.addAdapter(worldCountryAdapter)
                    worldAdapter.submitList(listOf(renderState.data.worldStats))
                    worldCountryAdapter.submitList(renderState.data.countriesStats)
                }
            }
            is WorldStateScreen.SuccessWorldStatsBarCharts -> {
                if (menu.isCurrentItemChecked(menuItemBarChart)) {
                    concatAdapter.addAdapter(0, worldBarChartAdapter)
                    if (concatAdapter.containsAdapter(worldBarCountriesChartAdapter)) {
                        binding.recyclerWorld.scrollToPosition(0)
                    }
                    worldBarChartAdapter.submitList(listOf(renderState.data))
                }
            }
            is WorldStateScreen.SuccessCountriesStatsBarCharts -> {
                if (menu.isCurrentItemChecked(menuItemBarChart)) {
                    if (concatAdapter.containsAdapter(worldBarChartAdapter)) {
                        concatAdapter.addAdapter(1, worldBarCountriesChartAdapter)
                    } else {
                        concatAdapter.addAdapter(0, worldBarCountriesChartAdapter)
                    }
                    worldBarCountriesChartAdapter.submitList(renderState.data)
                }
            }
            is WorldStateScreen.SuccessCountriesStatsLineCharts -> {
                if (menu.isCurrentItemChecked(menuItemLineChart)) {
                    concatAdapter.addAdapter(worldLineChartAdapter)
                    worldLineChartAdapter.submitList(listOf(renderState.data))
                    worldLineChartAdapter.notifyDataSetChanged()
                }
            }
            is WorldStateScreen.SuccessCountriesStatsPieCharts -> {
                if (menu.isCurrentItemChecked(menuItemPieChart)) {
                    concatAdapter.addAdapter(worldPieChartAdapter)
                    concatAdapter.addAdapter(worldCountriesPieChartAdapter)
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
                    concatAdapter.removeAllAdapters()
                    viewModel.getListStats()
                }
                true
            }
            R.id.bar_chart_view -> {
                if (menu.isCurrentItemChecked(menuItemBarChart).not()) {
                    menu.enableItem(menuItemBarChart)
                    currentMenuItem = menuItemBarChart
                    concatAdapter.removeAllAdapters()
                    viewModel.getBarChartStats()
                }
                true
            }
            R.id.line_chart_view -> {
                if (menu.isCurrentItemChecked(menuItemLineChart).not()) {
                    menu.enableItem(menuItemLineChart)
                    currentMenuItem = menuItemLineChart
                    concatAdapter.removeAllAdapters()
                    viewModel.getLineChartsStats()
                }
                true
            }
            R.id.pie_chart_view -> {
                if (menu.isCurrentItemChecked(menuItemPieChart).not()) {
                    menu.enableItem(menuItemPieChart)
                    currentMenuItem = menuItemPieChart
                    concatAdapter.removeAllAdapters()
                    viewModel.getPieChartStats()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
