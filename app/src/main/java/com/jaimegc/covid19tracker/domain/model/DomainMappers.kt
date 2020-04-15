package com.jaimegc.covid19tracker.domain.model

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDateCountryDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDateDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerTotalDto
import com.jaimegc.covid19tracker.data.room.entities.CountryTodayStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.CovidTrackerAndWorldTodayStatsPojo
import com.jaimegc.covid19tracker.data.room.entities.WorldTodayStatsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun CovidTrackerDto.toDomain(): CovidTracker =
    CovidTracker(
        date = total.date,
        updatedAt = updatedAt,
        countryStats = dates.values.first().toDomain(updatedAt),
        worldStats = total.toDomain(updatedAt)
    )

private fun CovidTrackerDateDto.toDomain(updatedAt: String): CountryStats =
    CountryStats(
        countries = countries.values.map { country -> country.toDomain(updatedAt) }
    )

private fun CovidTrackerDateCountryDto.toDomain(updatedAt: String): Country =
    Country(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        todayStats = TodayStats(
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
    )

fun CovidTrackerTotalDto.toDomain(updatedAt: String): TodayStats =
    TodayStats(
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

fun CovidTrackerAndWorldTodayStatsPojo.toDomain(): CovidTracker =
    CovidTracker(
        date = covidTracker!!.date,
        updatedAt = covidTracker.updateAt,
        countryStats = CountryStats(countriesStats.map {
            countryEntity -> countryEntity.toDomain()
        }),
        worldStats = worldStats!!.toDomain()
    )

private fun WorldTodayStatsEntity.toDomain(): TodayStats =
    TodayStats(
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

private fun CountryTodayStatsEntity.toDomain(): Country =
    Country(
        id = id,
        name = name,
        nameEs = nameEs,
        date = date,
        todayStats = TodayStats(
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