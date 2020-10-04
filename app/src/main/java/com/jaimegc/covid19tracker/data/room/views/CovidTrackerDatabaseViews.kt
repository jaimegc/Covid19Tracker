package com.jaimegc.covid19tracker.data.room.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionStatsEntity

@DatabaseView(
    """
    SELECT * FROM country, country_stats 
    WHERE country.id = country_stats.id_country_fk 
    ORDER BY confirmed DESC"""
)
data class CountryAndStatsDV(
    @Embedded
    val country: CountryEntity?,
    @Embedded
    val countryStats: CountryStatsEntity?
)

@DatabaseView(
    """
    SELECT * FROM region r
    LEFT JOIN region_stats s ON r.id = s.id_region_fk AND r.id_country_fk = s.id_region_country_fk
    ORDER BY confirmed DESC"""
)
data class RegionAndStatsDV(
    @Embedded
    val region: RegionEntity?,
    @Embedded
    val regionStats: RegionStatsEntity?
)

@DatabaseView(
    """
    SELECT * FROM sub_region r
    LEFT JOIN sub_region_stats s ON r.id = s.id_sub_region_fk AND r.id_region_fk = s.id_sub_region_region_fk
    ORDER BY confirmed DESC"""
)
data class SubRegionAndStatsDV(
    @Embedded
    val subRegion: SubRegionEntity?,
    @Embedded
    val subRegionStats: SubRegionStatsEntity?
)