package com.jaimegc.covid19tracker.fragment.kakao

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.world.WorldFragment
import com.jaimegc.covid19tracker.util.UITest
import com.jaimegc.covid19tracker.util.kakao.WorldScreen
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
class WorldFragmentKakaoTest : UITest() {

    private lateinit var scenario: FragmentScenario<WorldFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun worldInMenuListView_shouldHaveAtLeastTwoItems() {
        onScreen<WorldScreen> {
            menuItemListView { click() }
            recyclerWorld {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun worldInBarChartView_shouldHaveAtLeastTwoItems() {
        onScreen<WorldScreen> {
            menuItemBarChart { click() }
            recyclerWorld {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun worldInLineChartView_shouldHaveOneItem() {
        onScreen<WorldScreen> {
            menuItemLineChart { click() }
            recyclerWorld { hasSize(1) }
        }
    }

    @Test
    fun worldInPieChartView_shouldHaveAtLeastTwoItems() {
        onScreen<WorldScreen> {
            menuItemPieChart { click() }
            recyclerWorld {
                view.check(
                    matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
                )
            }
        }
    }

    @Test
    fun worldInMenuListView_shouldHaveWorldAndWorldCountryAdapters() {
        onScreen<WorldScreen> {
            menuItemListView { click() }
            recyclerWorld {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.WORLD, Adapters.WORLD_COUNTRY)))
                )
            }
        }
    }

    @Test
    fun worldInMenuBarChartView_shouldHaveWorldBarAndWorldCountriesBarChartAdapters() {
        onScreen<WorldScreen> {
            menuItemBarChart { click() }
            recyclerWorld {
                view.check(
                    matches(recyclerViewHasAdapters(
                        listOf(Adapters.WORLD_BAR_CHART, Adapters.WORLD_COUNTRIES_BAR_CHART))
                    )
                )
            }
        }
    }

    @Test
    fun worldInMenuLineChartView_shouldHaveWorldLineChartAdapter() {
        onScreen<WorldScreen> {
            menuItemLineChart { click() }
            recyclerWorld {
                view.check(
                    matches(recyclerViewHasAdapters(listOf(Adapters.WORLD_LINE_CHART)))
                )
            }
        }
    }

    @Test
    fun worldInMenuPieChartView_shouldHaveWorldPieAndWorldCountriesPieChartAdapters() {
        onScreen<WorldScreen> {
            menuItemPieChart { click() }
            recyclerWorld {
                view.check(
                    matches(recyclerViewHasAdapters(
                        listOf(Adapters.WORLD_PIE_CHART, Adapters.WORLD_COUNTRIES_PIE_CHART))
                    )
                )
            }
        }
    }

    @Test
    fun clickOnWorldCountryAdapter_shouldExpandTheItem() {
        val viewsSizeExpanded = 60
        val worldCountryAdapterPosition = 1

        onScreen<WorldScreen> {
            recyclerWorld {
                perform { scrollTo(worldCountryAdapterPosition) }
                childAt<WorldScreen.RecyclerWorldItem>(worldCountryAdapterPosition) {
                    click()
                }
                view.check(
                    matches(recyclerViewHasSameViewsSize(worldCountryAdapterPosition, mapOf(
                        R.id.ic_deaths to viewsSizeExpanded, R.id.ic_deaths to viewsSizeExpanded,
                        R.id.ic_recovered to viewsSizeExpanded, R.id.ic_open_cases to viewsSizeExpanded))
                    )
                )
            }
        }
    }
}