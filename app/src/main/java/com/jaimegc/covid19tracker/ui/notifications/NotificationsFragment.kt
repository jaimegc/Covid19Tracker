package com.jaimegc.covid19tracker.ui.notifications

import android.os.Bundle
import android.view.View
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.FragmentNotificationsBinding
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.extensions.hide
import com.jaimegc.covid19tracker.extensions.show
import com.jaimegc.covid19tracker.ui.adapter.WorldTotalAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldTotalCountryAdapter
import com.jaimegc.covid19tracker.ui.states.ScreenState
import com.jaimegc.covid19tracker.ui.states.BaseViewScreenState
import com.jaimegc.covid19tracker.ui.states.WorldTotalStateScreen
import org.koin.android.viewmodel.ext.android.viewModel

class NotificationsFragment : Fragment(R.layout.fragment_notifications),
    BaseViewScreenState<NotificationsViewModel, WorldTotalStateScreen> {

    override val viewModel: NotificationsViewModel by viewModel()
    private val worldTotalAdapter = WorldTotalAdapter()
    private val worldTotalCountryAdapter = WorldTotalCountryAdapter()
    private val mergeAdapter = MergeAdapter(worldTotalAdapter, worldTotalCountryAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNotificationsBinding.bind(view)

        binding.recyclerWorld.adapter = mergeAdapter

        viewModel.covidTracker.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Success -> worldTotalAdapter.submitList(listOf(state.data.total))
            }
        })

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
                worldTotalAdapter.submitList(listOf(renderState.data.total))
                worldTotalCountryAdapter.submitList(renderState.data.dates.first().countries)
            }
        }
    }
}
