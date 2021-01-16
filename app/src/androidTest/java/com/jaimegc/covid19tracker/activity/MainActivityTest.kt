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
import com.jaimegc.covid19tracker.utils.UITest
import com.jaimegc.covid19tracker.utils.matchers.BottomNavigationViewMenuItemMatcher.Companion.bottomNavigationViewHasMenuItemChecked
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ui.country.CountryFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest : UITest() {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun openActivity_shouldShowCountryFragmentByDefault() {
        scenario.onActivity { activity ->
            val fragments =
                activity.supportFragmentManager.fragments.first().childFragmentManager.fragments
            assertThat(fragments.size).isEqualTo(1)
            assertThat(fragments[0]::class.java).isEqualTo(CountryFragment::class.java)
        }

        onView(withId(R.id.nav_view)).check(
            matches(bottomNavigationViewHasMenuItemChecked(R.id.navigation_country))
        )
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun clickOnNavigationWorld_shouldShowWorldFragment() {
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
    }

    @Test
    fun clickOnBackButtonInNavigationWorld_shouldShowNavigationCountry() {
        onView(withId(R.id.navigation_world)).perform(click())
        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.nav_view)).check(
            matches(bottomNavigationViewHasMenuItemChecked(R.id.navigation_country))
        )
    }
}