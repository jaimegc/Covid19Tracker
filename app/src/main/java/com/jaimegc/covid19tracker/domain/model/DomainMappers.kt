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
            date = date,
            source = source ?: "",
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
        ),
        regionStats = regions?.map { region -> toRegionDomain(region, date) }
    )

fun CovidTrackerTotalDto.toDomain(date: String, updatedAt: String): WorldStats =
    WorldStats(
        date = date,
        updatedAt = updatedAt,
        stats = Stats(
            date = date,
            source = source ?: "",
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

fun CountryAndStatsPojo.toDomain(): CountryStats =
    CountryStats(
        country = country!!.toDomain(),
        stats = stats.map { countryStats -> countryStats.toDomain() }
    )

fun List<CountryAndOneStatsPojo>.toPojoOrdered(): List<CountryAndStatsPojo> =
    this.groupBy { it.country }.let { mapCountries ->
        val listCountryAndStatsPojo = mutableListOf<CountryAndStatsPojo>()
        mapCountries.map { countryStats ->
            listCountryAndStatsPojo.add(CountryAndStatsPojo(
                countryStats.key, countryStats.value.map { stats -> stats.countryStats }))
        }
        listCountryAndStatsPojo
    }

private fun CountryAndStatsDV.toDomain(): CountryOneStats =
    CountryOneStats(
        country = country!!.toDomain(),
        stats = countryStats!!.toDomain()
    )

fun List<WorldStatsEntity>.toDomain(): ListWorldStats =
    ListWorldStats(map { entitiy -> entitiy.toDomain() })

fun WorldStatsEntity.toDomain(): WorldStats =
    WorldStats(
        date = date,
        updatedAt = updatedAt,
        stats = stats.toDomain(date)
    )

fun List<CountryAndStatsPojo>.toDomain(): ListCountryStats =
    ListCountryStats(map { entitiy -> entitiy.toDomain() })

fun CountryEntity.toDomain(): Country =
    Country(
        id = id,
        name = name,
        nameEs = nameEs,
        code = code
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

private fun toRegionDomain(stats: CovidTrackerDateCountryDto, date: String): RegionStats =
    RegionStats(
        region = Region(
            id = stats.id,
            name = stats.name,
            nameEs = stats.nameEs
        ),
        stats = Stats(
            date = date,
            source = stats.source ?: "",
            confirmed = stats.todayConfirmed,
            deaths = stats.todayDeaths,
            newConfirmed = stats.todayNewConfirmed,
            newDeaths = stats.todayNewDeaths,
            newOpenCases = stats.todayNewOpenCases,
            newRecovered = stats.todayNewRecovered,
            openCases = stats.todayOpenCases,
            recovered = stats.todayRecovered,
            vsYesterdayConfirmed = stats.todayVsYesterdayConfirmed,
            vsYesterdayDeaths = stats.todayVsYesterdayDeaths,
            vsYesterdayOpenCases = stats.todayVsYesterdayOpenCases,
            vsYesterdayRecovered = stats.todayVsYesterdayRecovered
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
            date = date,
            source = stats.source ?: "",
            confirmed = stats.todayConfirmed,
            deaths = stats.todayDeaths,
            newConfirmed = stats.todayNewConfirmed,
            newDeaths = stats.todayNewDeaths,
            newOpenCases = stats.todayNewOpenCases,
            newRecovered = stats.todayNewRecovered,
            openCases = stats.todayOpenCases,
            recovered = stats.todayRecovered,
            vsYesterdayConfirmed = stats.todayVsYesterdayConfirmed,
            vsYesterdayDeaths = stats.todayVsYesterdayDeaths,
            vsYesterdayOpenCases = stats.todayVsYesterdayOpenCases,
            vsYesterdayRecovered = stats.todayVsYesterdayRecovered
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