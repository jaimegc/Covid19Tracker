package com.jaimegc.covid19tracker.fragment.kakao

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.agoda.kakao.spinner.KSpinnerItem
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.country.CountryFragment
import com.jaimegc.covid19tracker.util.UITest
import com.jaimegc.covid19tracker.util.kakao.CountryScreen
import com.jaimegc.covid19tracker.matchers.RecyclerViewCompareSquareViewSizeMatcher.Companion.recyclerViewHasSameViewsSize
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Companion.recyclerViewHasAdapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Companion.recyclerViewHasItemCount
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Options
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CountryFragmentKakaoTest : UITest() {

    private lateinit var scenario: FragmentScenario<CountryFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun countryWithRegions_shouldShowCountryAndPlaceSpinners() {
        onScreen<CountryScreen> {
            spinnerCountry { isDisplayed() }
            spinnerRegion { isDisplayed() }
        }
    }

    @Test
    fun countryWithEmptyRegions_shouldShowOnlyCountrySpinner() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            spinnerCountry { isDisplayed() }
            spinnerRegion { isNotDisplayed() }
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuListView_shouldHaveAtLeastTwoItems() {
        onScreen<CountryScreen> {
            menuItemListView { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInBarChartView_shouldHaveAtLeastTwoItems() {
        onScreen<CountryScreen> {
            menuItemBarChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInLineChartView_shouldHaveOneItem() {
        onScreen<CountryScreen> {
            menuItemLineChart { click() }
            recyclerPlace { hasSize(1) }
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInPieChartView_shouldHaveAtLeastTwoItems() {
        onScreen<CountryScreen> {
            menuItemPieChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuListView_shouldHaveAtLeastTwoItems() {
        onScreen<CountryScreen> {
            selectRegionWithSubRegions(this)
            menuItemListView { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun countryWithRegionSelectedInBarChartView_shouldHaveAtLeastTwoItems() {
        onScreen<CountryScreen> {
            selectRegionWithSubRegions(this)
            menuItemBarChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun countryWithRegionSelectedInLineChartView_shouldHaveOneItem() {
        onScreen<CountryScreen> {
            selectRegionWithSubRegions(this)
            menuItemLineChart { click() }
            recyclerPlace { hasSize(1) }
        }
    }

    @Test
    fun countryWithRegionSelectedInPieChartView_shouldHaveAtLeastTwoItems() {
        onScreen<CountryScreen> {
            selectRegionWithSubRegions(this)
            menuItemPieChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuListView_shouldHaveOneItem() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemListView { click() }
            recyclerPlace { hasSize(1) }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInBarChartView_shouldHaveOneItem() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemBarChart { click() }
            recyclerPlace { hasSize(1) }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInLineChartView_shouldHaveEmptyItems() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemLineChart { click() }
            recyclerPlace { hasSize(0) }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInPieChartView_shouldHaveOneItem() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemPieChart { click() }
            recyclerPlace { hasSize(1) }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuListView_shouldHaveOneItem() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemListView { click() }
            recyclerPlace { hasSize(1) }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInBarChartView_shouldHaveOneItem() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemBarChart { click() }
            recyclerPlace { hasSize(1) }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInLineChartView_shouldHaveEmptyItems() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemLineChart { click() }
            recyclerPlace { hasSize(0) }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInPieChartView_shouldHaveOneItem() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemPieChart { click() }
            recyclerPlace { hasSize(1) }
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        onScreen<CountryScreen> {
            menuItemListView { click() }
            recyclerPlace {
                view.check(
                    matches(
                        recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE))
                    )
                )
            }
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartAdapters() {
        onScreen<CountryScreen> {
            menuItemBarChart { click() }
            recyclerPlace {
                view.check(
                    matches(
                        recyclerViewHasAdapters(
                            listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART)
                        )
                    )
                )
            }
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuLineChartView_shouldHavePlaceLineChartAdapter() {
        onScreen<CountryScreen> {
            menuItemLineChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_LINE_CHART)))
                )
            }
        }
    }

    @Test
    fun countryWithRegionsNoSelectedInMenuPieChartView_shouldHavePlaceTotalPieAndPlacePieChartAdapters() {
        onScreen<CountryScreen> {
            menuItemPieChart { click() }
            recyclerPlace {
                view.check(
                    matches(
                        recyclerViewHasAdapters(
                            listOf(Adapters.PLACE_TOTAL_PIE_CHART, Adapters.PLACE_PIE_CHART)
                        )
                    )
                )
            }
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        onScreen<CountryScreen> {
            selectRegionWithSubRegions(this)
            menuItemListView { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE)))
                )
            }
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartAdapters() {
        onScreen<CountryScreen> {
            selectRegionWithSubRegions(this)
            menuItemBarChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(
                        listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART))
                    )
                )
            }
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuLineChartView_shouldHavePlaceLineChartAdapter() {
        onScreen<CountryScreen> {
            selectRegionWithSubRegions(this)
            menuItemLineChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_LINE_CHART)))
                )
            }
        }
    }

    @Test
    fun countryWithRegionSelectedInMenuPieChartView_shouldHavePlaceTotalPieAndPlacePieChartAdapters() {
        onScreen<CountryScreen> {
            selectRegionWithSubRegions(this)
            menuItemPieChart { click() }
            recyclerPlace {
                view.check(
                    matches(
                        recyclerViewHasAdapters(
                            listOf(Adapters.PLACE_TOTAL_PIE_CHART, Adapters.PLACE_PIE_CHART)
                        )
                    )
                )
            }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuListView_shouldHavePlaceTotalAdapter() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemListView { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL)))
                )
            }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuBarChartView_shouldHavePlaceTotalBarChartAdapter() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemBarChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART)))
                )
            }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuLineChartView_shouldHaveEmptyAdapter() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemLineChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.EMPTY_ADAPTER)))
                )
            }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuPieChartView_shouldHavePlaceTotalPieChartAdapter() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemPieChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART)))
                )
            }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuListView_shouldHavePlaceTotalAdapter() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemListView { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL)))
                )
            }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuBarChartView_shouldHavePlaceTotalBarChartAdapter() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemBarChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART)))
                )
            }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuLineChartView_shouldHaveEmptyAdapter() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemLineChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.EMPTY_ADAPTER)))
                )
            }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuPieChartView_shouldHavePlaceTotalPieChartAdapter() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemPieChart { click() }
            recyclerPlace {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_PIE_CHART)))
                )
            }
        }
    }

    @Test
    fun countryWithRegionWithEmptySubRegionsSelectedInMenuLineChartView_shouldHaveEmptyDatabaseViews() {
        onScreen<CountryScreen> {
            selectRegionWithEmptySubRegions(this)
            menuItemLineChart { click() }
            emptyDatabaseAnimation { isDisplayed() }
            emptyDatabaseText { isDisplayed() }
        }
    }

    @Test
    fun countryWithEmptyRegionsSelectedInMenuLineChartView_shouldHaveEmptyDatabaseViews() {
        onScreen<CountryScreen> {
            selectCountryWithEmptyRegions(this)
            menuItemLineChart { click() }
            emptyDatabaseAnimation { isDisplayed() }
            emptyDatabaseText { isDisplayed() }
        }
    }

    @Test
    fun clickOnPlaceAdapter_shouldExpandTheItem() {
        val viewsSizeExpanded = 60
        val placeAdapterPosition = 1

        onScreen<CountryScreen> {
            recyclerPlace {
                perform {
                    scrollTo(placeAdapterPosition)
                }
                childAt<CountryScreen.RecyclerCountryItem>(placeAdapterPosition) {
                    click()
                }
                view.check(
                    matches(recyclerViewHasSameViewsSize(placeAdapterPosition, mapOf(
                        R.id.ic_deaths to viewsSizeExpanded, R.id.ic_deaths to viewsSizeExpanded,
                        R.id.ic_recovered to viewsSizeExpanded, R.id.ic_open_cases to viewsSizeExpanded))
                    )
                )
            }
        }
    }

    private fun selectCountryWithEmptyRegions(countryScreen: CountryScreen) {
        countryScreen.spinnerCountry {
            open()
            firstChild<KSpinnerItem> { click() }
        }
    }

    private fun selectRegionWithSubRegions(countryScreen: CountryScreen) {
        countryScreen.spinnerRegion {
            open()
            childAt<KSpinnerItem>(1) { click() }
        }
    }

    private fun selectRegionWithEmptySubRegions(countryScreen: CountryScreen) {
        countryScreen.spinnerRegion {
            open()
            childAt<KSpinnerItem>(2) { click() }
        }
    }
}