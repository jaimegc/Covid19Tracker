package com.jaimegc.covid19tracker.domain.model

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDateCountryDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDateDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerTotalDto
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldAndCountriesPojo
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun CovidTrackerDto.toDomain(): CovidTracker =
    CovidTracker(
        countriesStats = dates.values.first().toDomain(updatedAt),
        worldStats = total.toDomain(updatedAt)
    )

private fun CovidTrackerDateDto.toDomain(updatedAt: String): List<CountryStats> =
    countries.values.map { country -> country.toDomain(updatedAt) }

private fun CovidTrackerDateCountryDto.toDomain(updatedAt: String): CountryStats =
    CountryStats(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        stats = Stats(
            date = date,
            source = source,
            confirmed = todayConfirmed,
            deaths = todayDeaths,
            newConfirmed = todayNewConfirmed,
            newDeaths = todayNewDeaths,
            newOpenCases = todayNewOpenCases,
            newRecovered = todayNewRecovered,
            openCases = todayOpenCases,
            recovered = todayRecovered,
            vsYesterdayConfirmed = todayVsYesterdayConfirmed,
            vsYesterdayDeaths = todayVsYesterdayDeaths,
            vsYesterdayOpenCases = todayVsYesterdayOpenCases,
            vsYesterdayRecovered = todayVsYesterdayRecovered
        )
    )

fun CovidTrackerTotalDto.toDomain(updatedAt: String): WorldStats =
    WorldStats(
        date = date,
        source = source,
        confirmed = todayConfirmed,
        deaths = todayDeaths,
        newConfirmed = todayNewConfirmed,
        newDeaths = todayNewDeaths,
        newOpenCases = todayNewOpenCases,
        newRecovered = todayNewRecovered,
        openCases = todayOpenCases,
        recovered = todayRecovered,
        vsYesterdayConfirmed = todayVsYesterdayConfirmed,
        vsYesterdayDeaths = todayVsYesterdayDeaths,
        vsYesterdayOpenCases = todayVsYesterdayOpenCases,
        vsYesterdayRecovered = todayVsYesterdayRecovered,
        updatedAt = updatedAt
    )

fun WorldAndCountriesPojo.toDomain(): CovidTracker =
    CovidTracker(
        countriesStats = countriesStats.map { countryEntity -> countryEntity.toDomain() },
        worldStats = worldStats!!.toDomain()
    )

private fun WorldStatsEntity.toDomain(): WorldStats =
    WorldStats(
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
        vsYesterdayRecovered = vsYesterdayRecovered,
        updatedAt = updatedAt
    )

private fun CountryStatsEntity.toDomain(): CountryStats =
    CountryStats(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        stats = Stats(
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