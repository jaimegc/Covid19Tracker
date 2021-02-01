package com.jaimegc.covid19tracker.fragment.barista

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.country.CountryFragment
import com.jaimegc.covid19tracker.util.UITest
import com.jaimegc.covid19tracker.matchers.RecyclerViewCompareSquareViewSizeMatcher.Companion.recyclerViewHasSameViewsSize
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Companion.recyclerViewHasAdapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Companion.recyclerViewHasItemCount
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Options
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.schibsted.spain.barista.interaction.BaristaSpinnerInteractions.clickSpinnerItem
import com.schibsted.spain.barista.internal.assertAnyView
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class CountryFragmentBaristaTest : UITest() {

    private lateinit var scenario: FragmentScenario<CountryFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun countryWithRegions_shouldShowCountryAndPlaceSpinners() {
        assertDisplayed(R.id.country_spinner)
        assertDisplayed(R.id.region_spinner)
    }

    @Test
    fun countryWithEmptyRegions_shouldShowOnlyCountrySpinner() {
        selectCountryWithEmptyRegions()

        assertDisplayed(R.id.country_spinner)
        assertNotDisplayed(R.id.region_spinner)
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuListView_shouldHaveAtLeastTwoItems() {
        clickMenu(R.id.list_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInBarChartView_shouldHaveAtLeastTwoItems() {
        clickMenu(R.id.bar_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInLineChartView_shouldHaveOneItem() {
        clickMenu(R.id.line_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 1)
    }

    @Test
    fun countryWithRegionsNoSelectedInPieChartView_shouldHaveAtLeastTwoItems() {
        clickMenu(R.id.pie_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuListView_shouldHaveAtLeastTwoItems() {
        selectRegionWithSubRegions()
        clickMenu(R.id.list_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun countryWithRegionSelectedInBarChartView_shouldHaveAtLeastTwoItems() {
        selectRegionWithSubRegions()
        clickMenu(R.id.bar_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun countryWithRegionSelectedInLineChartView_shouldHaveOneItem() {
        selectRegionWithSubRegions()
        clickMenu(R.id.line_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 1)
    }

    @Test
    fun countryWithRegionSelectedInPieChartView_shouldHaveAtLeastTwoItems() {
        selectRegionWithSubRegions()
        clickMenu(R.id.pie_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuListView_shouldHaveOneItem() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.list_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 1)
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInBarChartView_shouldHaveOneItem() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.bar_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 1)
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInLineChartView_shouldHaveEmptyItems() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.line_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 0)
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInPieChartView_shouldHaveOneItem() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.pie_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 1)
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuListView_shouldHaveOneItem() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.list_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 1)
    }

    @Test
    fun countryWithEmptyRegionsSelectedInBarChartView_shouldHaveOneItem() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.bar_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 1)
    }

    @Test
    fun countryWithEmptyRegionsSelectedInLineChartView_shouldHaveEmptyItems() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.line_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 0)
    }

    @Test
    fun countryWithEmptyRegionsSelectedInPieChartView_shouldHaveOneItem() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.pie_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_place, 1)
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        clickMenu(R.id.list_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartAdapters() {
        clickMenu(R.id.bar_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuLineChartView_shouldHavePlaceLineChartAdapter() {
        clickMenu(R.id.line_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_LINE_CHART))
        )
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuPieChartView_shouldHavePlaceTotalPieAndPlacePieChartAdapters() {
        clickMenu(R.id.pie_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART, Adapters.PLACE_PIE_CHART))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        selectRegionWithSubRegions()
        clickMenu(R.id.list_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartAdapters() {
        selectRegionWithSubRegions()
        clickMenu(R.id.bar_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuLineChartView_shouldHavePlaceLineChartAdapter() {
        selectRegionWithSubRegions()
        clickMenu(R.id.line_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_LINE_CHART))
        )
    }

    @Test
    fun countryWithRegionSelectedInMenuPieChartView_shouldHavePlaceTotalPieAndPlacePieChartAdapters() {
        selectRegionWithSubRegions()
        clickMenu(R.id.pie_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART, Adapters.PLACE_PIE_CHART))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuListView_shouldHavePlaceTotalAdapter() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.list_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuBarChartView_shouldHavePlaceTotalBarChartAdapter() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.bar_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuLineChartView_shouldHaveEmptyAdapter() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.line_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.EMPTY_ADAPTER))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuPieChartView_shouldHavePlaceTotalPieChartAdapter() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.pie_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuListView_shouldHavePlaceTotalAdapter() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.list_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuBarChartView_shouldHavePlaceTotalBarChartAdapter() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.bar_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuLineChartView_shouldHaveEmptyAdapter() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.line_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.EMPTY_ADAPTER))
        )
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuPieChartView_shouldHavePlaceTotalPieChartAdapter() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.pie_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART))
        )
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuLineChartView_shouldHaveEmptyDatabaseViews() {
        selectRegionWithEmptySubRegions()
        clickMenu(R.id.line_chart_view)

        assertDisplayed(R.id.empty_database_animation)
        assertDisplayed(R.id.empty_database_text)
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuLineChartView_shouldHaveEmptyDatabaseViews() {
        selectCountryWithEmptyRegions()
        clickMenu(R.id.line_chart_view)

        assertDisplayed(R.id.empty_database_animation)
        assertDisplayed(R.id.empty_database_text)
    }

    @Test
    fun clickOnPlaceAdapter_shouldExpandTheItem() {
        val viewsSizeExpanded = 60
        val placeAdapterPosition = 1

        scrollListToPosition(R.id.recycler_place, placeAdapterPosition)
        clickListItem(R.id.recycler_place, placeAdapterPosition)

        assertAnyView(displayedWithId(R.id.recycler_place),
            recyclerViewHasSameViewsSize(placeAdapterPosition, mapOf(
                R.id.ic_deaths to viewsSizeExpanded, R.id.ic_deaths to viewsSizeExpanded,
                R.id.ic_recovered to viewsSizeExpanded, R.id.ic_open_cases to viewsSizeExpanded)
            )
        )
    }

    private fun selectCountryWithEmptyRegions() {
        clickSpinnerItem(R.id.country_spinner, 0)
    }

    private fun selectRegionWithSubRegions() {
        clickSpinnerItem(R.id.region_spinner, 1)
    }

    private fun selectRegionWithEmptySubRegions() {
        clickSpinnerItem(R.id.region_spinner, 2)
    }
}