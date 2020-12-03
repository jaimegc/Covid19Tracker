package com.jaimegc.covid19tracker

import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDateCountryDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDateDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerTotalDto
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.StatsEmbedded
import com.jaimegc.covid19tracker.data.room.entities.SubRegionEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.WorldAndCountriesStatsPojo
import com.jaimegc.covid19tracker.data.room.views.CountryAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.RegionAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.SubRegionAndStatsDV
import com.jaimegc.covid19tracker.domain.model.Country
import com.jaimegc.covid19tracker.domain.model.CountryAndStats
import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.ListCountry
import com.jaimegc.covid19tracker.domain.model.ListCountryAndStats
import com.jaimegc.covid19tracker.domain.model.ListCountryOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegion
import com.jaimegc.covid19tracker.domain.model.ListRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListRegionOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegionStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionStats
import com.jaimegc.covid19tracker.domain.model.ListWorldStats
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
import com.jaimegc.covid19tracker.ui.model.PlaceListStatsChartUI
import com.jaimegc.covid19tracker.ui.model.PlaceStatsChartUI
import com.jaimegc.covid19tracker.ui.model.PlaceStatsUI
import com.jaimegc.covid19tracker.ui.model.PlaceUI
import com.jaimegc.covid19tracker.ui.model.StatsChartUI
import com.jaimegc.covid19tracker.ui.model.StatsUI
import com.jaimegc.covid19tracker.ui.model.WorldCountryStatsUI
import com.jaimegc.covid19tracker.ui.model.WorldStatsChartUI
import com.jaimegc.covid19tracker.ui.model.WorldStatsUI

