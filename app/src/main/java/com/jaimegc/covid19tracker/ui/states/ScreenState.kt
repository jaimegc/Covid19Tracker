package com.jaimegc.covid19tracker.ui.states

import com.jaimegc.covid19tracker.ui.model.CovidTrackerUI

sealed class ScreenState<out T : BaseScreenState> {
    object Loading : ScreenState<Nothing>()
    class Render<out T : BaseScreenState>(val renderState: T) : ScreenState<T>()
}

sealed class WorldTotalStateScreen : BaseScreenState() {
    class Success(val data: CovidTrackerUI) : WorldTotalStateScreen()
}

sealed class BaseScreenState