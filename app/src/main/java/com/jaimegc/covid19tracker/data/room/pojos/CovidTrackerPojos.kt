package com.jaimegc.covid19tracker.data.room.pojos

import androidx.room.Embedded
import androidx.room.Relation
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
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

data class CountryAndOneStatsPojo(
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

data class SubRegionAndStatsPojo(
    @Embedded
    val subRegion: SubRegionEntity?,
    @Relation(parentColumn = "id", entityColumn = "id_sub_region_fk")
    val stats: List<SubRegionStatsEntity>
)

data class RegionAndOneStatsPojo(
    @Embedded
    val region: RegionEntity?,
    @Embedded
    val regionStats: RegionStatsEntity?
) {
    fun isValid(): Boolean =
        region != null && regionStats != null
}

data class SubRegionAndOneStatsPojo(
    @Embedded
    val subRegion: SubRegionEntity?,
    @Embedded
    val subRegionStats: SubRegionStatsEntity?
) {
    fun isValid(): Boolean =
        subRegion != null && subRegionStats != null
}