package com.jaimegc.covid19tracker.fragment.barista

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.world.WorldFragment
import com.jaimegc.covid19tracker.util.UITest
import com.jaimegc.covid19tracker.matchers.RecyclerViewCompareSquareViewSizeMatcher.Companion.recyclerViewHasSameViewsSize
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Companion.recyclerViewHasAdapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Companion.recyclerViewHasItemCount
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Options
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.schibsted.spain.barista.internal.assertAnyView
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class WorldFragmentBaristaTest : UITest() {

    private lateinit var scenario: FragmentScenario<WorldFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun worldInMenuListView_shouldHaveAtLeastTwoItems() {
        clickMenu(R.id.list_view)

        assertAnyView(displayedWithId(R.id.recycler_world),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun worldInBarChartView_shouldHaveAtLeastTwoItems() {
        clickMenu(R.id.bar_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_world),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun worldInLineChartView_shouldHaveOneItem() {
        clickMenu(R.id.line_chart_view)

        assertRecyclerViewItemCount(R.id.recycler_world, 1)
    }

    @Test
    fun worldInPieChartView_shouldHaveAtLeastTwoItems() {
        clickMenu(R.id.pie_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_world),
            recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        )
    }

    @Test
    fun worldInMenuListView_shouldHaveWorldAndWorldCountryAdapters() {
        clickMenu(R.id.list_view)

        assertAnyView(displayedWithId(R.id.recycler_world),
            recyclerViewHasAdapters(listOf(Adapters.WORLD, Adapters.WORLD_COUNTRY))
        )
    }

    @Test
    fun worldInMenuBarChartView_shouldHaveWorldBarAndWorldCountriesBarChartAdapters() {
        clickMenu(R.id.bar_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_world),
            recyclerViewHasAdapters(
                listOf(Adapters.WORLD_BAR_CHART, Adapters.WORLD_COUNTRIES_BAR_CHART)
            )
        )
    }

    @Test
    fun worldInMenuLineChartView_shouldHaveWorldLineChartAdapter() {
        clickMenu(R.id.line_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_world),
            recyclerViewHasAdapters(listOf(Adapters.WORLD_LINE_CHART))
        )
    }

    @Test
    fun worldInMenuPieChartView_shouldHaveWorldPieAndWorldCountriesPieChartAdapters() {
        clickMenu(R.id.pie_chart_view)

        assertAnyView(displayedWithId(R.id.recycler_world),
            recyclerViewHasAdapters(
                listOf(Adapters.WORLD_PIE_CHART, Adapters.WORLD_COUNTRIES_PIE_CHART)
            )
        )
    }

    @Test
    fun clickOnWorldCountryAdapter_shouldExpandTheItem() {
        val viewsSizeExpanded = 60
        val worldCountryAdapterPosition = 1

        scrollListToPosition(R.id.recycler_world, worldCountryAdapterPosition)
        clickListItem(R.id.recycler_world, worldCountryAdapterPosition)

        assertAnyView(displayedWithId(R.id.recycler_world),
            recyclerViewHasSameViewsSize(worldCountryAdapterPosition, mapOf(
                R.id.ic_deaths to viewsSizeExpanded, R.id.ic_deaths to viewsSizeExpanded,
                R.id.ic_recovered to viewsSizeExpanded, R.id.ic_open_cases to viewsSizeExpanded)
            )
        )
    }
}