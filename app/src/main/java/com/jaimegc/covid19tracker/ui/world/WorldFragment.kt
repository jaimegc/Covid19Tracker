package com.jaimegc.covid19tracker.ui.world

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.FragmentWorldBinding
import com.jaimegc.covid19tracker.databinding.LoadingBinding
import com.jaimegc.covid19tracker.common.extensions.*
import com.jaimegc.covid19tracker.ui.adapter.*
import com.jaimegc.covid19tracker.ui.base.BaseFragment
import com.jaimegc.covid19tracker.ui.base.states.*
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
    private lateinit var loadingBinding: LoadingBinding
    private lateinit var menu: Menu

    private var currentMenuItem = MENU_ITEM_LIST

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorldBinding.bind(view)
        loadingBinding = LoadingBinding.bind(view)

        binding.recyclerWorld.adapter = mergeAdapter

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                ScreenState.Loading ->
                    if (mergeAdapter.adapters.isEmpty()) loadingBinding.loading.show()
                is ScreenState.Render<WorldStateScreen> -> {
                    loadingBinding.loading.hide()
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
                if (menu.isCurrentItemChecked(MENU_ITEM_LIST)) {
                    mergeAdapter.removeAllAdapters()
                    mergeAdapter.addAdapter(worldAdapter)
                    mergeAdapter.addAdapter(worldCountryAdapter)
                    worldAdapter.submitList(listOf(renderState.data.worldStats))
                    worldCountryAdapter.submitList(renderState.data.countriesStats)
                }
            }
            is WorldStateScreen.SuccessWorldStatsBarCharts -> {
                if (menu.isCurrentItemChecked(MENU_ITEM_BAR_CHART)) {
                    mergeAdapter.addAdapter(0, worldBarChartAdapter)
                    if (mergeAdapter.containsAdapter(worldBarCountriesChartAdapter)) {
                        binding.recyclerWorld.scrollToPosition(0)
                    }
                    worldBarChartAdapter.submitList(listOf(renderState.data))
                }
            }
            is WorldStateScreen.SuccessCountriesStatsBarCharts -> {
                if (menu.isCurrentItemChecked(MENU_ITEM_BAR_CHART)) {
                    if (mergeAdapter.containsAdapter(worldBarChartAdapter)) {
                        mergeAdapter.addAdapter(1, worldBarCountriesChartAdapter)
                    } else {
                        mergeAdapter.addAdapter(0, worldBarCountriesChartAdapter)
                    }
                    worldBarCountriesChartAdapter.submitList(renderState.data)
                }
            }
            is WorldStateScreen.SuccessCountriesStatsLineCharts -> {
                if (menu.isCurrentItemChecked(MENU_ITEM_LINE_CHART)) {
                    mergeAdapter.addAdapter(worldLineChartAdapter)
                    worldLineChartAdapter.submitList(listOf(renderState.data))
                    worldLineChartAdapter.notifyDataSetChanged()
                }
            }
            is WorldStateScreen.SuccessCountriesStatsPieCharts -> {
                if (menu.isCurrentItemChecked(MENU_ITEM_PIE_CHART)) {
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
                if (menu.isCurrentItemChecked(MENU_ITEM_LIST).not()) {
                    menu.enableItem(MENU_ITEM_LIST)
                    currentMenuItem = MENU_ITEM_LIST
                    mergeAdapter.removeAllAdapters()
                    viewModel.getListChartStats()
                }
                true
            }
            R.id.bar_chart_view -> {
                if (menu.isCurrentItemChecked(MENU_ITEM_BAR_CHART).not()) {
                    menu.enableItem(MENU_ITEM_BAR_CHART)
                    currentMenuItem = MENU_ITEM_BAR_CHART
                    mergeAdapter.removeAllAdapters()
                    viewModel.getBarChartStats()
                }
                true
            }
            R.id.line_chart_view -> {
                if (menu.isCurrentItemChecked(MENU_ITEM_LINE_CHART).not()) {
                    menu.enableItem(MENU_ITEM_LINE_CHART)
                    currentMenuItem = MENU_ITEM_LINE_CHART
                    mergeAdapter.removeAllAdapters()
                    viewModel.getLineChartStats()
                }
                true
            }
            R.id.pie_chart_view -> {
                if (menu.isCurrentItemChecked(MENU_ITEM_PIE_CHART).not()) {
                    menu.enableItem(MENU_ITEM_PIE_CHART)
                    currentMenuItem = MENU_ITEM_PIE_CHART
                    mergeAdapter.removeAllAdapters()
                    viewModel.getPieChartStats()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
