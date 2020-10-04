package com.jaimegc.covid19tracker.domain.model

import arrow.core.Either
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
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.WorldAndCountriesStatsPojo
import com.jaimegc.covid19tracker.data.room.views.CountryAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.RegionAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.SubRegionAndStatsDV
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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

fun List<CountryAndOneStatsPojo>.toPojoCountriesOrdered(): List<CountryAndStatsPojo> =
    this.groupBy { it.country }.let { mapCountries ->
        val listCountryAndStatsPojo = mutableListOf<CountryAndStatsPojo>()
        mapCountries.map { countryStats ->
            listCountryAndStatsPojo.add(
                CountryAndStatsPojo(
                    countryStats.key,
                    countryStats.value.map { stats -> stats.countryStats!! }
                )
            )
        }
        listCountryAndStatsPojo
    }

fun List<RegionAndOneStatsPojo>.toPojoRegionsOrdered(): List<RegionAndStatsPojo> =
    this.groupBy { it.region }.let { mapRegions ->
        val listRegionAndStatsPojo = mutableListOf<RegionAndStatsPojo>()
        mapRegions.map { regionStats ->
            listRegionAndStatsPojo.add(
                RegionAndStatsPojo(
                    regionStats.key,
                    regionStats.value.map { stats -> stats.regionStats!! }
                )
            )
        }
        listRegionAndStatsPojo
    }

fun List<SubRegionAndOneStatsPojo>.toPojoSubRegionsOrdered(): List<SubRegionAndStatsPojo> =
    this.groupBy { it.subRegion }.let { mapSubRegions ->
        val listSubRegionAndStatsPojo = mutableListOf<SubRegionAndStatsPojo>()
        mapSubRegions.map { subRegionStats ->
            listSubRegionAndStatsPojo.add(
                SubRegionAndStatsPojo(
                    subRegionStats.key,
                    subRegionStats.value.map { stats -> stats.subRegionStats!! }
                )
            )
        }
        listSubRegionAndStatsPojo
    }

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

fun List<RegionAndStatsPojo>.toPojoRegionDomain(): ListRegionAndStats =
    ListRegionAndStats(
        map { entity ->
            RegionAndStats(
                region = entity.region!!.toDomain(),
                stats = entity.stats.toRegionDomain()
            )
        }
    )

fun List<SubRegionAndStatsPojo>.toPojoSubRegionDomain(): ListSubRegionAndStats =
    ListSubRegionAndStats(
        map { entity ->
            SubRegionAndStats(
                subRegion = entity.subRegion!!.toDomain(),
                stats = entity.stats.toSubRegionDomain()
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

fun CountryOneStats.toEntity(): CountryEntity =
    CountryEntity(
        id = country.id,
        name = country.name,
        nameEs = country.nameEs,
        code = country.code
    )

fun WorldStats.toEntity(): WorldStatsEntity =
    WorldStatsEntity(
        dateTimestamp = dateTimestamp,
        date = date,
        updatedAt = updatedAt,
        stats = stats.toEmbedded()
    )

fun Stats.toEmbedded(): StatsEmbedded =
    StatsEmbedded(
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

fun Stats.toEntity(idCountryFk: String): CountryStatsEntity =
    CountryStatsEntity(
        dateTimestamp = dateTimestamp,
        date = date,
        stats = StatsEmbedded(
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
        ),
        idCountryFk = idCountryFk
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

fun Region.toEntity(idCountryFk: String): RegionEntity =
    RegionEntity(
        id = id,
        name = name,
        nameEs = nameEs,
        idCountryFk = idCountryFk
    )

fun SubRegion.toEntity(idRegionFk: String, idCountryFk: String): SubRegionEntity =
    SubRegionEntity(
        id = id,
        name = name,
        nameEs = nameEs,
        idRegionFk = idRegionFk,
        idCountryFk = idCountryFk
    )

fun RegionStats.toEntity(idRegionFk: String, idCountryFk: String): RegionStatsEntity =
    RegionStatsEntity(
        dateTimestamp = stats.dateTimestamp,
        date = stats.date,
        stats = StatsEmbedded(
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
        ),
        idRegionFk = idRegionFk,
        idCountryFk = idCountryFk
    )

fun SubRegionStats.toEntity(idSubRegionFk: String, idRegionFk: String): SubRegionStatsEntity =
    SubRegionStatsEntity(
        dateTimestamp = stats.dateTimestamp,
        date = stats.date,
        stats = StatsEmbedded(
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
        ),
        idSubRegionFk = idSubRegionFk,
        idRegionFk = idRegionFk
    )

fun <T, R> mapEntityValid(parse: Flow<T?>, mapper: (T) -> Pair<Boolean, R>): Flow<Either<DomainError, R>> =
    try {
        parse.map {
            it?.let {
                when (mapper(it).first) {
                    true -> Either.right(mapper(it).second)
                    else -> Either.left(DomainError.DatabaseEmptyData)
                }
            } ?: Either.left(DomainError.DatabaseEmptyData)
        }
    } catch (exception: Exception) {
        flow { Either.left(DomainError.DatabaseDomainError(exception.toString())) }
    }

fun <T, S, R> mapEntityMenuValid(
    parse: Flow<T?>,
    mapper: (T) -> Pair<Boolean, Pair<S, R>>
): Flow<Either<DomainError, Pair<S, R>>> =
    try {
        parse.map {
            it?.let {
                when (mapper(it).first) {
                    true -> Either.right(mapper(it).second)
                    else -> Either.left(DomainError.DatabaseEmptyData)
                }
            } ?: Either.left(DomainError.DatabaseEmptyData)
        }
    } catch (exception: Exception) {
        flow { Either.left(DomainError.DatabaseDomainError(exception.toString())) }
    }

fun <T, R> mapEntity(flow: Flow<T>, mapper: (T) -> R): Flow<Either<DomainError, R>> =
    try {
        flow.map { Either.right(mapper(it)) }
    } catch (exception: Exception) {
        flow { Either.left(DomainError.DatabaseDomainError(exception.toString())) }
    }