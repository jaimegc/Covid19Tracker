package com.jaimegc.covid19tracker.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.ui.home.MainActivity
import com.jaimegc.covid19tracker.ui.world.WorldFragment
import com.jaimegc.covid19tracker.utils.matchers.BottomNavigationViewMenuItemMatcher.Companion.bottomNavigationViewHasMenuItemChecked
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.ui.country.CountryFragment
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest : KoinTest {

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
    fun clickOnBackButtonInNavigationWorld_shouldShowNavigationCountry() {
        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_world)).perform(click())
        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.nav_view)).check(
            matches(bottomNavigationViewHasMenuItemChecked(R.id.navigation_country))
        )

        scenario.close()
    }

    @Test
    fun openActivity_shouldCallGetCovidTracker() {
        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(MainActivity::class.java)

        verify { viewModel.getCovidTracker() }

        scenario.close()
    }
}