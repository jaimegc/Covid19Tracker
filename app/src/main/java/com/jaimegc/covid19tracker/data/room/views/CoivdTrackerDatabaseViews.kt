package com.jaimegc.covid19tracker.data.room.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.StatsEntity

@DatabaseView("""
    SELECT * FROM country, stats 
    WHERE country.id = stats.id_country_fk 
    ORDER BY confirmed DESC""")
data class CountryAndStatsDV(
    @Embedded
    val country: CountryEntity?,
    @Embedded
    val stats: StatsEntity?
)

@DatabaseView("""
    SELECT * FROM country, stats 
    WHERE country.id = stats.id_country_fk 
    ORDER BY confirmed DESC LIMIT 6""")
data class CountryAndStatsOrderByConfirmedDV(
    @Embedded
    val country: CountryEntity?,
    @Embedded
    val stats: StatsEntity?
)

@DatabaseView("""
    SELECT * FROM country, stats 
    WHERE country.id = stats.id_country_fk 
    ORDER BY deaths DESC LIMIT 6""")
data class CountryAndStatsOrderByDeathsDV(
    @Embedded
    val country: CountryEntity?,
    @Embedded
    val stats: StatsEntity?
)