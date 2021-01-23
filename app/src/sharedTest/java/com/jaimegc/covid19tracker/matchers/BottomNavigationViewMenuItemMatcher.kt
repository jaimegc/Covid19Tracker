package com.jaimegc.covid19tracker.matchers

import android.view.View
import androidx.core.view.forEach
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.hamcrest.Description
import org.hamcrest.Matcher

class BottomNavigationViewMenuItemMatcher(
    private val menuItemId: Int
) : BoundedMatcher<View, BottomNavigationView>(BottomNavigationView::class.java) {

    companion object {
        fun bottomNavigationViewHasMenuItemChecked(menuItemId: Int): Matcher<View> =
            BottomNavigationViewMenuItemMatcher(menuItemId)
    }

    override fun describeTo(description: Description) {
        description.appendText("BottomNavigationView doesn't have an item with such id = $menuItemId")
    }

    override fun matchesSafely(navigationView: BottomNavigationView): Boolean {
        val menu = navigationView.menu
        var isMenuItemChecked = false

        menu.forEach { menuItem ->
            if (menuItem.isChecked && menuItem.itemId == menuItemId) {
                isMenuItemChecked = true
            }
        }

        return isMenuItemChecked
    }
}