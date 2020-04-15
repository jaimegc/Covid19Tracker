package com.jaimegc.covid19tracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaimegc.covid19tracker.data.room.daos.CountryStatsDao
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.data.room.daos.WorldStatsDao
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity


@Database(entities = [CountryStatsEntity::class, WorldStatsEntity::class],
    version = Covid19TrackerDatabase.version
)
abstract class Covid19TrackerDatabase : RoomDatabase() {
    abstract fun covidTrackerTotalDao(): CovidTrackerDao
    abstract fun countryTodayStatsDao(): CountryStatsDao
    abstract fun worldTodayStatsDao(): WorldStatsDao

    companion object {
        const val version = 1
        private const val DATABASE_NAME = "covid19-tracker-db"

        fun build(context: Context): Covid19TrackerDatabase =
            Room.databaseBuilder(context.applicationContext, Covid19TrackerDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}