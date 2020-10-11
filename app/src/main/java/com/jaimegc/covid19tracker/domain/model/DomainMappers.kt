package com.jaimegc.covid19tracker.domain.model

import com.jaimegc.covid19tracker.common.extensions.dateToMilliseconds
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
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.WorldAndCountriesStatsPojo
import com.jaimegc.covid19tracker.data.room.views.CountryAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.RegionAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.SubRegionAndStatsDV

fun CovidTrackerDto.toDomain(date: String): CovidTracker =
    CovidTracker(
        countriesStats = dates.values.first().toDomain(date),
        worldStats = total.toDomain(date, updatedAt)
    )

private fun CovidTrackerDateDto.toDomain(date: String): List<CountryOneStats> =
    countries.values.map { country -> country.toDomain(date) }

private fun CovidTrackerDateCountryDto.toDomain(date: String): CountryOneStats =
    CountryOneStats(
        country = Country(
            id = id,
            name = name,
            nameEs = nameEs,
            code = CountryCode(id).code
        ),
        stats = Stats(
            dateTimestamp = date.dateToMilliseconds(),
            date = date,
            source = source ?: "",
            confirmed = todayConfirmed ?: 0L,
            deaths = todayDeaths ?: 0L,
            newConfirmed = todayNewConfirmed ?: 0L,
            newDeaths = todayNewDeaths ?: 0L,
            newOpenCases = todayNewOpenCases ?: 0L,
            newRecovered = todayNewRecovered ?: 0L,
            openCases = todayOpenCases ?: 0L,
            recovered = todayRecovered ?: 0L,
            vsYesterdayConfirmed = todayVsYesterdayConfirmed ?: 0.0,
            vsYesterdayDeaths = todayVsYesterdayDeaths ?: 0.0,
            vsYesterdayOpenCases = todayVsYesterdayOpenCases ?: 0.0,
            vsYesterdayRecovered = todayVsYesterdayRecovered ?: 0.0
        ),
        regionStats = regions?.map { region -> toRegionDomain(region, date) }
    )

fun CovidTrackerTotalDto.toDomain(date: String, updatedAt: String): WorldStats =
    WorldStats(
        dateTimestamp = date.dateToMilliseconds(),
        date = date,
        updatedAt = updatedAt,
        stats = Stats(
            dateTimestamp = date.dateToMilliseconds(),
            date = date,
            source = source ?: "",
            confirmed = todayConfirmed ?: 0L,
            deaths = todayDeaths ?: 0L,
            newConfirmed = todayNewConfirmed ?: 0L,
            newDeaths = todayNewDeaths ?: 0L,
            newOpenCases = todayNewOpenCases ?: 0L,
            newRecovered = todayNewRecovered ?: 0L,
            openCases = todayOpenCases ?: 0L,
            recovered = todayRecovered ?: 0L,
            vsYesterdayConfirmed = todayVsYesterdayConfirmed ?: 0.0,
            vsYesterdayDeaths = todayVsYesterdayDeaths ?: 0.0,
            vsYesterdayOpenCases = todayVsYesterdayOpenCases ?: 0.0,
            vsYesterdayRecovered = todayVsYesterdayRecovered ?: 0.0
        )
    )

fun WorldAndCountriesStatsPojo.toDomain(): CovidTracker =
    CovidTracker(
        countriesStats = countriesStats.map { countryEntity -> countryEntity.toDomain() },
        worldStats = worldStats!!.toDomain()
    )

fun CountryAndStatsPojo.toDomain(): CountryAndStats =
    CountryAndStats(
        country = country!!.toDomain(),
        stats = stats.map { countryStats -> countryStats.toDomain() }
    )

fun RegionAndStatsPojo.toDomain(): RegionAndStats =
    RegionAndStats(
        region = region!!.toDomain(),
        stats = stats.map { regionStats -> regionStats.toDomain(regionStats.date) }
    )

fun SubRegionAndStatsPojo.toDomain(): SubRegionAndStats =
    SubRegionAndStats(
        subRegion = subRegion!!.toDomain(),
        stats = stats.map { regionStats -> regionStats.toDomain(regionStats.date) }
    )

fun List<CountryEntity>.toDomain(): ListCountry =
    ListCountry(
        map { entity -> entity.toDomain() }
    )

fun List<RegionEntity>.toDomain(): ListRegion =
    ListRegion(
        map { entity -> entity.toDomain() }
    )

fun CountryAndOneStatsPojo.toDomain(): CountryOneStats =
    CountryOneStats(
        country = country!!.toDomain(),
        stats = countryStats!!.toDomain()
    )

fun RegionAndOneStatsPojo.toDomain(): RegionOneStats =
    RegionOneStats(
        region = region!!.toDomain(),
        stats = regionStats!!.toDomain(regionStats.date)
    )

private fun CountryAndStatsDV.toDomain(): CountryOneStats =
    CountryOneStats(
        country = country!!.toDomain(),
        stats = countryStats!!.toDomain()
    )

fun List<WorldStatsEntity>.toDomain(): ListWorldStats =
    ListWorldStats(map { entity -> entity.toDomain() })

fun WorldStatsEntity.toDomain(): WorldStats =
    WorldStats(
        dateTimestamp = date.dateToMilliseconds(),
        date = date,
        updatedAt = updatedAt,
        stats = stats.toDomain(date)
    )

fun List<CountryAndStatsPojo>.toDomain(): ListCountryAndStats =
    ListCountryAndStats(map { entity -> entity.toDomain() })

fun List<RegionAndStatsPojo>.toDomain(): ListRegionAndStats =
    ListRegionAndStats(map { entity -> entity.toDomain() })

