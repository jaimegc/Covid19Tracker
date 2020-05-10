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
import com.jaimegc.covid19tracker.extensions.*
import com.jaimegc.covid19tracker.ui.adapter.CountrySpinnerAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceTotalAdapter
import com.jaimegc.covid19tracker.ui.states.BaseViewScreenState
import com.jaimegc.covid19tracker.ui.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.states.ScreenState
import org.koin.android.viewmodel.ext.android.viewModel

class CountryFragment : Fragment(R.layout.fragment_country),
    BaseViewScreenState<CountryViewModel, PlaceStateScreen> {

    override val viewModel: CountryViewModel by viewModel()
    private val placeTotalAdapter = PlaceTotalAdapter()
    private val mergeAdapter = MergeAdapter()

    private lateinit var binding: FragmentCountryBinding
    private lateinit var loadingBinding: LoadingBinding
    private lateinit var menu: Menu
    private lateinit var countrySpinnerAdapter: CountrySpinnerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountryBinding.bind(view)
        loadingBinding = LoadingBinding.bind(view)

        binding.recyclerPlace.adapter = mergeAdapter

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                ScreenState.Loading -> if (binding.recyclerPlace.isEmpty()) loadingBinding.loading.show()
                is ScreenState.Render<PlaceStateScreen> -> {
                    loadingBinding.loading.hide()
                    handleRenderState(screenState.renderState)
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
                    renderState.data.indexOf(renderState.data.first { it.name == "Spain" })
                )

                binding.countrySpinner.onItemSelected { pos ->
                    viewModel.getCountryAndStatsByIdDate(countrySpinnerAdapter.getCountryId(pos))
                }
            }
            is PlaceStateScreen.SuccessPlaceStats -> {
                mergeAdapter.addAdapter(placeTotalAdapter)
                placeTotalAdapter.submitList(listOf(renderState.data))
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
                    menu.enableItem(MENU_ITEM_LIST)
                    viewModel.getCountryAndStatsByIdDate(countrySpinnerAdapter.getCountryId(
                        binding.countrySpinner.selectedItemId.toInt()
                    ))
                    mergeAdapter.removeAllAdapters()
                    true
                }
                R.id.bar_chart_view -> {
                    menu.enableItem(MENU_ITEM_BAR_CHART)
                    mergeAdapter.removeAllAdapters()
                    true
                }
                R.id.line_chart_view -> {
                    menu.enableItem(MENU_ITEM_LINE_CHART)
                    mergeAdapter.removeAllAdapters()
                    true
                }
                R.id.pie_chart_view -> {
                    menu.enableItem(MENU_ITEM_PIE_CHART)
                    mergeAdapter.removeAllAdapters()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else {
            super.onOptionsItemSelected(item)
        }

    companion object {
        private const val MENU_ITEM_LIST = 0
        private const val MENU_ITEM_BAR_CHART = 1
        private const val MENU_ITEM_LINE_CHART = 2
        private const val MENU_ITEM_PIE_CHART = 3
    }
}
