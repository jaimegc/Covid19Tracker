package com.jaimegc.covid19tracker.domain.model

data class CovidTracker(
    val countriesStats: List<CountryOneStats>,
    val worldStats: WorldStats
)

data class ListWorldStats(
    val worldStats: List<WorldStats>
)

data class WorldStats(
    val date: String,
    val updatedAt: String,
    val stats: Stats
)

data class CountryOneStats(
    val country: Country,
    val stats: Stats,
    val regionStats: List<RegionStats>? = null
)

data class RegionOneStats(
    val region: Region,
    val stats: Stats
)

data class ListCountryAndStats(
    val countriesStats: List<CountryAndStats>
)

data class ListCountryOnlyStats(
    val countriesStats: List<Stats>
)

data class ListRegionOnlyStats(
    val regionStats: List<Stats>
)

data class ListRegionStats(
    val regionStats: List<RegionStats>
)

data class ListSubRegionStats(
    val subRegionStats: List<SubRegionStats>
)

data class ListRegionAndStats(
    val regionStats: List<RegionAndStats>
)

data class ListSubRegionAndStats(
    val subRegionStats: List<SubRegionAndStats>
)

data class ListCountry(
    val countries: List<Country>
)

data class ListRegion(
    val regions: List<Region>
)

data class CountryAndStats(
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

data class RegionAndStats(
    val region: Region,
    val stats: List<Stats>
)

data class SubRegionAndStats(
    val subRegion: SubRegion,
    val stats: List<Stats>
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