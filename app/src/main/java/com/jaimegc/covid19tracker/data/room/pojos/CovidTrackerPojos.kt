package com.jaimegc.covid19tracker.data.room.pojos

import androidx.room.Embedded
import androidx.room.Relation
import com.jaimegc.covid19tracker.data.room.dataviews.CountryAndStatsDV
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity

data class WorldAndCountriesStatsPojo(
    @Embedded
    val worldStats: WorldStatsEntity?,
    @Relation(parentColumn = "date", entityColumn = "date")
    val countriesStats: List<CountryAndStatsDV>
) {
    fun isValid(): Boolean =
        worldStats != null && countriesStats.isNotEmpty()
}