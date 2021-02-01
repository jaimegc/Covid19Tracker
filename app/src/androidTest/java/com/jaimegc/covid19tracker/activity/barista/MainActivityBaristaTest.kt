package com.jaimegc.covid19tracker.activity.barista

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import arrow.core.Either
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.home.MainActivity
import com.jaimegc.covid19tracker.ui.world.WorldFragment
import com.jaimegc.covid19tracker.matchers.BottomNavigationViewMenuItemMatcher.Companion.bottomNavigationViewHasMenuItemChecked
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ScreenStateFactoryTest
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.country.CountryFragment
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import com.jaimegc.covid19tracker.util.FileUtils
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickBack
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.internal.assertAnyView
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityBaristaTest : KoinTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

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
    fun openActivity_shouldShowCountryFragmentByDefault() {
        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(MainActivity::class.java)

        scenario.onActivity { activity ->
            val fragments =
                activity.supportFragmentManager.fragments.first().childFragmentManager.fragments
            assertThat(fragments.size).isEqualTo(1)
            assertThat(fragments[0]::class.java).isEqualTo(CountryFragment::class.java)
        }

        assertAnyView(displayedWithId(R.id.nav_view),
            bottomNavigationViewHasMenuItemChecked(R.id.navigation_country)
        )

        scenario.close()
    }

    @Test
    fun clickOnNavigationWorld_shouldShowWorldFragment() {
        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(MainActivity::class.java)

        clickOn(R.id.navigation_world)

        scenario.onActivity { activity ->
            val fragments =
                activity.supportFragmentManager.fragments.first().childFragmentManager.fragments
            assertThat(fragments.size).isEqualTo(2)
            assertThat(fragments[1]::class.java).isEqualTo(WorldFragment::class.java)
        }

        assertAnyView(displayedWithId(R.id.nav_view),
            bottomNavigationViewHasMenuItemChecked(R.id.navigation_world)
        )

        scenario.close()
    }

    @Test
    fun pressBackButtonInNavigationWorld_shouldShowNavigationCountry() {
        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(MainActivity::class.java)

        clickOn(R.id.navigation_world)
        clickBack()

        onView(withId(R.id.nav_view)).check(
            matches(bottomNavigationViewHasMenuItemChecked(R.id.navigation_country))
        )

        scenario.close()
    }
}