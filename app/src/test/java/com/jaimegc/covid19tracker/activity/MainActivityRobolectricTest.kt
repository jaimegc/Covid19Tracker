package com.jaimegc.covid19tracker.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.work.testing.WorkManagerTestInitHelper
import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.matchers.BottomNavigationViewMenuItemMatcher.Companion.bottomNavigationViewHasMenuItemChecked
import com.jaimegc.covid19tracker.ui.country.CountryFragment
import com.jaimegc.covid19tracker.ui.home.MainActivity
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import com.jaimegc.covid19tracker.ui.world.WorldFragment
import com.jaimegc.covid19tracker.utils.FileUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode


@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityRobolectricTest : AutoCloseKoinTest() {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private val viewModel = mockk<MainViewModel>(relaxed = true)
    private lateinit var mockModule: Module

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Before
    fun setUp() {
        mockModule = module(createdAtStart = true, override = true) {
            single {
                Covid19TrackerDatabase.buildTest(get())
            }
            single {
                viewModel
            }
        }

        mockRequests()

        WorkManagerTestInitHelper.initializeTestWorkManager(getApplicationContext())
    }

    private fun mockRequests() {
        // Empty requests for UpdateDatabaseWorker
        declareMock<FileUtils> {
            every { generateCurrentDates() } returns listOf()
        }

        declareMock<GetCovidTracker> {
            every { getCovidTrackerByDate(any()) } returns flow {
                emit(Either.right(stateCovidTrackerEmptyData))
            }
        }
    }

    @Test
    fun openActivity_shouldShowCountryFragmentByDefault() {
        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(MainActivity::class.java)

        scenario.onActivity { activity ->
            val fragments =
                activity.supportFragmentManager.fragments.first().childFragmentManager.fragments
            assertThat(fragments.size).isEqualTo(1)
            assertThat(fragments[0]::class.java).isEqualTo(CountryFragment::class.java)
        }

        onView(withId(R.id.nav_view)).check(
            matches(bottomNavigationViewHasMenuItemChecked(R.id.navigation_country))
        )

        scenario.close()
    }

    @Test
    fun clickOnNavigationWorld_shouldShowWorldFragment() {
        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_world)).perform(click())

        scenario.onActivity { activity ->
            val fragments =
                activity.supportFragmentManager.fragments.first().childFragmentManager.fragments
            assertThat(fragments.size).isEqualTo(2)
            assertThat(fragments[1]::class.java).isEqualTo(WorldFragment::class.java)
        }

        onView(withId(R.id.nav_view)).check(
            matches(bottomNavigationViewHasMenuItemChecked(R.id.navigation_world))
        )

        scenario.close()
    }

    @Test
    fun pressBackButtonInNavigationWorld_shouldShowNavigationCountry() {
        loadKoinModules(mockModule)

        buildActivity(MainActivity::class.java).setup()

        onView(withId(R.id.navigation_world)).perform(click())
        pressBack()

        onView(withId(R.id.nav_view)).check(
            matches(bottomNavigationViewHasMenuItemChecked(R.id.navigation_country))
        )
    }

    @Test
    fun openActivity_shouldCallGetCovidTracker() {
        loadKoinModules(mockModule)

        buildActivity(MainActivity::class.java).setup()

        verify { viewModel.getCovidTracker() }
    }
}