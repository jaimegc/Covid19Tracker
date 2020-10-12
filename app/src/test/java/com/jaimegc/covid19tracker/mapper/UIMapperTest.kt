package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.ui.model.ErrorUI
import com.jaimegc.covid19tracker.ui.model.toChartUI
import com.jaimegc.covid19tracker.ui.model.toListChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceUI
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.country
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryListStatsChartUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryOneStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryOneStatsChartUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryPlaceStatsUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTracker
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTrackerUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryOnlyStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryOnlyStatsPlaceUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionOnlyStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionOnlyStatsPlaceUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionPlaceStatsChartUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionStatsPlaceUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionPlaceStatsChartUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionStatsPlaceUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.listWorldCountryStatsUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.placeListStatsChartUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.placeStatsUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.placeUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.region
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionOneStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.stats
import com.jaimegc.covid19tracker.utils.ModelBuilder.statsChartUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.statsUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegion
import com.jaimegc.covid19tracker.utils.ModelBuilder.worldStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.worldStatsChartUI
import com.jaimegc.covid19tracker.utils.ModelBuilder.worldStatsUI
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class UIMapperTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun covidTrackerDomainToUI() {
        assertEquals(covidTrackerUI, covidTracker.toUI())
    }

    @Test
    fun worldStatsDomainToUI() {
        assertEquals(worldStatsUI, worldStats.toUI())
    }

    @Test
    fun countryDomainToUI() {
        assertEquals(countryUI, country.toUI())
    }

    @Test
    fun statsDomainToUI() {
        assertEquals(statsUI, stats.toUI())
    }

    @Test
    fun statsDomainToChartUI() {
        assertEquals(statsChartUI, stats.toChartUI())
    }

    @Test
    fun countryOneStatsDomainToChartUI() {
        assertEquals(countryOneStatsChartUI, countryOneStats.toChartUI())
    }

    @Test
    fun worldStatsDomainToListChartUI() {
        assertEquals(worldStatsChartUI, worldStats.toListChartUI())
    }

    @Test
    fun countryAndStatsDomainToListChartUI() {
        assertEquals(countryListStatsChartUI, countryAndStats.toListChartUI())
    }

    @Test
    fun covidTrackerDomainToListChartUI() {
        assertEquals(listWorldCountryStatsUI, covidTracker.toListChartUI())
    }

    @Test
    fun regionDomainToPlaceUI() {
        assertEquals(placeUI, region.toPlaceUI())
    }

    @Test
    fun subRegionDomainToPlaceUI() {
        assertEquals(placeUI, subRegion.toPlaceUI())
    }

    @Test
    fun countryOneStatsDomainToPlaceUI() {
        assertEquals(countryPlaceStatsUI, countryOneStats.toPlaceUI())
    }

    @Test
    fun regionOneStatsDomainToPlaceUI() {
        assertEquals(placeStatsUI, regionOneStats.toPlaceUI())
    }

    @Test
    fun listRegionStatsDomainToPlaceUI() {
        assertEquals(listRegionStatsPlaceUI, listRegionStats.toPlaceUI())
    }

    @Test
    fun listSubRegionStatsDomainToPlaceUI() {
        assertEquals(listSubRegionStatsPlaceUI, listSubRegionStats.toPlaceUI())
    }

    @Test
    fun listCountryOnlyStatsDomainToPlaceUI() {
        assertEquals(listCountryOnlyStatsPlaceUI, listCountryOnlyStats.toPlaceUI())
    }

    @Test
    fun listRegionOnlyStatsDomainToPlaceUI() {
        assertEquals(listRegionOnlyStatsPlaceUI, listRegionOnlyStats.toPlaceUI())
    }

    @Test
    fun listRegionAndStatsDomainToPlaceUI() {
        assertEquals(placeListStatsChartUI, listRegionAndStats.toPlaceUI())
    }

    @Test
    fun listSubRegionAndStatsDomainToPlaceUI() {
        assertEquals(placeListStatsChartUI, listSubRegionAndStats.toPlaceUI())
    }

    @Test
    fun listRegionStatsDomainToPlaceChartUI() {
        assertEquals(listRegionPlaceStatsChartUI, listRegionStats.toPlaceChartUI())
    }

    @Test
    fun listSubRegionStatsDomainToPlaceChartUI() {
        assertEquals(listSubRegionPlaceStatsChartUI, listSubRegionStats.toPlaceChartUI())
    }

    @Test
    fun errorDomainToUI() {
        val domainError: DomainError = mock()

        val uiError = ErrorUI.SomeError

        assertEquals(uiError, domainError.toUI())
    }
}