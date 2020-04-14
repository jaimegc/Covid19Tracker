package com.jaimegc.covid19tracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaimegc.covid19tracker.data.room.daos.CountryTodayStatsDao
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.data.room.daos.WorldTodayStatsDao
import com.jaimegc.covid19tracker.data.room.entities.CountryTodayStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.CovidTrackerEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldTodayStatsEntity


@Database(entities = [CovidTrackerEntity::class, CountryTodayStatsEntity::class, WorldTodayStatsEntity::class],
    version = Covid19TrackerDatabase.version
)
abstract class Covid19TrackerDatabase : RoomDatabase() {
    abstract fun covidTrackerTotalDao(): CovidTrackerDao
    abstract fun countryTodayStatsDao(): CountryTodayStatsDao
    abstract fun worldTodayStatsDao(): WorldTodayStatsDao

    companion object {
        const val version = 1
        private const val DATABASE_NAME = "covid19-tracker-db"

        fun build(context: Context): Covid19TrackerDatabase =
            Room.databaseBuilder(context.applicationContext, Covid19TrackerDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}