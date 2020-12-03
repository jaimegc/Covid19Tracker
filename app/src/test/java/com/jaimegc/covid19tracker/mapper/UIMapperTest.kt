package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.ui.model.ErrorUI
import com.jaimegc.covid19tracker.ui.model.toChartUI
import com.jaimegc.covid19tracker.ui.model.toListChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceUI
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.ModelFactoryTest.country
import com.jaimegc.covid19tracker.ModelFactoryTest.countryAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.countryListStatsChartUI
import com.jaimegc.covid19tracker.ModelFactoryTest.countryOneStats
import com.jaimegc.covid19tracker.ModelFactoryTest.countryOneStatsChartUI
import com.jaimegc.covid19tracker.ModelFactoryTest.countryPlaceStatsUI
import com.jaimegc.covid19tracker.ModelFactoryTest.countryUI
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTracker
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTrackerUI
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryOnlyStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryOnlyStatsPlaceUI
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionOnlyStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionOnlyStatsPlaceUI
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionPlaceStatsChartUI
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionStatsPlaceUI
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionPlaceStatsChartUI
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionStatsPlaceUI
import com.jaimegc.covid19tracker.ModelFactoryTest.listWorldCountryStatsUI
import com.jaimegc.covid19tracker.ModelFactoryTest.placeListStatsChartUI
import com.jaimegc.covid19tracker.ModelFactoryTest.placeStatsUI
import com.jaimegc.covid19tracker.ModelFactoryTest.placeUI
import com.jaimegc.covid19tracker.ModelFactoryTest.region
import com.jaimegc.covid19tracker.ModelFactoryTest.regionOneStats
import com.jaimegc.covid19tracker.ModelFactoryTest.stats
import com.jaimegc.covid19tracker.ModelFactoryTest.statsChartUI
import com.jaimegc.covid19tracker.ModelFactoryTest.statsUI
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegion
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStats
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStatsChartUI
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStatsUI
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