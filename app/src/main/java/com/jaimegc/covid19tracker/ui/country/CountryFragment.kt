package com.jaimegc.covid19tracker.ui.country

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
import com.jaimegc.covid19tracker.databinding.FragmentCountryBinding
import com.jaimegc.covid19tracker.databinding.LoadingBinding
import com.jaimegc.covid19tracker.common.extensions.*
import com.jaimegc.covid19tracker.ui.adapter.*
import com.jaimegc.covid19tracker.ui.model.StatsChartUI
import com.jaimegc.covid19tracker.ui.states.BaseViewScreenState
import com.jaimegc.covid19tracker.ui.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.states.ScreenState
import org.koin.android.viewmodel.ext.android.viewModel

class CountryFragment : Fragment(R.layout.fragment_country),
    BaseViewScreenState<CountryViewModel, PlaceStateScreen> {

    override val viewModel: CountryViewModel by viewModel()
    private val placeTotalAdapter = PlaceTotalAdapter()
    private val placeAdapter = PlaceAdapter()
    private val placeTotalBarChartAdapter = PlaceTotalBarChartAdapter()
    private val placeBarChartAdapter = PlaceBarChartAdapter()
    private val placeTotalPieChartAdapter = PlaceTotalPieChartAdapter()
    private val placePieChartAdapter = PlacePieChartAdapter()
    private val placeLineChartAdapter = PlaceLineChartAdapter()
    private val mergeAdapter = MergeAdapter()

    private lateinit var binding: FragmentCountryBinding
    private lateinit var loadingBinding: LoadingBinding
    private lateinit var menu: Menu
    private lateinit var countrySpinnerAdapter: CountrySpinnerAdapter
    private lateinit var regionSpinnerAdapter: PlaceSpinnerAdapter
    private lateinit var statsParent: StatsChartUI

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountryBinding.bind(view)
        loadingBinding = LoadingBinding.bind(view)

        binding.recyclerPlace.adapter = mergeAdapter

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                ScreenState.Loading -> if (binding.recyclerPlace.isEmpty()) loadingBinding.loading.show()
                is ScreenState.Render<PlaceStateScreen> -> {
                    handleRenderState(screenState.renderState)
                    loadingBinding.loading.hide()
                }
            }
        })

        viewModel.getCountries()
        setHasOptionsMenu(true)
    }

    override fun handleRenderState(renderState: PlaceStateScreen) {
        when (renderState) {
            is PlaceStateScreen.SuccessSpinnerCountries -> {
                countrySpinnerAdapter = CountrySpinnerAdapter(renderState.data)
                binding.countrySpinner.adapter = countrySpinnerAdapter

                binding.countrySpinner.setSelection(
                    renderState.data.indexOf(renderState.data.first { it.id == "spain" }))

                binding.countrySpinner.onItemSelected { pos ->
                    countrySpinnerAdapter.getCountryId(pos).let { idCountry ->
                        viewModel.getRegionsByCountry(idCountry)
                        mergeAdapter.removeAllAdapters()

                        when (menu.isCurrentItemChecked()) {
                            MENU_ITEM_LIST ->
                                viewModel.getListChartStats(idCountry)
                            MENU_ITEM_BAR_CHART ->
                                viewModel.getBarChartStats(idCountry)
                            MENU_ITEM_LINE_CHART ->
                                viewModel.getLineChartStats(idCountry)
                            else -> viewModel.getPieChartStats(idCountry)
                        }
                    }
                }
            }
            is PlaceStateScreen.SuccessSpinnerRegions -> {
                if (renderState.data.isNotEmpty()) {
                    binding.regionSpinner.show()
                    binding.icExpandRegion.show()
                    regionSpinnerAdapter =
                        PlaceSpinnerAdapter(requireContext(), renderState.data.toMutableList())
                    binding.regionSpinner.adapter = regionSpinnerAdapter

                    binding.regionSpinner.onItemSelected(ignoreFirst = true) { pos ->
                        viewModel.getRegionsByCountry(regionSpinnerAdapter.getId(pos))
                    }
                } else {
                    binding.regionSpinner.hide()
                    binding.icExpandRegion.hide()
                }
            }
            is PlaceStateScreen.SuccessCountryAndStats -> {
                mergeAdapter.addAdapter(placeTotalAdapter)
                placeTotalAdapter.submitList(listOf(renderState.data))
            }
            is PlaceStateScreen.SuccessRegionStats -> {
                mergeAdapter.addAdapter(placeAdapter)
                placeAdapter.submitList(renderState.data)
            }
            is PlaceStateScreen.SuccessPlaceTotalStatsBarChart -> {
                mergeAdapter.addAdapter(placeTotalBarChartAdapter)
                placeTotalBarChartAdapter.submitList(listOf(renderState.data))
            }
            is PlaceStateScreen.SuccessPlaceStatsBarChart -> {
                mergeAdapter.addAdapter(placeBarChartAdapter)
                placeBarChartAdapter.submitList(renderState.data)
            }
            is PlaceStateScreen.SuccessCountryAndStatsPieChart -> {
                statsParent = renderState.data
                mergeAdapter.addAdapter(placeTotalPieChartAdapter)
                placeTotalPieChartAdapter.submitList(listOf(statsParent))
            }
            is PlaceStateScreen.SuccessRegionAndStatsPieChart -> {
                mergeAdapter.addAdapter(placePieChartAdapter)
                if (placeTotalPieChartAdapter.currentList.isNotEmpty()) {
                    renderState.data.map { placeStats ->
                        placeStats.statsParent = statsParent
                    }
                }
                placePieChartAdapter.submitList(renderState.data)
            }
            is PlaceStateScreen.SuccessRegionsStatsLineCharts -> {
                mergeAdapter.addAdapter(placeLineChartAdapter)
                placeLineChartAdapter.submitList(listOf(renderState.data))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_country, menu).also {
            this.menu = menu
            menu.enableItem(MENU_ITEM_LIST)
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (::countrySpinnerAdapter.isInitialized) {
            when (item.itemId) {
                R.id.list_view -> {
                    if (menu.isCurrentItemChecked(MENU_ITEM_LIST).not()) {
                        mergeAdapter.removeAllAdapters()
                        menu.enableItem(MENU_ITEM_LIST)
                        viewModel.getListChartStats(getSelectedCountry())
                    }
                    true
                }
                R.id.bar_chart_view -> {
                    if (menu.isCurrentItemChecked(MENU_ITEM_BAR_CHART).not()) {
                        menu.enableItem(MENU_ITEM_BAR_CHART)
                        mergeAdapter.removeAllAdapters()
                        viewModel.getBarChartStats(getSelectedCountry())
                    }
                    true
                }
                R.id.line_chart_view -> {
                    if (menu.isCurrentItemChecked(MENU_ITEM_LINE_CHART).not()) {
                        menu.enableItem(MENU_ITEM_LINE_CHART)
                        mergeAdapter.removeAllAdapters()
                        viewModel.getLineChartStats(getSelectedCountry())
                    }
                    true
                }
                R.id.pie_chart_view -> {
                    if (menu.isCurrentItemChecked(MENU_ITEM_PIE_CHART).not()) {
                        menu.enableItem(MENU_ITEM_PIE_CHART)
                        mergeAdapter.removeAllAdapters()
                        viewModel.getPieChartStats(getSelectedCountry())
                    }
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else {
            super.onOptionsItemSelected(item)
        }

    private fun getSelectedCountry(): String =
        countrySpinnerAdapter.getCountryId(
            binding.countrySpinner.selectedItemId.toInt()
        )

    companion object {
        private const val MENU_ITEM_LIST = 0
        private const val MENU_ITEM_BAR_CHART = 1
        private const val MENU_ITEM_LINE_CHART = 2
        private const val MENU_ITEM_PIE_CHART = 3
    }
}
