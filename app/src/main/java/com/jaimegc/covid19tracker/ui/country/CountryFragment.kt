package com.jaimegc.covid19tracker.ui.country

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
import com.jaimegc.covid19tracker.common.extensions.isVisible
import com.jaimegc.covid19tracker.common.extensions.onItemSelected
import com.jaimegc.covid19tracker.common.extensions.removeAllAdapters
import com.jaimegc.covid19tracker.common.extensions.show
import com.jaimegc.covid19tracker.databinding.FragmentCountryBinding
import com.jaimegc.covid19tracker.data.preference.CountryPreferences
import com.jaimegc.covid19tracker.ui.adapter.CountrySpinnerAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceLineChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlacePieChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceSpinnerAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceTotalAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceTotalBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceTotalPieChartAdapter
import com.jaimegc.covid19tracker.ui.base.BaseFragment
import com.jaimegc.covid19tracker.ui.model.StatsChartUI
import com.jaimegc.covid19tracker.ui.base.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.get

@FlowPreview
@ExperimentalCoroutinesApi
class CountryFragment : BaseFragment<CountryViewModel, PlaceStateScreen>(R.layout.fragment_country) {

    override val viewModel: CountryViewModel by viewModel()

    private val countryPreferences: CountryPreferences = get()
    private val placeTotalAdapter = PlaceTotalAdapter()
    private val placeAdapter = PlaceAdapter()
    private val placeTotalBarChartAdapter = PlaceTotalBarChartAdapter()
    private val placeBarChartAdapter = PlaceBarChartAdapter()
    private val placeTotalPieChartAdapter = PlaceTotalPieChartAdapter()
    private val placePieChartAdapter = PlacePieChartAdapter()
    private val placeLineChartAdapter = PlaceLineChartAdapter()
    private val mergeAdapter = MergeAdapter()

    private lateinit var binding: FragmentCountryBinding
    private lateinit var menu: Menu
    private lateinit var countrySpinnerAdapter: CountrySpinnerAdapter
    private lateinit var placeSpinnerAdapter: PlaceSpinnerAdapter
    private lateinit var statsParent: StatsChartUI

    private var countryJustSelected = false
    private var currentMenuItem = menuItemList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountryBinding.bind(view)

        binding.recyclerPlace.adapter = mergeAdapter

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                ScreenState.Loading ->
                    if (mergeAdapter.adapters.isEmpty()) {
                        binding.emptyDatabase.layout.hide()
                        binding.loading.layout.show()
                    }
                ScreenState.EmptyData ->
                    if (currentMenuItem == menuItemLineChart) {
                        binding.loading.layout.hide()
                        binding.emptyDatabase.layout.show()
                    }
                is ScreenState.Render<PlaceStateScreen> -> {
                    binding.loading.layout.hide()
                    handleRenderState(screenState.renderState)
                }
                is ScreenState.Error<PlaceStateScreen> -> {
                    // Not implemented
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
                    renderState.data.indexOf(renderState.data.first { country ->
                        country.id == countryPreferences.getId() })
                    )

