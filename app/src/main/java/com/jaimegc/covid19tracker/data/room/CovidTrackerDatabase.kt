package com.jaimegc.covid19tracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaimegc.covid19tracker.data.room.daos.CountryDao
import com.jaimegc.covid19tracker.data.room.daos.CountryStatsDao
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.data.room.daos.RegionDao
import com.jaimegc.covid19tracker.data.room.daos.RegionStatsDao
import com.jaimegc.covid19tracker.data.room.daos.SubRegionStatsDao
import com.jaimegc.covid19tracker.data.room.daos.WorldStatsDao
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
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