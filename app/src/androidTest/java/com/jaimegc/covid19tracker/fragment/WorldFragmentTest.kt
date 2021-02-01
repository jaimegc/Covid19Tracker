package com.jaimegc.covid19tracker.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.adapter.WorldCountryAdapter
import com.jaimegc.covid19tracker.ui.world.WorldFragment
import com.jaimegc.covid19tracker.util.UITest
import com.jaimegc.covid19tracker.matchers.RecyclerViewCompareSquareViewSizeMatcher.Companion.recyclerViewHasSameViewsSize
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Companion.recyclerViewHasAdapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Companion.recyclerViewHasItemCount
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Options
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class WorldFragmentTest : UITest() {

    private lateinit var scenario: FragmentScenario<WorldFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun worldInMenuListView_shouldHaveAtLeastTwoItems() {
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun worldInBarChartView_shouldHaveAtLeastTwoItems() {
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun worldInLineChartView_shouldHaveOneItem() {
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasItemCount(1))
        )
    }

    @Test
    fun worldInPieChartView_shouldHaveAtLeastTwoItems() {
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun worldInMenuListView_shouldHaveWorldAndWorldCountryAdapters() {
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.WORLD, Adapters.WORLD_COUNTRY)))
        )
    }

    @Test
    fun worldInMenuBarChartView_shouldHaveWorldBarAndWorldCountriesBarChartAdapters() {
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasAdapters(
                listOf(Adapters.WORLD_BAR_CHART, Adapters.WORLD_COUNTRIES_BAR_CHART))
            )
        )
    }

    @Test
    fun worldInMenuLineChartView_shouldHaveWorldLineChartAdapter() {
        onView(withId(R.id.line_chart_view)).perform(click())

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.WORLD_LINE_CHART)))
        )
    }

    @Test
    fun worldInMenuPieChartView_shouldHaveWorldPieAndWorldCountriesPieChartAdapters() {
        onView(withId(R.id.pie_chart_view)).perform(click())

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasAdapters(
                listOf(Adapters.WORLD_PIE_CHART, Adapters.WORLD_COUNTRIES_PIE_CHART))
            )
        )
    }

    @Test
    fun clickOnWorldCountryAdapter_shouldExpandTheItem() {
        val viewsSizeExpanded = 60
        val worldCountryAdapterPosition = 1

        onView(withId(R.id.recycler_world)).perform(
            scrollToPosition<WorldCountryAdapter.WorldCountryViewHolder>(worldCountryAdapterPosition),
            actionOnItemAtPosition<WorldCountryAdapter.WorldCountryViewHolder>(
                worldCountryAdapterPosition, click()
            )
        )

        onView(withId(R.id.recycler_world)).check(
            matches(recyclerViewHasSameViewsSize(worldCountryAdapterPosition, mapOf(
                R.id.ic_deaths to viewsSizeExpanded, R.id.ic_deaths to viewsSizeExpanded,
                R.id.ic_recovered to viewsSizeExpanded, R.id.ic_open_cases to viewsSizeExpanded))
            )
        )
    }
}