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
        covidTracker.toUI() shouldBe covidTrackerUI
    }

    "worldStatsDomain to ui" {
        worldStats.toUI() shouldBe worldStatsUI
    }

    "countryDomain to ui" {
        country.toUI() shouldBe countryUI
    }

    "statsDomain to ui" {
        stats.toUI() shouldBe statsUI
    }

    "statsDomain to chart ui" {
        stats.toChartUI() shouldBe statsChartUI
    }

    "countryOneStatsDomain to chart ui" {
        countryOneStats.toChartUI() shouldBe countryOneStatsChartUI
    }

    "worldStatsDomain to list chart ui" {
        worldStats.toListChartUI() shouldBe worldStatsChartUI
    }

    "countryAndStatsDomain to list chart ui" {
        countryAndStats.toListChartUI() shouldBe countryListStatsChartUI
    }

    "covidTrackerDomain to list chart ui" {
        covidTracker.toListChartUI() shouldBe listWorldCountryStatsUI
    }

    "regionDomain to place ui" {
        region.toPlaceUI() shouldBe placeUI
    }

    "subRegionDomain to place ui" {
        subRegion.toPlaceUI() shouldBe
            placeUI.copy(id = subRegion.id, name = subRegion.name, nameEs = subRegion.nameEs)
    }

    "countryOneStatsDomain to place ui" {
        countryOneStats.toPlaceUI() shouldBe countryPlaceStatsUI
    }

    "regionOneStatsDomain to place ui" {
        regionOneStats.toPlaceUI() shouldBe placeStatsUI
    }

    "listRegionStatsDomain to place ui" {
        listRegionStats.toPlaceUI() shouldBe listRegionStatsPlaceUI
    }

    "listSubRegionStatsDomain to place ui" {
        listSubRegionStats.toPlaceUI() shouldBe
            listOf(listSubRegionStatsPlaceUI[0].copy(
                id = subRegion.id, name = subRegion.name, nameEs = subRegion.nameEs)
            )
    }

    "listCountryOnlyStatsDomain to place ui" {
        listCountryOnlyStats.toPlaceUI() shouldBe listCountryOnlyStatsPlaceUI
    }

    "listRegionOnlyStatsDomain to place ui" {
        listRegionOnlyStats.toPlaceUI() shouldBe listRegionOnlyStatsPlaceUI
    }

    "listRegionAndStatsDomain to place ui" {
        listRegionAndStats.toPlaceUI() shouldBe placeListStatsChartUI
    }

    "listSubRegionAndStatsDomain to place ui" {
        listSubRegionAndStats.toPlaceUI() shouldBe
            listOf(placeListStatsChartUI[0].copy(
                place = placeUI.copy(subRegion.id, name = subRegion.name, nameEs = subRegion.nameEs))
            )
    }

    "listRegionStatsDomain to place chart ui" {
        listRegionStats.toPlaceChartUI() shouldBe listRegionPlaceStatsChartUI
    }

    "listSubRegionStatsDomain to place chart ui" {
        listSubRegionStats.toPlaceChartUI() shouldBe
            listOf(listSubRegionPlaceStatsChartUI[0].copy(
                place = placeUI.copy(id = subRegion.id, name = subRegion.name, nameEs = subRegion.nameEs))
            )
    }

    "errorDomain to ui" {
        val domainError: DomainError = mock()
        val uiError = ErrorUI.SomeError

        domainError.toUI() shouldBe uiError
    }
})