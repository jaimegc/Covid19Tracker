package com.jaimegc.covid19tracker.ui.notifications

import android.os.Bundle
import android.view.View
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.FragmentWorldBinding
import com.jaimegc.covid19tracker.extensions.hide
import com.jaimegc.covid19tracker.extensions.show
import com.jaimegc.covid19tracker.ui.adapter.WorldAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountryAdapter
import com.jaimegc.covid19tracker.ui.states.ScreenState
import com.jaimegc.covid19tracker.ui.states.BaseViewScreenState
import com.jaimegc.covid19tracker.ui.states.WorldTotalStateScreen
import org.koin.android.viewmodel.ext.android.viewModel

class WorldFragment : Fragment(R.layout.fragment_world),
    BaseViewScreenState<WorldViewModel, WorldTotalStateScreen> {

    override val viewModel: WorldViewModel by viewModel()
    private val worldAdapter = WorldAdapter()
    private val worldCountryAdapter = WorldCountryAdapter()
    private val mergeAdapter = MergeAdapter(worldAdapter, worldCountryAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentWorldBinding.bind(view)

        binding.recyclerWorld.adapter = mergeAdapter

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                ScreenState.Loading -> if (binding.recyclerWorld.isEmpty()) binding.loading.show()
                is ScreenState.Render<WorldTotalStateScreen> -> {
                    binding.loading.hide()
                    binding.swipeRefreshLayout.isRefreshing = false
                    handleRenderState(screenState.renderState)
                }
            }
        })

        viewModel.getCovidTrackerLast()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCovidTrackerLast()
        }
    }

    override fun handleRenderState(renderState: WorldTotalStateScreen) {
        when (renderState) {
            is WorldTotalStateScreen.Success -> {
                worldAdapter.submitList(listOf(renderState.data.worldStats))
                worldCountryAdapter.submitList(renderState.data.countriesStats)
            }
        }
    }
}
