package com.jaimegc.covid19tracker.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import arrow.core.Either
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEmptyRegionsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.countryStatsEmptyRegionsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.countryStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStatsEntity
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ScreenStateFactoryTest
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.home.MainActivity
import com.jaimegc.covid19tracker.utils.FileUtils
import com.jaimegc.covid19tracker.utils.SharedPreferencesTest
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewConcatAdapterMatcher.Companion.recyclerViewHasAdapters
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewItemsCountMatcher.Companion.recyclerViewHasItemCount
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewItemsCountMatcher.Options
import com.jaimegc.covid19tracker.utils.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest : KoinTest, SharedPreferencesTest() {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var mockModule: Module
    val covidTrackerDao: CovidTrackerDao by inject()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Before
    fun before() {
        // This is not necessary because we have the Covid19TrackerTestRunner
        // with the databaseTestModule using this. It's only an example :)
        mockModule = module(createdAtStart = true, override = true) {
            single {
                Covid19TrackerDatabase.buildTest(get())
            }
        }

        loadKoinModules(mockModule)

        mockRequests()

        runBlocking {
            covidTrackerDao.populateDatabase(
                worldsStats = listOf(worldStatsEntity),
                countries = listOf(countryEntity, countryEmptyRegionsEntity),
                countriesStats = listOf(countryStatsEntity, countryStatsEmptyRegionsEntity),
                regions = listOf(regionEntity),
                regionsStats = listOf(regionStatsEntity),
                subRegions = listOf(subRegionEntity),
                subRegionsStats = listOf(subRegionStatsEntity),
            )
        }

        scenario = launch(MainActivity::class.java)
    }

    @After
    fun after() {
        scenario.close()
        unloadKoinModules(mockModule)
    }

    private fun mockRequests() {
        // Empty requests for UpdateDatabaseWorker
        declareMock<FileUtils> {
            every { generateCurrentDates() } returns listOf()
        }

        declareMock<GetCovidTracker> {
            every { getCovidTrackerByDate(any()) } returns flow {
                emit(Either.right(ScreenStateFactoryTest.stateCovidTrackerEmptyData))
            }
        }
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
    fun countryWithRegionsInMenuListView_shouldHaveAtLeastTwoItemsInTheList() {
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(2, Options.GREATER_THAN_OR_EQUAL))
        )
    }

    @Test
    fun countryWithEmptyRegionsInMenuListView_shouldHaveOneItemInTheList() {
        onView(withId(R.id.list_view)).perform(click())
        selectCountryWithEmptyRegions()

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1, Options.EQUALS))
        )
    }

    @Test
    fun countryWithRegionsInMenuListView_shouldHavePlaceTotalAndPlaceAdapters() {
        onView(withId(R.id.list_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL, Adapters.PLACE)))
        )
    }

    @Test
    fun countryWithEmptyRegionsInMenuListView_shouldOnlyPlaceTotalAdapter() {
        onView(withId(R.id.list_view)).perform(click())
        selectCountryWithEmptyRegions()

        matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL)))
    }

    @Test
    fun countryWithRegionsInBarChartView_shouldHaveTwoItems() {
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(2, Options.EQUALS))
        )
    }

    @Test
    fun countryWithRegionsInMenuBarChartView_shouldHavePlaceTotalBarAndPlaceBarChartsAdapters() {
        onView(withId(R.id.bar_chart_view)).perform(click())

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasAdapters(
                listOf(Adapters.PLACE_TOTAL_BAR_CHART, Adapters.PLACE_BAR_CHART)
            ))
        )
    }

    @Test
    fun countryWithEmptyRegionsInMenuBarChartView_shouldOnlyPlaceBarTotalChartAdapter() {
        onView(withId(R.id.bar_chart_view)).perform(click())
        selectCountryWithEmptyRegions()

        matches(recyclerViewHasAdapters(listOf(Adapters.PLACE_TOTAL_BAR_CHART)))
    }

    @Test
    fun countryWithEmptyRegionsInBarChartView_shouldHaveOneItem() {
        onView(withId(R.id.bar_chart_view)).perform(click())
        selectCountryWithEmptyRegions()

        onView(withId(R.id.recycler_place)).check(
            matches(recyclerViewHasItemCount(1, Options.EQUALS))
        )
    }

    private fun selectCountryWithEmptyRegions() {
        onView(withId(R.id.country_spinner)).perform(click())
        onView(withText(countryEmptyRegionsEntity.name)).perform(click())
    }
}
