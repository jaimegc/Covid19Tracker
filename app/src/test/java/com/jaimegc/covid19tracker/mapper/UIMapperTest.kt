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
    fun `covidTrackerDomain to ui`() {
        assertEquals(covidTrackerUI, covidTracker.toUI())
    }

    @Test
    fun `worldStatsDomain to ui`() {
        assertEquals(worldStatsUI, worldStats.toUI())
    }

    @Test
    fun `countryDomain to ui`() {
        assertEquals(countryUI, country.toUI())
    }

    @Test
    fun `statsDomain to ui`() {
        assertEquals(statsUI, stats.toUI())
    }

    @Test
    fun `statsDomain to chart ui`() {
        assertEquals(statsChartUI, stats.toChartUI())
    }

    @Test
    fun `countryOneStatsDomain to chart ui`() {
        assertEquals(countryOneStatsChartUI, countryOneStats.toChartUI())
    }

    @Test
    fun `worldStatsDomain to list chart ui`() {
        assertEquals(worldStatsChartUI, worldStats.toListChartUI())
    }

    @Test
    fun `countryAndStatsDomain to list chart ui`() {
        assertEquals(countryListStatsChartUI, countryAndStats.toListChartUI())
    }

    @Test
    fun `covidTrackerDomain to list chart ui`() {
        assertEquals(listWorldCountryStatsUI, covidTracker.toListChartUI())
    }

    @Test
    fun `regionDomain to place ui`() {
        assertEquals(placeUI, region.toPlaceUI())
    }

    @Test
    fun `subRegionDomain to place ui`() {
        assertEquals(placeUI, subRegion.toPlaceUI())
    }

    @Test
    fun `countryOneStatsDomain to place ui`() {
        assertEquals(countryPlaceStatsUI, countryOneStats.toPlaceUI())
    }

    @Test
    fun `regionOneStatsDomain to place ui`() {
        assertEquals(placeStatsUI, regionOneStats.toPlaceUI())
    }

    @Test
    fun `listRegionStatsDomain to place ui`() {
        assertEquals(listRegionStatsPlaceUI, listRegionStats.toPlaceUI())
    }

    @Test
    fun `listSubRegionStatsDomain to place ui`() {
        assertEquals(listSubRegionStatsPlaceUI, listSubRegionStats.toPlaceUI())
    }

    @Test
    fun `listCountryOnlyStatsDomain to place ui`() {
        assertEquals(listCountryOnlyStatsPlaceUI, listCountryOnlyStats.toPlaceUI())
    }

    @Test
    fun `listRegionOnlyStatsDomain to place ui`() {
        assertEquals(listRegionOnlyStatsPlaceUI, listRegionOnlyStats.toPlaceUI())
    }

    @Test
    fun `listRegionAndStatsDomain to place ui`() {
        assertEquals(placeListStatsChartUI, listRegionAndStats.toPlaceUI())
    }

    @Test
    fun `listSubRegionAndStatsDomain to place ui`() {
        assertEquals(placeListStatsChartUI, listSubRegionAndStats.toPlaceUI())
    }

    @Test
    fun `listRegionStatsDomain to place chart ui`() {
        assertEquals(listRegionPlaceStatsChartUI, listRegionStats.toPlaceChartUI())
    }

    @Test
    fun `listSubRegionStatsDomain to place chart ui`() {
        assertEquals(listSubRegionPlaceStatsChartUI, listSubRegionStats.toPlaceChartUI())
    }

    @Test
    fun `errorDomain to ui`() {
        val domainError: DomainError = mock()

        val uiError = ErrorUI.SomeError

        assertEquals(uiError, domainError.toUI())
    }
}