package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.domain.model.Country
import com.jaimegc.covid19tracker.domain.model.CountryAndStats
import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListCountryOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListRegionOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegionStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionStats
import com.jaimegc.covid19tracker.domain.model.Region
import com.jaimegc.covid19tracker.domain.model.RegionAndStats
import com.jaimegc.covid19tracker.domain.model.RegionOneStats
import com.jaimegc.covid19tracker.domain.model.RegionStats
import com.jaimegc.covid19tracker.domain.model.Stats
import com.jaimegc.covid19tracker.domain.model.SubRegion
import com.jaimegc.covid19tracker.domain.model.SubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.SubRegionStats
import com.jaimegc.covid19tracker.domain.model.WorldStats
import com.jaimegc.covid19tracker.ui.model.CountryListStatsChartUI
import com.jaimegc.covid19tracker.ui.model.CountryStatsChartUI
import com.jaimegc.covid19tracker.ui.model.CountryStatsUI
import com.jaimegc.covid19tracker.ui.model.CountryUI
import com.jaimegc.covid19tracker.ui.model.CovidTrackerUI
import com.jaimegc.covid19tracker.ui.model.ErrorUI
import com.jaimegc.covid19tracker.ui.model.PlaceListStatsChartUI
import com.jaimegc.covid19tracker.ui.model.PlaceStatsChartUI
import com.jaimegc.covid19tracker.ui.model.PlaceStatsUI
import com.jaimegc.covid19tracker.ui.model.PlaceUI
import com.jaimegc.covid19tracker.ui.model.StatsChartUI
import com.jaimegc.covid19tracker.ui.model.StatsUI
import com.jaimegc.covid19tracker.ui.model.WorldCountryStatsUI
import com.jaimegc.covid19tracker.ui.model.WorldStatsChartUI
import com.jaimegc.covid19tracker.ui.model.WorldStatsUI
import com.jaimegc.covid19tracker.ui.model.toChartUI
import com.jaimegc.covid19tracker.ui.model.toListChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceUI
import com.jaimegc.covid19tracker.ui.model.toUI
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class UIMapperTest {

    private val stats =
        Stats(
            dateTimestamp = 1601589600000L,
            date = "2020-10-02",
            source = "John Hopkins University",
            confirmed = 39290,
            deaths = 1458,
            newConfirmed = 5,
            newDeaths = 0,
            newOpenCases = 5,
            newRecovered = 0,
            openCases = 4990,
            recovered = 32842,
            vsYesterdayConfirmed = 0.000127275041364339,
            vsYesterdayDeaths = 0.0,
            vsYesterdayOpenCases = 0.00100300902708117,
            vsYesterdayRecovered = 0.0
        )

    private val statsUI =
        StatsUI(
            date = "2020-10-02",
            source = "John Hopkins University",
            confirmed = "39,290",
            deaths = "1,458",
            newConfirmed = "5",
            newDeaths = "0",
            newOpenCases = "5",
            newRecovered = "0",
            openCases = "4,990",
            recovered = "32,842",
            confirmedCompact = "39,290",
            deathsCompact = "1,458",
            openCasesCompact = "4,990",
            recoveredCompact = "32,842",
            vsYesterdayConfirmed = "0.02",
            vsYesterdayDeaths = "0.00",
            vsYesterdayOpenCases = "0.11",
            vsYesterdayRecovered = "0.00"
        )

    private val worldStats =
        WorldStats(
            dateTimestamp = 1601589600000L,
            date = "2020-10-02",
            updatedAt = "2020-10-02 22:10UTC",
            stats = stats
        )

    private val worldStatsUI =
        WorldStatsUI(
            date = worldStats.date,
            updatedAt = worldStats.updatedAt,
            stats = statsUI
        )

    private val country =
        Country(
            id = "spain",
            name = "Spain", nameEs = "España",
            code = "ES"
        )

    private val countryUI =
        CountryUI(
            id = country.id,
            name = country.name,
            nameEs = country.nameEs,
            code = country.code
        )

    private val region =
        Region(
            id = "andalucia",
            name = "Andalucia",
            nameEs = "Andalucía"
        )

    private val placeUI =
        PlaceUI(
            id = "andalucia",
            name = "Andalucia",
            nameEs = "Andalucía"
        )

    private val subRegion =
        SubRegion(
            id = "andalucia",
            name = "Andalucia",
            nameEs = "Andalucía"
        )

    private val regionOneStats =
        RegionOneStats(
            region = region,
            stats = stats
        )

    private val subRegionStats =
        SubRegionStats(
            subRegion = subRegion,
            stats = stats
        )

    private val regionStats =
        RegionStats(
            region = region,
            stats = stats,
            subRegionStats = listOf(subRegionStats)
        )

    private val countryOneStats =
        CountryOneStats(
            country = country,
            stats = stats,
            regionStats = listOf(regionStats)
        )

    private val countryAndStats =
        CountryAndStats(
            country = country,
            stats = listOf(stats)
        )

    private val regionAndStats =
        RegionAndStats(
            region = region,
            stats = listOf(stats)
        )

    private val subRegionAndStats =
        SubRegionAndStats(
            subRegion = subRegion,
            stats = listOf(stats)
        )

    private val statsChartUI =
        StatsChartUI(
            date = stats.date,
            source = stats.source,
            confirmed = 39290.0f,
            deaths = 1458.0f,
            newConfirmed = 5.0f,
            newDeaths = 0.0f,
            newOpenCases = 5.0f,
            newRecovered = 0.0f,
            openCases = 4990.0f,
            recovered = 32842.0f
        )

    private val countryOneStatsChartUI =
        CountryStatsChartUI(
            country = countryUI,
            stats = statsChartUI
        )

    private val countryStatsUI =
        CountryStatsUI(
            country = countryUI,
            stats = statsUI,
            isExpanded = false
        )

    private val covidTracker =
        CovidTracker(
            countriesStats = listOf(countryOneStats),
            worldStats = worldStats
        )

    private val covidTrackerUI =
        CovidTrackerUI(
            countriesStats = listOf(countryStatsUI),
            worldStats = worldStatsUI
        )

    private val countryPlaceStatsUI =
        PlaceStatsUI(
            id = country.id,
            name = country.name,
            nameEs = country.nameEs,
            code = country.code,
            stats = statsUI
        )

    private val placeStatsUI =
        PlaceStatsUI(
            id = "andalucia",
            name = "Andalucia",
            nameEs = "Andalucía",
            stats = statsUI
        )

    private val worldStatsChartUI =
        WorldStatsChartUI(
            date = worldStats.date,
            updatedAt = worldStats.updatedAt,
            stats = statsChartUI
        )

    private val countryListStatsChartUI =
        CountryListStatsChartUI(
            country = countryUI,
            stats = listOf(statsChartUI)
        )

    private val worldCountryStatsUI =
        WorldCountryStatsUI(
            countryStats = countryOneStatsChartUI,
            worldStats = worldStatsChartUI
        )

    private val listWorldCountryStatsUI =
        listOf(
            worldCountryStatsUI
        )

    private val listRegionStats =
        ListRegionStats(
            regionStats = listOf(regionStats)
        )

    private val listSubRegionStats =
        ListSubRegionStats(
            subRegionStats = listOf(subRegionStats)
        )

    private val listRegionStatsPlaceUI =
        listOf(
            placeStatsUI
        )

    private val listSubRegionStatsPlaceUI =
        listOf(
            placeStatsUI
        )

    private val placeStatsChartUI =
        PlaceStatsChartUI(
            place = placeUI,
            stats = statsChartUI
        )

    private val listRegionPlaceStatsChartUI =
        listOf(
            placeStatsChartUI
        )

    private val listSubRegionPlaceStatsChartUI =
        listOf(
            placeStatsChartUI
        )

    private val listCountryOnlyStats =
        ListCountryOnlyStats(
            countriesStats = listOf(stats)
        )

    private val listRegionOnlyStats =
        ListRegionOnlyStats(
            regionStats = listOf(stats)
        )

    private val listCountryOnlyStatsPlaceUI =
        listOf(
            statsChartUI
        )

    private val listRegionOnlyStatsPlaceUI =
        listOf(
            statsChartUI
        )

    private val listRegionAndStats =
        ListRegionAndStats(
            regionStats = listOf(regionAndStats)
        )

    private val listSubRegionAndStats =
        ListSubRegionAndStats(
            subRegionStats = listOf(subRegionAndStats)
        )

    private val placeListStatsChartUI =
        listOf(
            PlaceListStatsChartUI(
                place = placeUI,
                stats = listOf(statsChartUI)
            )
        )

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun covidTrackerModelToUI() {
        assertEquals(covidTracker.toUI(), covidTrackerUI)
    }

    @Test
    fun worldStatsModelToUI() {
        assertEquals(worldStats.toUI(), worldStatsUI)
    }

    @Test
    fun countryModelToUI() {
        assertEquals(country.toUI(), countryUI)
    }

    @Test
    fun statsModelToUI() {
        assertEquals(stats.toUI(), statsUI)
    }

    @Test
    fun statsModelToChartUI() {
        assertEquals(stats.toChartUI(), statsChartUI)
    }

    @Test
    fun countryOneStatsModelToChartUI() {
        assertEquals(countryOneStats.toChartUI(), countryOneStatsChartUI)
    }

    @Test
    fun worldStatsModelToListChartUI() {
        assertEquals(worldStats.toListChartUI(), worldStatsChartUI)
    }

    @Test
    fun countryAndStatsModelToListChartUI() {
        assertEquals(countryAndStats.toListChartUI(), countryListStatsChartUI)
    }

    @Test
    fun covidTrackerModelToListChartUI() {
        assertEquals(covidTracker.toListChartUI(), listWorldCountryStatsUI)
    }

    @Test
    fun regionModelToPlaceUI() {
        assertEquals(region.toPlaceUI(), placeUI)
    }

    @Test
    fun subRegionModelToPlaceUI() {
        assertEquals(subRegion.toPlaceUI(), placeUI)
    }

    @Test
    fun countryOneStatsModelToPlaceUI() {
        assertEquals(countryOneStats.toPlaceUI(), countryPlaceStatsUI)
    }

    @Test
    fun regionOneStatsModelToPlaceUI() {
        assertEquals(regionOneStats.toPlaceUI(), placeStatsUI)
    }

    @Test
    fun listRegionStatsModelToPlaceUI() {
        assertEquals(listRegionStats.toPlaceUI(), listRegionStatsPlaceUI)
    }

    @Test
    fun listSubRegionStatsModelToPlaceUI() {
        assertEquals(listSubRegionStats.toPlaceUI(), listSubRegionStatsPlaceUI)
    }

    @Test
    fun listCountryOnlyStatsModelToPlaceUI() {
        assertEquals(listCountryOnlyStats.toPlaceUI(), listCountryOnlyStatsPlaceUI)
    }

    @Test
    fun listRegionOnlyStatsModelToPlaceUI() {
        assertEquals(listRegionOnlyStats.toPlaceUI(), listRegionOnlyStatsPlaceUI)
    }

    @Test
    fun listRegionAndStatsModelToPlaceUI() {
        assertEquals(listRegionAndStats.toPlaceUI(), placeListStatsChartUI)
    }

    @Test
    fun listSubRegionAndStatsModelToPlaceUI() {
        assertEquals(listSubRegionAndStats.toPlaceUI(), placeListStatsChartUI)
    }

    @Test
    fun listRegionStatsModelToPlaceChartUI() {
        assertEquals(listRegionStats.toPlaceChartUI(), listRegionPlaceStatsChartUI)
    }

    @Test
    fun listSubRegionStatsModelToPlaceChartUI() {
        assertEquals(listSubRegionStats.toPlaceChartUI(), listSubRegionPlaceStatsChartUI)
    }

    @Test
    fun errorModelToUI() {
        val domainError: DomainError = mock()

        val uiError = ErrorUI.SomeError

        assertEquals(domainError.toUI(), uiError)
    }
}