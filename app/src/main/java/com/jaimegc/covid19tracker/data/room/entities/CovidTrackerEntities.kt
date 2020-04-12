package com.jaimegc.covid19tracker.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "covid_tracker_total")
data class CovidTrackerTotalEntity(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "source")
    val source: Long,
    @ColumnInfo(name = "today_confirmed")
    val todayConfirmed: Long,
    @ColumnInfo(name = "today_deaths")
    val todayDeaths: Long,
    @ColumnInfo(name = "today_new_confirmed")
    val todayNewConfirmed: Long,
    @ColumnInfo(name = "today_new_deaths")
    val todayNewDeaths: Long,
    @ColumnInfo(name = "today_new_open_cases")
    val todayNewOpenCases: Long,
    @ColumnInfo(name = "today_new_recovered")
    val todayNewRecovered: Long,
    @ColumnInfo(name = "today_open_cases")
    val todayOpenCases: Long,
    @ColumnInfo(name = "today_recovered")
    val todayRecovered: Long,
    @ColumnInfo(name = "today_vs_yesterday_confirmed")
    val todayVsYesterdayConfirmed: Double,
    @ColumnInfo(name = "today_vs_yesterday_deaths")
    val todayVsYesterdayDeaths: Double,
    @ColumnInfo(name = "today_vs_yesterday_open_cases")
    val todayVsYesterdayOpenCases: Double,
    @ColumnInfo(name = "today_vs_yesterday_recovered")
    val todayVsYesterdayRecovered: Double,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Double
)