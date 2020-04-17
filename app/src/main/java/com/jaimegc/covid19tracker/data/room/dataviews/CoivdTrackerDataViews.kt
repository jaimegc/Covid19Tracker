package com.jaimegc.covid19tracker.data.room.dataviews

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.StatsEntity

@DatabaseView("SELECT *, * FROM country, stats WHERE country.id = stats.id_country_fk")
data class CountryAndStatsDV(
    @Embedded
    val country: CountryEntity?,
    @Embedded
    val stats: StatsEntity?
)