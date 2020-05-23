package com.jaimegc.covid19tracker.ui.country

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.FragmentCountryBinding
import com.jaimegc.covid19tracker.databinding.LoadingBinding
import com.jaimegc.covid19tracker.common.extensions.*
import com.jaimegc.covid19tracker.data.preference.CountryPreferences
import com.jaimegc.covid19tracker.ui.adapter.*
import com.jaimegc.covid19tracker.ui.base.BaseFragment
import com.jaimegc.covid19tracker.ui.model.StatsChartUI
import com.jaimegc.covid19tracker.ui.base.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.get

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
    private lateinit var loadingBinding: LoadingBinding
    private lateinit var menu: Menu
    private lateinit var countrySpinnerAdapter: CountrySpinnerAdapter
    private lateinit var placeSpinnerAdapter: PlaceSpinnerAdapter
    private lateinit var statsParent: StatsChartUI

    private var countryJustSelected = false

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
                mergeAdapter.addAdapter(placeTotalAdapter)
                placeTotalAdapter.submitList(listOf(renderState.data))
            }
            is PlaceStateScreen.SuccessPlaceStats -> {
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
                        selectMenu(getSelectedCountry(), getSelectedPlace())
                    }
                    true
                }
                R.id.bar_chart_view -> {
                    if (menu.isCurrentItemChecked(MENU_ITEM_BAR_CHART).not()) {
                        menu.enableItem(MENU_ITEM_BAR_CHART)
                        selectMenu(getSelectedCountry(), getSelectedPlace())
                    }
                    true
                }
                R.id.line_chart_view -> {
                    if (menu.isCurrentItemChecked(MENU_ITEM_LINE_CHART).not()) {
                        menu.enableItem(MENU_ITEM_LINE_CHART)
                        selectMenu(getSelectedCountry(), getSelectedPlace())
                    }
                    true
                }
                R.id.pie_chart_view -> {
                    if (menu.isCurrentItemChecked(MENU_ITEM_PIE_CHART).not()) {
                        menu.enableItem(MENU_ITEM_PIE_CHART)
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

        when (menu.isCurrentItemChecked()) {
            MENU_ITEM_LIST ->
                viewModel.getListChartStats(idCountry, idRegion)
            MENU_ITEM_BAR_CHART ->
                viewModel.getBarChartStats(idCountry, idRegion)
            MENU_ITEM_LINE_CHART ->
                viewModel.getLineChartStats(idCountry, idRegion)
            else -> viewModel.getPieChartStats(idCountry, idRegion)
        }
    }

    companion object {
        private const val MENU_ITEM_LIST = 0
        private const val MENU_ITEM_BAR_CHART = 1
        private const val MENU_ITEM_LINE_CHART = 2
        private const val MENU_ITEM_PIE_CHART = 3
    }
}
