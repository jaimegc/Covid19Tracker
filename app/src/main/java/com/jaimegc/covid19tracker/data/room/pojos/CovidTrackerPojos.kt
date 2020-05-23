package com.jaimegc.covid19tracker.data.room.pojos

import androidx.room.Embedded
import androidx.room.Relation
import com.jaimegc.covid19tracker.data.room.entities.*
import com.jaimegc.covid19tracker.data.room.views.CountryAndStatsDV

data class WorldAndCountriesStatsPojo(
    @Embedded
    val worldStats: WorldStatsEntity?,
    @Relation(parentColumn = "date", entityColumn = "date")
    val countriesStats: List<CountryAndStatsDV>
) {
    fun isValid(): Boolean =
        worldStats != null && countriesStats.isNotEmpty()
}

data class CountryAndStatsPojo(
    @Embedded
    val country: CountryEntity?,
    @Relation(parentColumn = "id", entityColumn = "id_country_fk")
    val stats: List<CountryStatsEntity>
)

class CountryAndOneStatsPojo(
    @Embedded
    val country: CountryEntity?,
    @Embedded
    val countryStats: CountryStatsEntity?
) {
    fun isValid(): Boolean =
        country != null && countryStats != null
}

data class RegionAndStatsPojo(
    @Embedded
    val region: RegionEntity?,
    @Relation(parentColumn = "id", entityColumn = "id_region_fk")
    val stats: List<RegionStatsEntity>
)

class RegionAndOneStatsPojo(
    @Embedded
    val region: RegionEntity?,
    @Embedded
    val regionStats: RegionStatsEntity?
) {
    fun isValid(): Boolean =
        region != null && regionStats != null
}

data class SubRegionAndStatsPojo(
    @Embedded
    val subRegion: SubRegionEntity?,
    @Relation(parentColumn = "id", entityColumn = "id_sub_region_fk")
    val stats: List<SubRegionStatsEntity>
)