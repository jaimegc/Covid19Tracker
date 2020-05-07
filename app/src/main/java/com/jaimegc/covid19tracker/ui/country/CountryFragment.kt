package com.jaimegc.covid19tracker.ui.country

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.FragmentCountryBinding
import com.jaimegc.covid19tracker.ui.adapter.CountrySpinnerAdapter
import com.jaimegc.covid19tracker.ui.states.BaseViewScreenState
import com.jaimegc.covid19tracker.ui.states.CountryStateScreen
import com.jaimegc.covid19tracker.ui.states.ScreenState
import kotlinx.android.synthetic.main.fragment_country.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CountryFragment : Fragment(R.layout.fragment_country),
    BaseViewScreenState<CountryViewModel, CountryStateScreen> {

    override val viewModel: CountryViewModel by viewModel()

    private lateinit var binding: FragmentCountryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountryBinding.bind(view)

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                //ScreenState.Loading -> if (binding.recyclerWorld.isEmpty()) binding.loading.show()
                ScreenState.Loading -> Unit
                is ScreenState.Render<CountryStateScreen> -> {
                    //binding.loading.hide()
                    handleRenderState(screenState.renderState)
                }
            }
        })

        viewModel.getCountries()
    }

    override fun handleRenderState(renderState: CountryStateScreen) {
        when (renderState) {
            is CountryStateScreen.SuccessCountries -> {
                binding.countrySpinner.adapter =
                    CountrySpinnerAdapter(requireContext(), renderState.data)
                binding.countrySpinner.setSelection(
                    renderState.data.indexOf(renderState.data.first { it.name == "Spain" })
                )
            }
        }
    }
}
