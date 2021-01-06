package com.jaimegc.covid19tracker.utils.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class RecyclerViewItemsCountMatcher(
    private val expectedItemCount: Int,
    private val option: Options
) : BaseMatcher<View>() {

    companion object {
        fun recyclerViewHasItemCount(itemCount: Int, option: Options = Options.EQUALS): Matcher<View> =
            RecyclerViewItemsCountMatcher(itemCount, option)
    }

    enum class Options {
        EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL
    }

    override fun matches(item: Any): Boolean =
        (item as RecyclerView).adapter?.let { adapter ->
            return when(option) {
                Options.EQUALS ->
                    adapter.itemCount == expectedItemCount
                Options.NOT_EQUALS ->
                    adapter.itemCount != expectedItemCount
                Options.GREATER_THAN ->
                    adapter.itemCount > expectedItemCount
                Options.LESS_THAN ->
                    adapter.itemCount < expectedItemCount
                Options.GREATER_THAN_OR_EQUAL ->
                    adapter.itemCount >= expectedItemCount
                Options.LESS_THAN_OR_EQUAL ->
                    adapter.itemCount <= expectedItemCount
            }
        } ?: false

    override fun describeTo(description: Description) {
        description.appendText("recyclerview does not contains $expectedItemCount items")
    }
}