object ModelFactoryTest {
    val stats =
        Stats(
            dateTimestamp = 1601596800000L,
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

    val statsUI =
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

    val worldStats =
        WorldStats(
            dateTimestamp = 1601596800000L,
            date = "2020-10-02",
            updatedAt = "2020-10-02 22:10UTC",
            stats = stats
        )

    val worldStatsUI =
        WorldStatsUI(
            date = worldStats.date,
            updatedAt = worldStats.updatedAt,
            stats = statsUI
        )

    val country =
        Country(
            id = "spain",
            name = "Spain",
            nameEs = "España",
            code = "ES"
        )

    val countryUI =
        CountryUI(
            id = country.id,
            name = country.name,
            nameEs = country.nameEs,
            code = country.code
        )

    val region =
        Region(
            id = "andalucia",
            name = "Andalucia",
            nameEs = "Andalucía"
        )

    val placeUI =
        PlaceUI(
            id = "andalucia",
            name = "Andalucia",
            nameEs = "Andalucía"
        )

    val subRegion =
        SubRegion(
            id = "andalucia",
            name = "Andalucia",
            nameEs = "Andalucía"
        )

    val regionOneStats =
        RegionOneStats(
            region = region,
            stats = stats
        )

    val subRegionStats =
        SubRegionStats(
            subRegion = subRegion,
            stats = stats
        )

    val regionStats =
        RegionStats(
            region = region,
            stats = stats,
            subRegionStats = listOf(subRegionStats)
        )

    val countryOneStats =
        CountryOneStats(
            country = country,
            stats = stats,
            regionStats = listOf(regionStats)
        )

    val countryAndStats =
        CountryAndStats(
            country = country,
            stats = listOf(stats)
        )

    val regionAndStats =
        RegionAndStats(
            region = region,
            stats = listOf(stats)
        )

    val subRegionAndStats =
        SubRegionAndStats(
            subRegion = subRegion,
            stats = listOf(stats)
        )

    val statsChartUI =
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

    val countryOneStatsChartUI =
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

    val covidTracker =
        CovidTracker(
            countriesStats = listOf(countryOneStats),
            worldStats = worldStats
        )

    val covidTrackerUI =
        CovidTrackerUI(
            countriesStats = listOf(countryStatsUI),
            worldStats = worldStatsUI
        )

    val countryPlaceStatsUI =
        PlaceStatsUI(
            id = country.id,
            name = country.name,
            nameEs = country.nameEs,
            code = country.code,
            stats = statsUI
        )

    val placeStatsUI =
        PlaceStatsUI(
            id = "andalucia",
            name = "Andalucia",
            nameEs = "Andalucía",
            stats = statsUI
        )

    val worldStatsChartUI =
        WorldStatsChartUI(
            date = worldStats.date,
            updatedAt = worldStats.updatedAt,
            stats = statsChartUI
        )

    val countryListStatsChartUI =
        CountryListStatsChartUI(
            country = countryUI,
            stats = listOf(statsChartUI)
        )

    private val worldCountryStatsUI =
        WorldCountryStatsUI(
            countryStats = countryOneStatsChartUI,
            worldStats = worldStatsChartUI
        )

    val listWorldCountryStatsUI = listOf(worldCountryStatsUI)

    val listRegionStats =
        ListRegionStats(
            regionStats = listOf(regionStats)
        )

    val listSubRegionStats =
        ListSubRegionStats(
            subRegionStats = listOf(subRegionStats)
        )

    val listRegionStatsPlaceUI = listOf(placeStatsUI)

    val listSubRegionStatsPlaceUI = listOf(placeStatsUI)

    private val placeStatsChartUI =
        PlaceStatsChartUI(
            place = placeUI,
            stats = statsChartUI
        )

    val listRegionPlaceStatsChartUI = listOf(placeStatsChartUI)

    val listSubRegionPlaceStatsChartUI = listOf(placeStatsChartUI)

    val listCountryOnlyStats =
        ListCountryOnlyStats(
            countriesStats = listOf(stats)
        )

    val listRegionOnlyStats =
        ListRegionOnlyStats(
            regionStats = listOf(stats)
        )

    val listCountryOnlyStatsPlaceUI = listOf(statsChartUI)

    val listRegionOnlyStatsPlaceUI = listOf(statsChartUI)

    val listRegionAndStats =
        ListRegionAndStats(
            regionStats = listOf(regionAndStats)
        )

    val listSubRegionAndStats =
        ListSubRegionAndStats(
            subRegionStats = listOf(subRegionAndStats)
        )

    val placeListStatsChartUI =
        listOf(
            PlaceListStatsChartUI(
                place = placeUI,
                stats = listOf(statsChartUI)
            )
        )

    val covidTrackerDateCountryDto =
        CovidTrackerDateCountryDto(
            id = country.id,
            name = country.name,
            nameEs = country.nameEs,
            source = "John Hopkins University",
            todayConfirmed = stats.confirmed,
            todayDeaths = stats.deaths,
            todayNewConfirmed = stats.newConfirmed,
            todayNewDeaths = stats.newDeaths,
            todayNewOpenCases = stats.newOpenCases,
            todayNewRecovered = stats.newRecovered,
            todayOpenCases = stats.openCases,
            todayRecovered = stats.recovered,
            todayVsYesterdayConfirmed = stats.vsYesterdayConfirmed,
            todayVsYesterdayDeaths = stats.vsYesterdayDeaths,
            todayVsYesterdayOpenCases = stats.vsYesterdayOpenCases,
            todayVsYesterdayRecovered = stats.vsYesterdayRecovered,
            yesterdayConfirmed = 10000,
            yesterdayDeaths = 2000,
            yesterdayOpenCases = 3000,
            yesterdayRecovered = 400,
            regions = listOf(
                CovidTrackerDateCountryDto(
                    id = region.id,
                    name = region.name,
                    nameEs = region.nameEs,
                    source = "John Hopkins University",
                    todayConfirmed = stats.confirmed,
                    todayDeaths = stats.deaths,
                    todayNewConfirmed = stats.newConfirmed,
                    todayNewDeaths = stats.newDeaths,
                    todayNewOpenCases = stats.newOpenCases,
                    todayNewRecovered = stats.newRecovered,
                    todayOpenCases = stats.openCases,
                    todayRecovered = stats.recovered,
                    todayVsYesterdayConfirmed = stats.vsYesterdayConfirmed,
                    todayVsYesterdayDeaths = stats.vsYesterdayDeaths,
                    todayVsYesterdayOpenCases = stats.vsYesterdayOpenCases,
                    todayVsYesterdayRecovered = stats.vsYesterdayRecovered,
                    yesterdayConfirmed = 10000,
                    yesterdayDeaths = 2000,
                    yesterdayOpenCases = 3000,
                    yesterdayRecovered = 400,
                    regions = null,
                    subRegions = listOf(
                        CovidTrackerDateCountryDto(
                            id = region.id,
                            name = region.name,
                            nameEs = region.nameEs,
                            source = "John Hopkins University",
                            todayConfirmed = stats.confirmed,
                            todayDeaths = stats.deaths,
                            todayNewConfirmed = stats.newConfirmed,
                            todayNewDeaths = stats.newDeaths,
                            todayNewOpenCases = stats.newOpenCases,
                            todayNewRecovered = stats.newRecovered,
                            todayOpenCases = stats.openCases,
                            todayRecovered = stats.recovered,
                            todayVsYesterdayConfirmed = stats.vsYesterdayConfirmed,
                            todayVsYesterdayDeaths = stats.vsYesterdayDeaths,
                            todayVsYesterdayOpenCases = stats.vsYesterdayOpenCases,
                            todayVsYesterdayRecovered = stats.vsYesterdayRecovered,
                            yesterdayConfirmed = 10000,
                            yesterdayDeaths = 2000,
                            yesterdayOpenCases = 3000,
                            yesterdayRecovered = 400,
                            regions = null,
                            subRegions = null
                        )
                    )
                )
            ),
            subRegions = null
        )

    val covidTrackerTotalDto =
        CovidTrackerTotalDto(
            source = "John Hopkins University",
            todayConfirmed = stats.confirmed,
            todayDeaths = stats.deaths,
            todayNewConfirmed = stats.newConfirmed,
            todayNewDeaths = stats.newDeaths,
            todayNewOpenCases = stats.newOpenCases,
            todayNewRecovered = stats.newRecovered,
            todayOpenCases = stats.openCases,
            todayRecovered = stats.recovered,
            todayVsYesterdayConfirmed = stats.vsYesterdayConfirmed,
            todayVsYesterdayDeaths = stats.vsYesterdayDeaths,
            todayVsYesterdayOpenCases = stats.vsYesterdayOpenCases,
            todayVsYesterdayRecovered = stats.vsYesterdayRecovered,
            yesterdayConfirmed = 10000,
            yesterdayDeaths = 2000,
            yesterdayOpenCases = 3000,
            yesterdayRecovered = 400
        )

    private val covidTrackerDateDto =
        CovidTrackerDateDto(
            countries = mapOf(country.id to covidTrackerDateCountryDto)
        )

    val covidTrackerDto =
        CovidTrackerDto(
            dates = mapOf("2020-10-02" to covidTrackerDateDto),
            total = covidTrackerTotalDto,
            updatedAt = "2020-10-02 22:10UTC",
        )

    val countryEntity =
        CountryEntity(
            id = country.id,
            name = country.name,
            nameEs = country.nameEs,
            code = country.code
        )

    val regionEntity =
        RegionEntity(
            id = region.id,
            name = region.name,
            nameEs = region.nameEs,
            idCountryFk = country.id
        )

    val subRegionEntity =
        SubRegionEntity(
            id = region.id,
            name = region.name,
            nameEs = region.nameEs,
            idRegionFk = region.id,
            idCountryFk = country.id
        )

    val statsEmbedded =
        StatsEmbedded(
            source = stats.source,
            confirmed = stats.confirmed,
            deaths = stats.deaths,
            newConfirmed = stats.newConfirmed,
            newDeaths = stats.newDeaths,
            newOpenCases = stats.newOpenCases,
            newRecovered = stats.newRecovered,
            openCases = stats.openCases,
            recovered = stats.recovered,
            vsYesterdayConfirmed = stats.vsYesterdayConfirmed,
            vsYesterdayDeaths = stats.vsYesterdayDeaths,
            vsYesterdayOpenCases = stats.vsYesterdayOpenCases,
            vsYesterdayRecovered = stats.vsYesterdayRecovered
        )

    val worldStatsEntity =
        WorldStatsEntity(
            dateTimestamp = worldStats.dateTimestamp,
            date = worldStats.date,
            updatedAt = worldStats.updatedAt,
            stats = statsEmbedded
        )

    val countryStatsEntity =
        CountryStatsEntity(
            dateTimestamp = stats.dateTimestamp,
            date = stats.date,
            stats = statsEmbedded,
            idCountryFk = country.id
        )

    val regionStatsEntity =
        RegionStatsEntity(
            dateTimestamp = stats.dateTimestamp,
            date = stats.date,
            stats = statsEmbedded,
            idRegionFk = region.id,
            idCountryFk = country.id
        )

    val subRegionStatsEntity =
        SubRegionStatsEntity(
            dateTimestamp = stats.dateTimestamp,
            date = stats.date,
            stats = statsEmbedded,
            idSubRegionFk = subRegion.id,
            idRegionFk = region.id
        )

    val countryAndStatsDV =
        CountryAndStatsDV(
            country = countryEntity,
            countryStats = countryStatsEntity
        )

    val regionAndStatsDV =
        RegionAndStatsDV(
            region = regionEntity,
            regionStats = regionStatsEntity
        )

    val subRegionAndStatsDV =
        SubRegionAndStatsDV(
            subRegion = subRegionEntity,
            subRegionStats = subRegionStatsEntity
        )

    val worldAndCountriesStatsPojo =
        WorldAndCountriesStatsPojo(
            worldStats = worldStatsEntity,
            countriesStats = listOf(countryAndStatsDV)
        )

    val countryAndStatsPojo =
        CountryAndStatsPojo(
            country = countryEntity,
            stats = listOf(countryStatsEntity)
        )

    val countryAndOneStatsPojo =
        CountryAndOneStatsPojo(
            country = countryEntity,
            countryStats = countryStatsEntity
        )

    val regionAndStatsPojo =
        RegionAndStatsPojo(
            region = regionEntity,
            stats = listOf(regionStatsEntity)
        )

    val subRegionAndStatsPojo =
        SubRegionAndStatsPojo(
            subRegion = subRegionEntity,
            stats = listOf(subRegionStatsEntity)
        )

    val regionAndOneStatsPojo =
        RegionAndOneStatsPojo(
            region = regionEntity,
            regionStats = regionStatsEntity
        )

    val subRegionAndOneStatsPojo =
        SubRegionAndOneStatsPojo(
            subRegion = subRegionEntity,
            subRegionStats = subRegionStatsEntity
        )

    val listWorldStats =
        ListWorldStats(
            worldStats = listOf(worldStats)
        )

    val listCountryAndStats =
        ListCountryAndStats(
            countriesStats = listOf(countryAndStats)
        )

    val listCountry =
        ListCountry(
            countries = listOf(country)
        )

    val listRegion =
        ListRegion(
            regions = listOf(region)
        )

    val listWorldStatsEntity = listOf(worldStatsEntity)

    val listCountryAndStatsPojo = listOf(countryAndStatsPojo)

    val listRegionAndStatsPojo = listOf(regionAndStatsPojo)

    val listCountryStatsEntity = listOf(countryStatsEntity)

    val listRegionStatsEntity = listOf(regionStatsEntity)

    val listSubRegionStatsEntity = listOf(subRegionStatsEntity)

    val listSubRegionAndStatsPojo = listOf(subRegionAndStatsPojo)

    val listCountryEntity = listOf(countryEntity)

    val listRegionEntity = listOf(regionEntity)

    val listRegionAndStatsDV = listOf(regionAndStatsDV)

    val listSubRegionAndStatsDV = listOf(subRegionAndStatsDV)

    val listCountryAndOneStatsPojo = listOf(countryAndOneStatsPojo)

    val listRegionAndOneStatsPojo = listOf(regionAndOneStatsPojo)

    val listSubRegionAndOneStatsPojo = listOf(subRegionAndOneStatsPojo)
}