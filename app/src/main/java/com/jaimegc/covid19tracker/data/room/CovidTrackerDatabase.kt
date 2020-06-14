package com.jaimegc.covid19tracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaimegc.covid19tracker.data.room.daos.*
import com.jaimegc.covid19tracker.data.room.entities.*
import com.jaimegc.covid19tracker.data.room.views.CountryAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.RegionAndStatsDV
import java.io.File

@Database(entities = [CountryEntity::class, WorldStatsEntity::class, CountryStatsEntity::class,
    RegionEntity::class, RegionStatsEntity::class, SubRegionEntity::class, SubRegionStatsEntity::class],
    views = [CountryAndStatsDV::class, RegionAndStatsDV::class],
    version = Covid19TrackerDatabase.version
)
abstract class Covid19TrackerDatabase : RoomDatabase() {
    abstract fun covidTrackerDao(): CovidTrackerDao
    abstract fun countryStatsDao(): CountryStatsDao
    abstract fun worldStatsDao(): WorldStatsDao
    abstract fun countryDao(): CountryDao
    abstract fun regionDao(): RegionDao
    abstract fun regionStatsDao(): RegionStatsDao
    abstract fun subRegionStatsDao(): SubRegionStatsDao

    companion object {
        const val version = 1
        const val DATABASE_NAME = "covid19-tracker-db"

        fun build(context: Context): Covid19TrackerDatabase =
            Room.databaseBuilder(context, Covid19TrackerDatabase::class.java, DATABASE_NAME)
                .createFromFile(File("${context.filesDir}${File.separator}$DATABASE_NAME"))
                .fallbackToDestructiveMigration()
                .build()
    }
}