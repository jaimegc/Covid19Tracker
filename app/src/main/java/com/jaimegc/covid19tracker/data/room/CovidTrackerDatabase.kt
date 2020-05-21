package com.jaimegc.covid19tracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jaimegc.covid19tracker.data.room.daos.*
import com.jaimegc.covid19tracker.data.room.entities.*
import com.jaimegc.covid19tracker.data.room.views.CountryAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.RegionAndStatsDV
import com.jaimegc.covid19tracker.worker.PopulateDatabaseWorker
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
                //.createFromAsset("covid19-tracker-db")
                //.createFromFile(File("${context.filesDir}${File.separator}$DATABASE_NAME"))
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Populate database using a Worker
                        val request = OneTimeWorkRequestBuilder<PopulateDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
    }
}