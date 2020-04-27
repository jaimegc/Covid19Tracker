package com.jaimegc.covid19tracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jaimegc.covid19tracker.data.room.daos.CountryStatsDao
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.data.room.daos.WorldStatsDao
import com.jaimegc.covid19tracker.data.room.views.CountryAndStatsDV
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.StatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import com.jaimegc.covid19tracker.worker.PopulateDatabaseWorker


@Database(entities = [CountryEntity::class, WorldStatsEntity::class, StatsEntity::class],
    views = [CountryAndStatsDV::class],
    version = Covid19TrackerDatabase.version
)
abstract class Covid19TrackerDatabase : RoomDatabase() {
    abstract fun covidTrackerDao(): CovidTrackerDao
    abstract fun countryStatsDao(): CountryStatsDao
    abstract fun worldStatsDao(): WorldStatsDao

    companion object {
        const val version = 1
        private const val DATABASE_NAME = "covid19-tracker-db"

        fun build(context: Context): Covid19TrackerDatabase =
            Room.databaseBuilder(context.applicationContext, Covid19TrackerDatabase::class.java, DATABASE_NAME)
                .createFromAsset("database/covid19-tracker-db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        //val request = OneTimeWorkRequestBuilder<PopulateDatabaseWorker>().build()
                        //WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
    }
}