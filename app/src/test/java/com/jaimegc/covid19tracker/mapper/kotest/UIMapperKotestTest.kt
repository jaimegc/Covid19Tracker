package com.jaimegc.covid19tracker.mapper.kotest

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
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.*

class UIMapperKotestTest : StringSpec({

    beforeTest {
        Locale.setDefault(Locale.US)
    }

    "covidTrackerDomain to ui" {
        covidTrackerUI shouldBe covidTracker.toUI()
    }

    "worldStatsDomain to ui" {
        worldStatsUI shouldBe worldStats.toUI()
    }

    "countryDomain to ui" {
        countryUI shouldBe country.toUI()
    }

    "statsDomain to ui" {
        statsUI shouldBe stats.toUI()
    }

    "statsDomain to chart ui" {
        statsChartUI shouldBe stats.toChartUI()
    }

    "countryOneStatsDomain to chart ui" {
        countryOneStatsChartUI shouldBe countryOneStats.toChartUI()
    }

    "worldStatsDomain to list chart ui" {
        worldStatsChartUI shouldBe worldStats.toListChartUI()
    }

    "countryAndStatsDomain to list chart ui" {
        countryListStatsChartUI shouldBe countryAndStats.toListChartUI()
    }

    "covidTrackerDomain to list chart ui" {
        listWorldCountryStatsUI shouldBe covidTracker.toListChartUI()
    }

    "regionDomain to place ui" {
        placeUI shouldBe region.toPlaceUI()
    }

    "subRegionDomain to place ui" {
        placeUI.copy(id = subRegion.id, name = subRegion.name, nameEs = subRegion.nameEs) shouldBe
            subRegion.toPlaceUI()
    }

    "countryOneStatsDomain to place ui" {
        countryPlaceStatsUI shouldBe countryOneStats.toPlaceUI()
    }

    "regionOneStatsDomain to place ui" {
        placeStatsUI shouldBe regionOneStats.toPlaceUI()
    }

    "listRegionStatsDomain to place ui" {
        listRegionStatsPlaceUI shouldBe listRegionStats.toPlaceUI()
    }

    "listSubRegionStatsDomain to place ui" {
        listOf(listSubRegionStatsPlaceUI[0].copy(
            id = subRegion.id, name = subRegion.name, nameEs = subRegion.nameEs)
        ) shouldBe listSubRegionStats.toPlaceUI()
    }

    "listCountryOnlyStatsDomain to place ui" {
        listCountryOnlyStatsPlaceUI shouldBe listCountryOnlyStats.toPlaceUI()
    }

    "listRegionOnlyStatsDomain to place ui" {
        listRegionOnlyStatsPlaceUI shouldBe listRegionOnlyStats.toPlaceUI()
    }

    "listRegionAndStatsDomain to place ui" {
        placeListStatsChartUI shouldBe listRegionAndStats.toPlaceUI()
    }

    "listSubRegionAndStatsDomain to place ui" {
        listOf(placeListStatsChartUI[0].copy(
            place = placeUI.copy(subRegion.id, name = subRegion.name, nameEs = subRegion.nameEs))
        ) shouldBe listSubRegionAndStats.toPlaceUI()
    }

    "listRegionStatsDomain to place chart ui" {
        listRegionPlaceStatsChartUI shouldBe listRegionStats.toPlaceChartUI()
    }

    "listSubRegionStatsDomain to place chart ui" {
        listOf(listSubRegionPlaceStatsChartUI[0].copy(
            place = placeUI.copy(id = subRegion.id, name = subRegion.name, nameEs = subRegion.nameEs))
        ) shouldBe listSubRegionStats.toPlaceChartUI()
    }

    "errorDomain to ui" {
        val domainError: DomainError = mock()
        val uiError = ErrorUI.SomeError

        uiError shouldBe domainError.toUI()
    }
})