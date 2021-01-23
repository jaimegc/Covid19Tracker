package com.jaimegc.covid19tracker.fragment.screenrobot

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.world.WorldFragment
import com.jaimegc.covid19tracker.utils.UITest
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Options
import com.jaimegc.covid19tracker.utils.screenrobot.worldScreenRobot
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class WorldFragmenScreenRobotTest : UITest() {

    private lateinit var scenario: FragmentScenario<WorldFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun worldInMenuListView_shouldHaveAtLeastTwoItems() {
        worldScreenRobot {
            clickOnMenuItemListView()
            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun worldInBarChartView_shouldHaveAtLeastTwoItems() {
        worldScreenRobot {
            clickOnMenuItemBarChart()
            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun worldInLineChartView_shouldHaveOneItem() {
        worldScreenRobot {
            clickOnMenuItemLineChart()
            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun worldInPieChartView_shouldHaveAtLeastTwoItems() {
        worldScreenRobot {
            clickOnMenuItemPieChart()
            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun worldInMenuListView_shouldHaveWorldAndWorldCountryAdapters() {
        worldScreenRobot {
            clickOnMenuItemListView()
            checkRecyclerHasAdapters(listOf(Adapters.WORLD, Adapters.WORLD_COUNTRY))
        }
    }

    @Test
    fun worldInMenuBarChartView_shouldHaveWorldBarAndWorldCountriesBarChartAdapters() {
        worldScreenRobot {
            clickOnMenuItemBarChart()
            checkRecyclerHasAdapters(
                listOf(Adapters.WORLD_BAR_CHART, Adapters.WORLD_COUNTRIES_BAR_CHART)
            )
        }
    }

    @Test
    fun worldInMenuLineChartView_shouldHaveWorldLineChartAdapter() {
        worldScreenRobot {
            clickOnMenuItemLineChart()
            checkRecyclerHasAdapters(listOf(Adapters.WORLD_LINE_CHART))
        }
    }

    @Test
    fun worldInMenuPieChartView_shouldHaveWorldPieAndWorldCountriesPieChartAdapters() {
        worldScreenRobot {
            clickOnMenuItemPieChart()
            checkRecyclerHasAdapters(
                listOf(Adapters.WORLD_PIE_CHART, Adapters.WORLD_COUNTRIES_PIE_CHART)
            )
        }
    }

    @Test
    fun clickOnWorldCountryAdapter_shouldExpandTheItem() {
        worldScreenRobot {
            clickOnWorldCountryAdapter()
            checkWorldCountryItemExpand()
        }
    }
}