package com.jaimegc.covid19tracker.utils.matchers

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.jaimegc.covid19tracker.common.extensions.toPx
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class RecyclerViewCompareSquareViewSizeMatcher(
    private val adapterPosition: Int,
    private val mapViewsSize: Map<Int, Int>
) : BaseMatcher<View>() {

    companion object {
        fun recyclerViewHasSameViewsSize(adapterPosition: Int, viewsSize: Map<Int, Int>): Matcher<View> =
            RecyclerViewCompareSquareViewSizeMatcher(adapterPosition, viewsSize)
    }

    override fun matches(item: Any): Boolean =
        (item as RecyclerView).let { recycler ->
            recycler.findViewHolderForAdapterPosition(adapterPosition)?.let { viewHolder ->
                var hasDifferentSizes = true
                (viewHolder.itemView as ConstraintLayout).children.iterator().forEach { view ->
                    if (hasDifferentSizes) {
                        mapViewsSize[view.id]?.let { size ->
                            val sizePx = size.toPx()
                            if (view.width != sizePx || view.height != sizePx) hasDifferentSizes = false
                        }
                    }
                }
                hasDifferentSizes
            } ?: false
        }

    override fun describeTo(description: Description) {
        description.appendText(
            "recyclerview does not contains adapter or the view size is incorrect"
        )
    }
}