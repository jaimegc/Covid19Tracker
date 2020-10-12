package com.jaimegc.covid19tracker.data.room.mapper

import arrow.core.Either
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.StatsEmbedded
import com.jaimegc.covid19tracker.data.room.entities.SubRegionEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.Region
import com.jaimegc.covid19tracker.domain.model.RegionStats
import com.jaimegc.covid19tracker.domain.model.Stats
import com.jaimegc.covid19tracker.domain.model.SubRegion
import com.jaimegc.covid19tracker.domain.model.SubRegionStats
import com.jaimegc.covid19tracker.domain.model.WorldStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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