                binding.countrySpinner.onItemSelected { pos ->
                    countrySpinnerAdapter.getCountryId(pos).let { idCountry ->
                        countryPreferences.save(idCountry)
                        countryJustSelected = true
                        countrySpinnerAdapter.saveCurrentPosition(pos)
                        viewModel.getRegionsByCountry(idCountry)
                        selectMenu(idCountry)
                    }
                }
            }
            is PlaceStateScreen.SuccessSpinnerRegions -> {
                if (renderState.data.isNotEmpty()) {
                    binding.regionSpinner.show()
                    binding.icExpandRegion.show()
                    placeSpinnerAdapter =
                        PlaceSpinnerAdapter(requireContext(), renderState.data.toMutableList())
                    binding.regionSpinner.adapter = placeSpinnerAdapter

                    binding.regionSpinner.onItemSelected(ignoreFirst = false) { pos ->
                        if (countryJustSelected.not()) {
                            placeSpinnerAdapter.saveCurrentPosition(pos)
                            selectMenu(countrySpinnerAdapter.getCurrentCountryId(),
                                placeSpinnerAdapter.getId(pos))
                        }
                        countryJustSelected = false
                    }
                } else {
                    binding.regionSpinner.hide()
                    binding.icExpandRegion.hide()
                }
            }
            is PlaceStateScreen.SuccessPlaceAndStats -> {
                if (menu.isCurrentItemChecked(menuItemList)) {
                    mergeAdapter.addAdapter(placeTotalAdapter)
                    placeTotalAdapter.submitList(listOf(renderState.data))
                    binding.recyclerPlace.scrollToPosition(0)
                }
            }
            is PlaceStateScreen.SuccessPlaceStats -> {
                if (menu.isCurrentItemChecked(menuItemList)) {
                    mergeAdapter.addAdapter(placeAdapter)
                    placeAdapter.submitList(renderState.data)
                    binding.recyclerPlace.scrollToPosition(0)
                }
            }
            is PlaceStateScreen.SuccessPlaceTotalStatsBarChart -> {
                if (menu.isCurrentItemChecked(menuItemBarChart)) {
                    mergeAdapter.addAdapter(0, placeTotalBarChartAdapter)
                    if (mergeAdapter.containsAdapter(placeBarChartAdapter)) {
                        binding.recyclerPlace.scrollToPosition(0)
                    }
                    placeTotalBarChartAdapter.submitList(listOf(renderState.data))
                }
            }
            is PlaceStateScreen.SuccessPlaceStatsBarChart -> {
                if (menu.isCurrentItemChecked(menuItemBarChart)) {
                    if (mergeAdapter.containsAdapter(placeTotalBarChartAdapter)) {
                        mergeAdapter.addAdapter(1, placeBarChartAdapter)
                    } else {
                        mergeAdapter.addAdapter(0, placeBarChartAdapter)
                    }
                    placeBarChartAdapter.submitList(renderState.data)
                }
            }
            is PlaceStateScreen.SuccessPlaceTotalStatsPieChart -> {
                if (menu.isCurrentItemChecked(menuItemPieChart)) {
                    statsParent = renderState.data

                    if (statsParent.isNotEmpty()) {
                        mergeAdapter.addAdapter(0, placeTotalPieChartAdapter)

                        if (mergeAdapter.containsAdapter(placePieChartAdapter)) {
                            binding.recyclerPlace.scrollToPosition(0)
                        }

                        placeTotalPieChartAdapter.submitList(listOf(statsParent))
                    } else {
                        binding.emptyDatabase.layout.show()
                    }
                }
            }
            is PlaceStateScreen.SuccessPlaceAndStatsPieChart -> {
                if (menu.isCurrentItemChecked(menuItemPieChart)) {
                    if (mergeAdapter.containsAdapter(placeTotalPieChartAdapter)) {
                        if (placeTotalPieChartAdapter.currentList.isNotEmpty()) {
                            renderState.data.map { placeStats ->
                                placeStats.statsParent = statsParent
                            }
                        }
                        mergeAdapter.addAdapter(1, placePieChartAdapter)
                    } else {
                        mergeAdapter.addAdapter(0, placePieChartAdapter)
                    }

                    placePieChartAdapter.submitList(renderState.data)
                    binding.recyclerPlace.scrollToPosition(0)
                }
            }
            is PlaceStateScreen.SuccessPlaceStatsLineCharts -> {
                if (menu.isCurrentItemChecked(menuItemLineChart)) {
                    mergeAdapter.addAdapter(placeLineChartAdapter)
                    placeLineChartAdapter.submitList(listOf(renderState.data))
                    placeLineChartAdapter.notifyDataSetChanged()
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
        if (::countrySpinnerAdapter.isInitialized) {
            when (item.itemId) {
                R.id.list_view -> {
                    if (menu.isCurrentItemChecked(menuItemList).not()) {
                        mergeAdapter.removeAllAdapters()
                        menu.enableItem(menuItemList)
                        selectMenu(getSelectedCountry(), getSelectedPlace())
                    }
                    true
                }
                R.id.bar_chart_view -> {
                    if (menu.isCurrentItemChecked(menuItemBarChart).not()) {
                        menu.enableItem(menuItemBarChart)
                        selectMenu(getSelectedCountry(), getSelectedPlace())
                    }
                    true
                }
                R.id.line_chart_view -> {
                    if (menu.isCurrentItemChecked(menuItemLineChart).not()) {
                        menu.enableItem(menuItemLineChart)
                        selectMenu(getSelectedCountry(), getSelectedPlace())
                    }
                    true
                }
                R.id.pie_chart_view -> {
                    if (menu.isCurrentItemChecked(menuItemPieChart).not()) {
                        menu.enableItem(menuItemPieChart)
                        selectMenu(getSelectedCountry(), getSelectedPlace())
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

    private fun getSelectedPlace(): String =
        if (binding.regionSpinner.isVisible() && ::placeSpinnerAdapter.isInitialized) {
            placeSpinnerAdapter.getCurrentPlaceId()
        } else {
            ""
        }

    private fun selectMenu(idCountry: String, idRegion: String = "") {
        mergeAdapter.removeAllAdapters()
        binding.emptyDatabase.layout.hide()
        binding.loading.layout.hide()
        currentMenuItem = menu.isCurrentItemChecked()

        when (currentMenuItem) {
            menuItemList ->
                viewModel.getListStats(idCountry, idRegion)
            menuItemBarChart ->
                viewModel.getBarChartStats(idCountry, idRegion)
            menuItemLineChart ->
                viewModel.getLineChartStats(idCountry, idRegion)
            else -> viewModel.getPieChartStats(idCountry, idRegion)
        }
    }
}
