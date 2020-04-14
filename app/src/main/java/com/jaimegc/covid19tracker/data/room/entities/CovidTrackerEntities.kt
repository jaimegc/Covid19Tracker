package com.jaimegc.covid19tracker.data.room.entities

import androidx.room.*

@Entity(tableName = "covid_tracker")
data class CovidTrackerEntity(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "update_at")
    val updateAt: String
)

@Entity(
    tableName = "country_today_stats",
    foreignKeys = [ForeignKey(
        entity = CovidTrackerEntity::class,
        parentColumns = arrayOf("date"),
        childColumns = arrayOf("date_covid_tracker_fk"),
        onDelete = ForeignKey.CASCADE
    )])
data class CountryTodayStatsEntity(
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
    @ColumnInfo(name = "updated_at")
    val updatedAt: String,
    @ColumnInfo(name = "date_covid_tracker_fk")
    val dateCovidTrackerFk: String
)

@Entity(
    tableName = "world_today_stats",
    foreignKeys = [ForeignKey(
        entity = CovidTrackerEntity::class,
        parentColumns = arrayOf("date"),
        childColumns = arrayOf("date_covid_tracker_fk"),
        onDelete = ForeignKey.CASCADE
    )])
data class WorldTodayStatsEntity(
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
    val updatedAt: String,
    @ColumnInfo(name = "date_covid_tracker_fk")
    val dateCovidTrackerFk: String
)

data class CovidTrackerAndWorldTodayStatsPojo(
    @Embedded
    val covidTracker: CovidTrackerEntity?,
    @Relation(parentColumn = "date", entityColumn = "date_covid_tracker_fk", entity = CountryTodayStatsEntity::class)
    val countriesStats: List<CountryTodayStatsEntity>,
    @Relation(parentColumn = "date", entityColumn = "date_covid_tracker_fk", entity = WorldTodayStatsEntity::class)
    val worldStats: WorldTodayStatsEntity?
) {
    fun isValid(): Boolean =
        covidTracker != null && worldStats != null && countriesStats.isNotEmpty()
}