package com.jaimegc.covid19tracker.data.room.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity

@DatabaseView("""
    SELECT * FROM country, country_stats 
    WHERE country.id = country_stats.id_country_fk 
    ORDER BY confirmed DESC""")
data class CountryAndStatsDV(
    @Embedded
    val country: CountryEntity?,
    @Embedded
    val countryStats: CountryStatsEntity?
)