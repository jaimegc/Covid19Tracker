package com.jaimegc.covid19tracker.fragment.screenrobot

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.country.CountryFragment
import com.jaimegc.covid19tracker.utils.UITest
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Options
import com.jaimegc.covid19tracker.utils.screenrobot.countryScreenRobot
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CountryFragmentScreenRobotTest : UITest() {

    private lateinit var scenario: FragmentScenario<CountryFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun countryWithRegions_shouldShowCountryAndPlaceSpinners() {
        countryScreenRobot {
            countrySpinnerIsDisplayed()
            regionSpinnerIsDisplayed()
        }
    }

    @Test
    fun countryWithEmptyRegions_shouldShowOnlyCountrySpinner() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()

            countrySpinnerIsDisplayed()
            regionSpinnerIsNotDisplayed()    
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuListView_shouldHaveAtLeastTwoItems() {
        countryScreenRobot {
            clickOnMenuItemListView()

            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInBarChartView_shouldHaveAtLeastTwoItems() {
        countryScreenRobot {
            clickOnMenuItemBarChart()

            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInLineChartView_shouldHaveOneItem() {
        countryScreenRobot {
            clickOnMenuItemLineChart()

            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInPieChartView_shouldHaveAtLeastTwoItems() {
        countryScreenRobot {
            clickOnMenuItemPieChart()

            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuListView_shouldHaveAtLeastTwoItems() {
        countryScreenRobot {
            selectRegionWithSubRegions()
            clickOnMenuItemListView()

            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun countryWithRegionSelectedInBarChartView_shouldHaveAtLeastTwoItems() {
        countryScreenRobot {
            selectRegionWithSubRegions()
            clickOnMenuItemBarChart()
    
            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun countryWithRegionSelectedInLineChartView_shouldHaveOneItem() {
        countryScreenRobot {
            selectRegionWithSubRegions()
            clickOnMenuItemLineChart()
    
            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun countryWithRegionSelectedInPieChartView_shouldHaveAtLeastTwoItems() {
        countryScreenRobot {
            selectRegionWithSubRegions()
            clickOnMenuItemPieChart()

            checkRecyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL)
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuListView_shouldHaveOneItem() {
        countryScreenRobot {
            selectRegionWithEmptySubRegions()
            clickOnMenuItemListView()

            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInBarChartView_shouldHaveOneItem() {
        countryScreenRobot {
            selectRegionWithEmptySubRegions()
            clickOnMenuItemBarChart()

            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInLineChartView_shouldHaveEmptyItems() {
        countryScreenRobot {
            selectRegionWithEmptySubRegions()
            clickOnMenuItemLineChart()

            checkRecyclerViewHasItemCount(0)
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInPieChartView_shouldHaveOneItem() {
        countryScreenRobot {
            selectRegionWithEmptySubRegions()
            clickOnMenuItemPieChart()

            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuListView_shouldHaveOneItem() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemListView()

            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInBarChartView_shouldHaveOneItem() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemBarChart()

            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInLineChartView_shouldHaveEmptyItems() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemLineChart()

            checkRecyclerViewHasItemCount(0)
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInPieChartView_shouldHaveOneItem() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemPieChart()

            checkRecyclerViewHasItemCount(1)
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        countryScreenRobot {
            clickOnMenuItemListView()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE))
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartAdapters() {
        countryScreenRobot {
            clickOnMenuItemBarChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART))
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuLineChartView_shouldHavePlaceLineChartAdapter() {
        countryScreenRobot {
            clickOnMenuItemLineChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_LINE_CHART))
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuPieChartView_shouldHavePlaceTotalPieAndPlacePieChartAdapters() {
        countryScreenRobot {
            clickOnMenuItemPieChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART, Adapters.PLACE_PIE_CHART))
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        countryScreenRobot {
            selectRegionWithSubRegions()
            clickOnMenuItemListView()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE))
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartAdapters() {
        countryScreenRobot {
            selectRegionWithSubRegions()
            clickOnMenuItemBarChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART))
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuLineChartView_shouldHavePlaceLineChartAdapter() {
        countryScreenRobot {
            selectRegionWithSubRegions()
            clickOnMenuItemLineChart()
    
            checkRecyclerHasAdapters(listOf(Adapters.PLACE_LINE_CHART))
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuPieChartView_shouldHavePlaceTotalPieAndPlacePieChartAdapters() {
        countryScreenRobot {
            selectRegionWithSubRegions()
            clickOnMenuItemPieChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART, Adapters.PLACE_PIE_CHART))
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuListView_shouldHavePlaceTotalAdapter() {
        countryScreenRobot {
            selectRegionWithEmptySubRegions()
            clickOnMenuItemListView()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL))
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuBarChartView_shouldHavePlaceTotalBarChartAdapter() {
        countryScreenRobot {
           selectRegionWithEmptySubRegions()
            clickOnMenuItemBarChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART))
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuLineChartView_shouldHaveEmptyAdapter() {
        countryScreenRobot {
            selectRegionWithEmptySubRegions()
            clickOnMenuItemLineChart()

            checkRecyclerHasAdapters(listOf(Adapters.EMPTY_ADAPTER))
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuPieChartView_shouldHavePlaceTotalPieChartAdapter() {
        countryScreenRobot {
            selectRegionWithEmptySubRegions()
            clickOnMenuItemPieChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART))
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuListView_shouldHavePlaceTotalAdapter() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemListView()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL))
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuBarChartView_shouldHavePlaceTotalBarChartAdapter() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemBarChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART))
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuLineChartView_shouldHaveEmptyAdapter() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemLineChart()

            checkRecyclerHasAdapters(listOf(Adapters.EMPTY_ADAPTER))
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuPieChartView_shouldHavePlaceTotalPieChartAdapter() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemPieChart()

            checkRecyclerHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART))
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuLineChartView_shouldHaveEmptyDatabaseViews() {
        countryScreenRobot {
            selectRegionWithEmptySubRegions()
            clickOnMenuItemLineChart()

            emptyDatabaseAnimationIsDisplayed()
            emptyDatabaseTextIsDisplayed()
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuLineChartView_shouldHaveEmptyDatabaseViews() {
        countryScreenRobot {
            selectCountryWithEmptyRegions()
            clickOnMenuItemLineChart()

            emptyDatabaseAnimationIsDisplayed()
            emptyDatabaseTextIsDisplayed()
        }
    }

    @Test
    fun clickOnPlaceAdapter_shouldExpandTheItem() {
        countryScreenRobot {
            clickOnPlaceAdapter()
            checkPlaceItemExpand()
        }
    }
}