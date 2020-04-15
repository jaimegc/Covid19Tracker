package com.jaimegc.covid19tracker.data.room.entities

import androidx.room.*

@Entity(tableName = "world_stats")
data class WorldStatsEntity(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "confirmed")
    val confirmed: Long,
    @ColumnInfo(name = "deaths")
    val deaths: Long,
    @ColumnInfo(name = "new_confirmed")
    val newConfirmed: Long,
    @ColumnInfo(name = "new_deaths")
    val newDeaths: Long,
    @ColumnInfo(name = "new_open_cases")
    val newOpenCases: Long,
    @ColumnInfo(name = "new_recovered")
    val newRecovered: Long,
    @ColumnInfo(name = "open_cases")
    val openCases: Long,
    @ColumnInfo(name = "recovered")
    val recovered: Long,
    @ColumnInfo(name = "vs_yesterday_confirmed")
    val vsYesterdayConfirmed: Double,
    @ColumnInfo(name = "vs_yesterday_deaths")
    val vsYesterdayDeaths: Double,
    @ColumnInfo(name = "vs_yesterday_open_cases")
    val vsYesterdayOpenCases: Double,
    @ColumnInfo(name = "vs_yesterday_recovered")
    val vsYesterdayRecovered: Double,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)

@Entity(
    tableName = "country_stats",
    foreignKeys = [ForeignKey(
        entity = WorldStatsEntity::class,
        parentColumns = arrayOf("date"),
        childColumns = arrayOf("date_world_stats_fk"),
        onDelete = ForeignKey.CASCADE
    )])
data class CountryStatsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "name_es")
    val nameEs: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "confirmed")
    val confirmed: Long,
    @ColumnInfo(name = "deaths")
    val deaths: Long,
    @ColumnInfo(name = "new_confirmed")
    val newConfirmed: Long,
    @ColumnInfo(name = "new_deaths")
    val newDeaths: Long,
    @ColumnInfo(name = "new_open_cases")
    val newOpenCases: Long,
    @ColumnInfo(name = "new_recovered")
    val newRecovered: Long,
    @ColumnInfo(name = "open_cases")
    val openCases: Long,
    @ColumnInfo(name = "recovered")
    val recovered: Long,
    @ColumnInfo(name = "vs_yesterday_confirmed")
    val vsYesterdayConfirmed: Double,
    @ColumnInfo(name = "vs_yesterday_deaths")
    val vsYesterdayDeaths: Double,
    @ColumnInfo(name = "vs_yesterday_open_cases")
    val vsYesterdayOpenCases: Double,
    @ColumnInfo(name = "vs_yesterday_recovered")
    val vsYesterdayRecovered: Double,
    @ColumnInfo(name = "date_world_stats_fk")
    val dateWorldStatsFk: String
)

data class WorldAndCountriesPojo(
    @Embedded
    val worldStats: WorldStatsEntity?,
    @Relation(parentColumn = "date", entityColumn = "date_world_stats_fk", entity = CountryStatsEntity::class)
    val countriesStats: List<CountryStatsEntity>
) {
    fun isValid(): Boolean =
        worldStats != null && countriesStats.isNotEmpty()
}