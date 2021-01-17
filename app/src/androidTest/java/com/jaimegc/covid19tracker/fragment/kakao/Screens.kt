package com.jaimegc.covid19tracker.fragment.kakao

import android.view.View
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.spinner.KSpinner
import com.agoda.kakao.spinner.KSpinnerItem
import com.agoda.kakao.text.KTextView
import com.jaimegc.covid19tracker.R
import org.hamcrest.Matcher

class WorldScreen : Screen<WorldScreen>() {
    val menuItemListView = KView { withId(R.id.list_view) }
    val menuItemBarChart = KView { withId(R.id.bar_chart_view) }
    val menuItemLineChart = KView { withId(R.id.line_chart_view) }
    val menuItemPieChart = KView { withId(R.id.pie_chart_view) }

    val recyclerWorld: KRecyclerView = KRecyclerView(
        {
            withId(R.id.recycler_world)
        }, itemTypeBuilder = {
            itemType(::RecyclerWorldItem)
        }
    )

    class RecyclerWorldItem(parent: Matcher<View>) : KRecyclerItem<RecyclerWorldItem>(parent)
}

class CountryScreen : Screen<CountryScreen>() {
    val spinnerCountry = KSpinner(
        builder = { withId(R.id.country_spinner) },
        itemTypeBuilder = { itemType(::KSpinnerItem) }
    )

    val spinnerRegion = KSpinner(
        builder = { withId(R.id.region_spinner) },
        itemTypeBuilder = { itemType(::KSpinnerItem) }
    )

    val menuItemListView = KView { withId(R.id.list_view) }
    val menuItemBarChart = KView { withId(R.id.bar_chart_view) }
    val menuItemLineChart = KView { withId(R.id.line_chart_view) }
    val menuItemPieChart = KView { withId(R.id.pie_chart_view) }

    val recyclerPlace: KRecyclerView = KRecyclerView(
        {
            withId(R.id.recycler_place)
        }, itemTypeBuilder = {
            itemType(::RecyclerCountryItem)
        }
    )

    val emptyDatabaseAnimation = KTextView { withId(R.id.empty_database_animation) }
    val emptyDatabaseText = KTextView { withId(R.id.empty_database_text) }

    class RecyclerCountryItem(parent: Matcher<View>) : KRecyclerItem<RecyclerCountryItem>(parent)
}