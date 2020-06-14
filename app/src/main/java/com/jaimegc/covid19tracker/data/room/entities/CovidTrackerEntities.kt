package com.jaimegc.covid19tracker.data.room.entities

import androidx.room.*

@Entity(tableName = "world_stats")
data class WorldStatsEntity(
    @PrimaryKey
    @ColumnInfo(name = "date_timestamp")
    val dateTimestamp: Long,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String,
    @Embedded
    val stats: StatsEmbedded
)

data class StatsEmbedded(
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
    val vsYesterdayRecovered: Double
)

@Entity(
    tableName = "country",
    indices = [(Index(value = ["name"]))])
data class CountryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "name_es")
    val nameEs: String,
    @ColumnInfo(name = "code")
    val code: String
)

@Entity(
    tableName = "country_stats",
    indices = [(Index(value = ["id_country_fk", "confirmed", "deaths", "open_cases", "recovered"]))],
    primaryKeys = ["date_timestamp", "id_country_fk"],
    foreignKeys = [ForeignKey(
        entity = CountryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id_country_fk"),
        onDelete = ForeignKey.CASCADE
    )])
data class CountryStatsEntity(
    @ColumnInfo(name = "date_timestamp")
    val dateTimestamp: Long,
    @ColumnInfo(name = "date")
    val date: String,
    @Embedded
    val stats: StatsEmbedded,
    @ColumnInfo(name = "id_country_fk")
    val idCountryFk: String
)

@Entity(
    tableName = "region",
    indices = [(Index(value = ["id_country_fk", "name"]))],
    primaryKeys = ["id", "id_country_fk"],
    foreignKeys = [ForeignKey(
        entity = CountryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id_country_fk"),
        onDelete = ForeignKey.CASCADE
    )])
data class RegionEntity(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "name_es")
    val nameEs: String,
    @ColumnInfo(name = "id_country_fk")
    val idCountryFk: String
)

@Entity(
    tableName = "region_stats",
    indices = [(Index(value = ["id_region_fk", "id_region_country_fk", "confirmed",
        "deaths", "open_cases", "recovered"]))],
    primaryKeys = ["date_timestamp", "id_region_fk", "id_region_country_fk"],
    foreignKeys = [ForeignKey(
        entity = RegionEntity::class,
        parentColumns = arrayOf("id", "id_country_fk"),
        childColumns = arrayOf("id_region_fk", "id_region_country_fk"),
        onDelete = ForeignKey.CASCADE
    )])
data class RegionStatsEntity(
    @ColumnInfo(name = "date_timestamp")
    val dateTimestamp: Long,
    @ColumnInfo(name = "date")
    val date: String,
    @Embedded
    val stats: StatsEmbedded,
    @ColumnInfo(name = "id_region_fk")
    val idRegionFk: String,
    @ColumnInfo(name = "id_region_country_fk")
    val idCountryFk: String
)

@Entity(
    tableName = "sub_region",
    indices = [(Index(value = ["id_region_fk", "id_country_fk", "name"]))],
    primaryKeys = ["id", "id_region_fk"],
    foreignKeys = [ForeignKey(
        entity = RegionEntity::class,
        parentColumns = arrayOf("id", "id_country_fk"),
        childColumns = arrayOf("id_region_fk", "id_country_fk"),
        onDelete = ForeignKey.CASCADE
    )])
data class SubRegionEntity(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "name_es")
    val nameEs: String,
    @ColumnInfo(name = "id_region_fk")
    val idRegionFk: String,
    @ColumnInfo(name = "id_country_fk")
    val idCountryFk: String
)

@Entity(
    tableName = "sub_region_stats",
    indices = [(Index(value = ["id_sub_region_fk", "id_sub_region_region_fk", "confirmed",
        "deaths", "open_cases", "recovered"]))],
    primaryKeys = ["date_timestamp", "id_sub_region_fk", "id_sub_region_region_fk"],
    foreignKeys = [ForeignKey(
        entity = SubRegionEntity::class,
        parentColumns = arrayOf("id", "id_region_fk"),
        childColumns = arrayOf("id_sub_region_fk", "id_sub_region_region_fk"),
        onDelete = ForeignKey.CASCADE
    )])
data class SubRegionStatsEntity(
    @ColumnInfo(name = "date_timestamp")
    val dateTimestamp: Long,
    @ColumnInfo(name = "date")
    val date: String,
    @Embedded
    val stats: StatsEmbedded,
    @ColumnInfo(name = "id_sub_region_fk")
    val idSubRegionFk: String,
    @ColumnInfo(name = "id_sub_region_region_fk")
    val idRegionFk: String
)