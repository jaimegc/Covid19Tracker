package com.jaimegc.covid19tracker.domain.model

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDateCountryDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDateDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerTotalDto
import com.jaimegc.covid19tracker.data.room.views.CountryAndStatsDV
import com.jaimegc.covid19tracker.data.room.entities.*
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.WorldAndCountriesStatsPojo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun CovidTrackerDto.toDomain(date: String): CovidTracker =
    CovidTracker(
        countriesStats = dates.values.first().toDomain(date),
        worldStats = total.toDomain(date, updatedAt)
    )

private fun CovidTrackerDateDto.toDomain(date: String): List<CountryStats> =
    countries.values.map { country -> country.toDomain(date) }

private fun CovidTrackerDateCountryDto.toDomain(date: String): CountryStats =
    CountryStats(
        id = id,
        name = name,
        nameEs = nameEs,
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

fun CovidTrackerTotalDto.toDomain(date: String, updatedAt: String): WorldStats =
    WorldStats(
        date = date,
        updatedAt = updatedAt,
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

fun WorldAndCountriesStatsPojo.toDomain(): CovidTracker =
    CovidTracker(
        countriesStats = countriesStats.map { countryEntity -> countryEntity.toDomain() },
        worldStats = worldStats!!.toDomain()
    )

fun CountryAndStatsPojo.toDomainOnly(): CountryStats =
    CountryStats(
        id = country!!.id,
        name = country.name,
        nameEs = country.nameEs,
        stats = stats[0].toDomain()
    )

fun CountryAndStatsPojo.toDomain(): CountryListStats =
    CountryListStats(
        id = country!!.id,
        name = country.name,
        nameEs = country.nameEs,
        stats = stats.map { countryStats -> countryStats.toDomain() }
    )

fun List<CountryAndOneStatsPojo>.toPojoOrdered(): List<CountryAndStatsPojo> =
    this.groupBy { it.country }.let { mapCountries ->
        val listCountryAndStatsPojo = mutableListOf<CountryAndStatsPojo>()
        mapCountries.map { countryStats ->
            listCountryAndStatsPojo.add(CountryAndStatsPojo(
                countryStats.key, countryStats.value.map { stats -> stats.stats }))
        }
        listCountryAndStatsPojo
    }

private fun CountryAndStatsDV.toDomain(): CountryStats =
    CountryStats(
        id = country!!.id,
        name = country.name,
        nameEs = country.nameEs,
        stats = stats!!.toDomain()
    )

fun WorldStatsEntity.toDomain(): WorldStats =
    WorldStats(
        date = date,
        updatedAt = updatedAt,
        stats = stats.toDomain(date)
    )

private fun CountryEntity.toDomain(stats: StatsEntity): CountryStats =
    CountryStats(
        id = id,
        name = name,
        nameEs = nameEs,
        stats = stats.toDomain()
    )

fun StatsEmbedded.toDomain(date: String): Stats =
    Stats(
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

fun CountryStats.toEntity(): CountryEntity =
    CountryEntity(
        id = id,
        name = name,
        nameEs = nameEs
    )

fun WorldStats.toEntity(): WorldStatsEntity =
    WorldStatsEntity(
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

fun Stats.toEntity(idCountryFk: String): StatsEntity =
    StatsEntity(
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

fun StatsEntity.toDomain(): Stats =
    Stats(
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

fun <T, S, R> mapEntityValid2(parse: Flow<T?>, mapper2: (S) -> Flow<T>, mapper: (T) -> Pair<Boolean, S>): Flow<Either<DomainError, S>> =
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