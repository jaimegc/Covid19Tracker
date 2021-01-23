package com.jaimegc.covid19tracker.matchers

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.ui.adapter.PlaceAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceLineChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlacePieChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceTotalAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceTotalBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.PlaceTotalPieChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountriesBarChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountriesPieChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldCountryAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldLineChartAdapter
import com.jaimegc.covid19tracker.ui.adapter.WorldPieChartAdapter
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class RecyclerViewConcatAdapterMatcher(
    private val adapters: List<Adapters>
) : BaseMatcher<View>() {

    companion object {
        fun recyclerViewHasAdapters(adapters: List<Adapters>): Matcher<View> =
            RecyclerViewConcatAdapterMatcher(adapters)
    }

    enum class Adapters {
        PLACE_TOTAL, PLACE, PLACE_TOTAL_BAR_CHART, PLACE_BAR_CHART, PLACE_TOTAL_PIE_CHART,
        PLACE_PIE_CHART, PLACE_LINE_CHART, WORLD, WORLD_COUNTRY, WORLD_BAR_CHART, WORLD_LINE_CHART,
        WORLD_PIE_CHART, WORLD_COUNTRIES_BAR_CHART, WORLD_COUNTRIES_PIE_CHART, EMPTY_ADAPTER
    }

    override fun matches(item: Any): Boolean =
        (item as RecyclerView).adapter?.let { adapter ->
            if (adapter is ConcatAdapter && adapter.adapters.size > 0 && adapter.adapters.size == adapters.size) {
                var containsAllAdapters = true
                adapter.adapters.forEachIndexed { index, adapt ->
                    if (containsAllAdapters) {
                        containsAllAdapters = when (adapters[index]) {
                            Adapters.PLACE_TOTAL -> adapt is PlaceTotalAdapter
                            Adapters.PLACE -> adapt is PlaceAdapter
                            Adapters.PLACE_TOTAL_BAR_CHART -> adapt is PlaceTotalBarChartAdapter
                            Adapters.PLACE_BAR_CHART -> adapt is PlaceBarChartAdapter
                            Adapters.PLACE_TOTAL_PIE_CHART -> adapt is PlaceTotalPieChartAdapter
                            Adapters.PLACE_PIE_CHART -> adapt is PlacePieChartAdapter
                            Adapters.PLACE_LINE_CHART -> adapt is PlaceLineChartAdapter
                            Adapters.WORLD -> adapt is WorldAdapter
                            Adapters.WORLD_COUNTRY -> adapt is WorldCountryAdapter
                            Adapters.WORLD_BAR_CHART -> adapt is WorldBarChartAdapter
                            Adapters.WORLD_LINE_CHART -> adapt is WorldLineChartAdapter
                            Adapters.WORLD_PIE_CHART -> adapt is WorldPieChartAdapter
                            Adapters.WORLD_COUNTRIES_BAR_CHART -> adapt is WorldCountriesBarChartAdapter
                            Adapters.WORLD_COUNTRIES_PIE_CHART -> adapt is WorldCountriesPieChartAdapter
                            Adapters.EMPTY_ADAPTER -> false
                        }
                    }
                }
                containsAllAdapters
            } else if (adapter.itemCount == 0) {
                adapters.firstOrNull { it == Adapters.EMPTY_ADAPTER } != null
            } else {
                false
            }
        } ?: false

    override fun describeTo(description: Description) {
        description.appendText("concat adapters do not contains correct adapter")
    }
}