package com.jaimegc.covid19tracker.ui.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.FragmentNotificationsBinding
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.ui.adapter.WorldTotalAdapter
import com.jaimegc.covid19tracker.ui.states.ScreenState
import com.jaimegc.covid19tracker.ui.states.ViewScreenState
import com.jaimegc.covid19tracker.ui.states.WorldTotalStateScreen
import org.koin.android.viewmodel.ext.android.viewModel

class NotificationsFragment : Fragment(R.layout.fragment_notifications),
    ViewScreenState<WorldTotalStateScreen, NotificationsViewModel> {

    override val viewModel: NotificationsViewModel by viewModel()
    private val worldTotalAdapter = WorldTotalAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNotificationsBinding.bind(view)

        binding.recyclerWorld.adapter = worldTotalAdapter

        viewModel.covidTracker.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Success -> worldTotalAdapter.submitList(listOf(state.data.total))
            }
        })

        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                ScreenState.Loading -> { }
                is ScreenState.Render<WorldTotalStateScreen> ->
                    handleRenderState(screenState.renderState)
            }
        })

        viewModel.getCovidTrackerLast()
    }

    override fun handleRenderState(renderState: WorldTotalStateScreen) {
        when (renderState) {
            is WorldTotalStateScreen.Success ->
                worldTotalAdapter.submitList(listOf(renderState.data.total))
        }
    }
}