fun List<SubRegionAndStatsPojo>.toDomain(): ListSubRegionAndStats =
    ListSubRegionAndStats(map { entity -> entity.toDomain() })

fun List<CountryStatsEntity>.toStatsDomain(): ListCountryOnlyStats =
    ListCountryOnlyStats(map { entity -> entity.toDomain() })

fun List<RegionStatsEntity>.toStatsDomain(): ListRegionOnlyStats =
    ListRegionOnlyStats(map { entity -> entity.toDomain(entity.date) })

fun List<SubRegionAndStatsDV>.toDomain(): ListSubRegionStats =
    ListSubRegionStats(
        map { entity ->
            SubRegionStats(
                subRegion = entity.subRegion!!.toDomain(),
                stats = entity.subRegionStats!!.toDomain(entity.subRegionStats.date)
            )
        }
    )

fun List<RegionAndStatsDV>.toDomain(): ListRegionStats =
    ListRegionStats(
        map { entity ->
            RegionStats(
                region = entity.region!!.toDomain(),
                stats = entity.regionStats!!.toDomain(entity.regionStats.date)
            )
        }
    )

fun List<RegionStatsEntity>.toRegionDomain(): List<Stats> =
    map { entity -> entity.toDomain(entity.date) }

fun List<SubRegionStatsEntity>.toSubRegionDomain(): List<Stats> =
    map { entity -> entity.toDomain(entity.date) }

fun RegionStatsEntity.toDomain(date: String): Stats =
    stats.toDomain(date)

fun SubRegionStatsEntity.toDomain(date: String): Stats =
    stats.toDomain(date)

fun CountryEntity.toDomain(): Country =
    Country(
        id = id,
        name = name,
        nameEs = nameEs,
        code = code
    )

fun RegionEntity.toDomain(): Region =
    Region(
        id = id,
        name = name,
        nameEs = nameEs
    )

fun SubRegionEntity.toDomain(): SubRegion =
    SubRegion(
        id = id,
        name = name,
        nameEs = nameEs
    )

fun StatsEmbedded.toDomain(date: String): Stats =
    Stats(
        dateTimestamp = date.dateToMilliseconds(),
        date = date,
        source = source,
        confirmed = confirmed,
        deaths = deaths,
        newConfirmed = newConfirmed,
        newDeaths = newDeaths,
        newOpenCases = newOpenCases,
        newRecovered = newRecovered,
        openCases = openCases,
        recovered = recovered,
        vsYesterdayConfirmed = vsYesterdayConfirmed,
        vsYesterdayDeaths = vsYesterdayDeaths,
        vsYesterdayOpenCases = vsYesterdayOpenCases,
        vsYesterdayRecovered = vsYesterdayRecovered
    )

private fun toRegionDomain(stats: CovidTrackerDateCountryDto, date: String): RegionStats =
    RegionStats(
        region = Region(
            id = stats.id,
            name = stats.name,
            nameEs = stats.nameEs
        ),
        stats = Stats(
            dateTimestamp = date.dateToMilliseconds(),
            date = date,
            source = stats.source ?: "",
            confirmed = stats.todayConfirmed ?: 0L,
            deaths = stats.todayDeaths ?: 0L,
            newConfirmed = stats.todayNewConfirmed ?: 0L,
            newDeaths = stats.todayNewDeaths ?: 0L,
            newOpenCases = stats.todayNewOpenCases ?: 0L,
            newRecovered = stats.todayNewRecovered ?: 0L,
            openCases = stats.todayOpenCases ?: 0L,
            recovered = stats.todayRecovered ?: 0L,
            vsYesterdayConfirmed = stats.todayVsYesterdayConfirmed ?: 0.0,
            vsYesterdayDeaths = stats.todayVsYesterdayDeaths ?: 0.0,
            vsYesterdayOpenCases = stats.todayVsYesterdayOpenCases ?: 0.0,
            vsYesterdayRecovered = stats.todayVsYesterdayRecovered ?: 0.0
        ),
        subRegionStats = stats.subRegions?.map { region -> toSubRegionDomain(region, date) }
    )

private fun toSubRegionDomain(stats: CovidTrackerDateCountryDto, date: String): SubRegionStats =
    SubRegionStats(
        subRegion = SubRegion(
            id = stats.id,
            name = stats.name,
            nameEs = stats.nameEs
        ),
        stats = Stats(
            dateTimestamp = date.dateToMilliseconds(),
            date = date,
            source = stats.source ?: "",
            confirmed = stats.todayConfirmed ?: 0L,
            deaths = stats.todayDeaths ?: 0L,
            newConfirmed = stats.todayNewConfirmed ?: 0L,
            newDeaths = stats.todayNewDeaths ?: 0L,
            newOpenCases = stats.todayNewOpenCases ?: 0L,
            newRecovered = stats.todayNewRecovered ?: 0L,
            openCases = stats.todayOpenCases ?: 0L,
            recovered = stats.todayRecovered ?: 0L,
            vsYesterdayConfirmed = stats.todayVsYesterdayConfirmed ?: 0.0,
            vsYesterdayDeaths = stats.todayVsYesterdayDeaths ?: 0.0,
            vsYesterdayOpenCases = stats.todayVsYesterdayOpenCases ?: 0.0,
            vsYesterdayRecovered = stats.todayVsYesterdayRecovered ?: 0.0
        )
    )

fun CountryStatsEntity.toDomain(): Stats =
    Stats(
        dateTimestamp = date.dateToMilliseconds(),
        date = date,
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