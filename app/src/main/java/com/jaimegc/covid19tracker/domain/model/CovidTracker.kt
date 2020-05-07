package com.jaimegc.covid19tracker.domain.model

data class CovidTracker(
    val countriesStats: List<CountryStats>,
    val worldStats: WorldStats
)

data class WorldStats(
    val date: String,
    val updatedAt: String,
    val stats: Stats
)

data class CountryStats(
    val country: Country,
    val stats: Stats,
    val regionStats: List<RegionStats>? = null
)

data class CountryListStats(
    val country: Country,
    val stats: List<Stats>
)

data class Country(
    val id: String,
    val name: String,
    val nameEs: String,
    val code: String
)

data class Region(
    val id: String,
    val name: String,
    val nameEs: String
)

data class SubRegion(
    val id: String,
    val name: String,
    val nameEs: String
)

data class RegionStats(
    val region: Region,
    val stats: Stats,
    val subRegionStats: List<SubRegionStats>? = null
)

data class SubRegionStats(
    val subRegion: SubRegion,
    val stats: Stats
)

data class Stats(
    val date: String,
    val source: String,
    val confirmed: Long,
    val deaths: Long,
    val newConfirmed: Long,
    val newDeaths: Long,
    val newOpenCases: Long,
    val newRecovered: Long,
    val openCases: Long,
    val recovered: Long,
    val vsYesterdayConfirmed: Double,
    val vsYesterdayDeaths: Double,
    val vsYesterdayOpenCases: Double,
    val vsYesterdayRecovered: Double
)


