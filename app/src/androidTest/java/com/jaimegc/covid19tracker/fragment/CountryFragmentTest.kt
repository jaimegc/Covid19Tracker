package com.jaimegc.covid19tracker.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEmptyRegionsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEmptySubRegionsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEntity
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.adapter.PlaceAdapter
import com.jaimegc.covid19tracker.ui.country.CountryFragment
import com.jaimegc.covid19tracker.utils.UITest
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewCompareSquareViewSizeMatcher.Companion.recyclerViewHasSameViewsSize
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewConcatAdapterMatcher.Companion.recyclerViewHasAdapters
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewItemsCountMatcher.Companion.recyclerViewHasItemCount
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewItemsCountMatcher.Options
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CountryFragmentTest : UITest() {

    private lateinit var scenario: FragmentScenario<CountryFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun countryWithRegions_shouldShowCountryAndPlaceSpinners() {
        onView(withId(R.id.country_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.region_spinner)).check(matches(isDisplayed()))
    }

    @Test
    fun countryWithEmptyRegions_shouldShowOnlyCountrySpinner() {
        selectCountryWithEmptyRegions()

        onView(withId(R.id.country_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.region_spinner)).check(matches(not(isDisplayed())))
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuListView_shouldHaveAtLeastTwoItems() {
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInBarChartView_shouldHaveAtLeastTwoItems() {
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInLineChartView_shouldHaveOneItem() {
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInPieChartView_shouldHaveAtLeastTwoItems() {
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuListView_shouldHaveAtLeastTwoItems() {
        selectRegionWithSubRegions()
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun countryWithRegionSelectedInBarChartView_shouldHaveAtLeastTwoItems() {
        selectRegionWithSubRegions()
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun countryWithRegionSelectedInLineChartView_shouldHaveOneItem() {
        selectRegionWithSubRegions()
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun countryWithRegionSelectedInPieChartView_shouldHaveAtLeastTwoItems() {
        selectRegionWithSubRegions()
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuListView_shouldHaveOneItem() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInBarChartView_shouldHaveOneItem() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInLineChartView_shouldHaveEmptyItems() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(0))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInPieChartView_shouldHaveOneItem() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuListView_shouldHaveOneItem() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInBarChartView_shouldHaveOneItem() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInLineChartView_shouldHaveEmptyItems() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(0))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInPieChartView_shouldHaveOneItem() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE)))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartAdapters() {
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART)))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuLineChartView_shouldHavePlaceLineChartAdapter() {
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_LINE_CHART)))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuPieChartView_shouldHavePlaceTotalPieAndPlacePieChartAdapters() {
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART, Adapters.PLACE_PIE_CHART)))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        selectRegionWithSubRegions()
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE)))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartAdapters() {
        selectRegionWithSubRegions()
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART)))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuLineChartView_shouldHavePlaceLineChartAdapter() {
        selectRegionWithSubRegions()
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_LINE_CHART)))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuPieChartView_shouldHavePlaceTotalPieAndPlacePieChartAdapters() {
        selectRegionWithSubRegions()
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART, Adapters.PLACE_PIE_CHART)))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuListView_shouldHavePlaceTotalAdapter() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL)))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuBarChartView_shouldHavePlaceTotalBarChartAdapter() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART)))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuLineChartView_shouldHaveEmptyAdapter() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.EMPTY_ADAPTER)))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuPieChartView_shouldHavePlaceTotalPieChartAdapter() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART)))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuListView_shouldHavePlaceTotalAdapter() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL)))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuBarChartView_shouldHavePlaceTotalBarChartAdapter() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART)))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuLineChartView_shouldHaveEmptyAdapter() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.EMPTY_ADAPTER)))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuPieChartView_shouldHavePlaceTotalPieChartAdapter() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART)))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInLineChartView_shouldHaveEmptyDatabaseViews() {
        selectRegionWithEmptySubRegions()
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.empty_database_animation)).check(matches(isDisplayed()))
        onView(withId(R.id.empty_database_text)).check(matches(isDisplayed()))
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuLineChartView_shouldHaveEmptyDatabaseViews() {
        selectCountryWithEmptyRegions()
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.empty_database_animation)).check(matches(isDisplayed()))
        onView(withId(R.id.empty_database_text)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnPlaceAdapter_shouldExpandTheItem() {
        val viewsSizeExpanded = 60
        val placeAdapterPosition = 1

        onView(withId(R.id.recycler_place)).perform(
            scrollToPosition<PlaceAdapter.PlaceStatsViewHolder>(placeAdapterPosition),
            actionOnItemAtPosition<PlaceAdapter.PlaceStatsViewHolder>(placeAdapterPosition, click()),
        )

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasSameViewsSize(placeAdapterPosition, mapOf(
                R.id.ic_deaths to viewsSizeExpanded, R.id.ic_deaths to viewsSizeExpanded,
                R.id.ic_recovered to viewsSizeExpanded, R.id.ic_open_cases to viewsSizeExpanded))
            )
        )
    }

    private fun selectCountryWithEmptyRegions() {
        onView(withId(R.id.country_spinner)).perform(click())
        onView(withText(countryEmptyRegionsEntity.name)).perform(click())
    }

    private fun selectRegionWithSubRegions() {
        onView(withId(R.id.region_spinner)).perform(click())
        onView(withText(regionEntity.name)).perform(click())
    }

    private fun selectRegionWithEmptySubRegions() {
        onView(withId(R.id.region_spinner)).perform(click())
        onView(withText(regionEmptySubRegionsEntity.name)).perform(click())
    }
}