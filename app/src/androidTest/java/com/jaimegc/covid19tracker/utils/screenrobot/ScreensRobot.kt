package com.jaimegc.covid19tracker.utils.screenrobot

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEmptyRegionsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEmptySubRegionsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEntity
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.matchers.BottomNavigationViewMenuItemMatcher.Companion.bottomNavigationViewHasMenuItemChecked
import com.jaimegc.covid19tracker.matchers.RecyclerViewCompareSquareViewSizeMatcher.Companion.recyclerViewHasSameViewsSize
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Companion.recyclerViewHasItemCount
import com.jaimegc.covid19tracker.matchers.RecyclerViewItemsCountMatcher.Options
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Adapters
import com.jaimegc.covid19tracker.matchers.RecyclerViewConcatAdapterMatcher.Companion.recyclerViewHasAdapters
import com.jaimegc.covid19tracker.ui.home.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not

open class ScreenRobot {
    protected fun clickOnView(@IdRes viewId: Int): ViewInteraction =
        onView((withId(viewId))).perform(click())

    protected fun clickOnViewWithText(text: String): ViewInteraction =
        onView(withText(text)).perform(click())

    protected fun viewMatches(@IdRes viewId: Int, matcher: Matcher<View>): ViewInteraction =
        onView(withId(viewId)).check(matches(matcher))

    protected fun clickOnItemList(@IdRes viewId: Int, position: Int): ViewInteraction =
        onView(withId(viewId)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(position),
            actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click())
        )

    protected fun viewIsDisplayed(@IdRes viewId: Int): ViewInteraction =
        onView(withId(viewId)).check(matches(isDisplayed()))

    protected fun viewIsNotDisplayed(@IdRes viewId: Int): ViewInteraction =
        onView(withId(viewId)).check(matches(not(isDisplayed())))

    protected fun pressBackButton() = pressBack()

    protected fun checkOpenActivity(name: String) = intended(IntentMatchers.hasComponent(name))
}

open class MenuItem : ScreenRobot() {
    fun clickOnMenuItemListView() = clickOnView(R.id.list_view)
    fun clickOnMenuItemBarChart() = clickOnView(R.id.bar_chart_view)
    fun clickOnMenuItemLineChart() = clickOnView(R.id.line_chart_view)
    fun clickOnMenuItemPieChart() = clickOnView(R.id.pie_chart_view)
}

class WorldScreenRobot : MenuItem() {
    private val viewsSizeExpanded = 60
    private val worldCountryAdapterPosition = 1

    fun checkRecyclerViewHasItemCount(itemCount: Int, option: Options = Options.EQUALS) =
        viewMatches(R.id.recycler_world, recyclerViewHasItemCount(itemCount, option))
    fun checkRecyclerHasAdapters(adapters: List<Adapters>) =
        viewMatches(R.id.recycler_world, recyclerViewHasAdapters(adapters))

    fun clickOnWorldCountryAdapter() =
        clickOnItemList(R.id.recycler_world, worldCountryAdapterPosition)
    fun checkWorldCountryItemExpand() = viewMatches(R.id.recycler_world,
        recyclerViewHasSameViewsSize(worldCountryAdapterPosition, mapOf(
            R.id.ic_deaths to viewsSizeExpanded, R.id.ic_deaths to viewsSizeExpanded,
            R.id.ic_recovered to viewsSizeExpanded, R.id.ic_open_cases to viewsSizeExpanded))
        )
}

class CountryScreenRobot : MenuItem() {
    private val viewsSizeExpanded = 60
    private val placeAdapterPosition = 1

    fun countrySpinnerIsDisplayed() = viewIsDisplayed(R.id.country_spinner)
    fun regionSpinnerIsDisplayed() = viewIsDisplayed(R.id.region_spinner)
    fun regionSpinnerIsNotDisplayed() = viewIsNotDisplayed(R.id.region_spinner)
    fun emptyDatabaseAnimationIsDisplayed() = viewIsDisplayed(R.id.empty_database_animation)
    fun emptyDatabaseTextIsDisplayed() = viewIsDisplayed(R.id.empty_database_text)

    fun checkRecyclerViewHasItemCount(itemCount: Int, option: Options = Options.EQUALS) =
        viewMatches(R.id.recycler_place, recyclerViewHasItemCount(itemCount, option))
    fun checkRecyclerHasAdapters(adapters: List<Adapters>) =
        viewMatches(R.id.recycler_place, recyclerViewHasAdapters(adapters))

    fun selectCountryWithEmptyRegions() {
        clickOnView(R.id.country_spinner)
        clickOnViewWithText(countryEmptyRegionsEntity.name)
    }

    fun selectRegionWithSubRegions() {
        clickOnView(R.id.region_spinner)
        clickOnViewWithText(regionEntity.name)
    }

    fun selectRegionWithEmptySubRegions() {
        clickOnView(R.id.region_spinner)
        clickOnViewWithText(regionEmptySubRegionsEntity.name)
    }

    fun clickOnPlaceAdapter() = clickOnItemList(R.id.recycler_place, placeAdapterPosition)
    fun checkPlaceItemExpand() = viewMatches(R.id.recycler_place,
        recyclerViewHasSameViewsSize(placeAdapterPosition, mapOf(
            R.id.ic_deaths to viewsSizeExpanded, R.id.ic_deaths to viewsSizeExpanded,
            R.id.ic_recovered to viewsSizeExpanded, R.id.ic_open_cases to viewsSizeExpanded))
        )
}

class MainScreenRobot : ScreenRobot() {
    fun clickOnMenuNavigationWorld() = clickOnView(R.id.navigation_world)

    fun checkMenuNavigationCountry() =
        viewMatches(R.id.nav_view, bottomNavigationViewHasMenuItemChecked(R.id.navigation_country))
    fun checkMenuNavigationWorld() =
        viewMatches(R.id.nav_view, bottomNavigationViewHasMenuItemChecked(R.id.navigation_world))

    fun pressBack() = pressBackButton()
}

class InitializeDatabaseScreenRobot : ScreenRobot() {
    fun checkOpenMainActivity() = checkOpenActivity(MainActivity::class.java.name)
}

fun worldScreenRobot(func: WorldScreenRobot.() -> Unit) = WorldScreenRobot().apply { func() }
fun countryScreenRobot(func: CountryScreenRobot.() -> Unit) = CountryScreenRobot().apply { func() }
fun mainScreenRobot(func: MainScreenRobot.() -> Unit) = MainScreenRobot().apply { func() }
fun initializeDatabaseScreenRobot(func: InitializeDatabaseScreenRobot.() -> Unit) =
    InitializeDatabaseScreenRobot().apply { func() }