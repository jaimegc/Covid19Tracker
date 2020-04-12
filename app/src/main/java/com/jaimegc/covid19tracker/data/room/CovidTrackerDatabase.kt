package com.jaimegc.covid19tracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerTotalDao
import com.jaimegc.covid19tracker.data.room.entities.CovidTrackerTotalEntity


@Database(entities = [CovidTrackerTotalEntity::class],
    version = Covid19TrackerDatabase.version
)
abstract class Covid19TrackerDatabase : RoomDatabase() {
    abstract fun covidTrackerTotalDao(): CovidTrackerTotalDao

    companion object {
        const val version = 1
        private const val DATABASE_NAME = "covid19-tracker-db"

        fun build(context: Context): Covid19TrackerDatabase =
            Room.databaseBuilder(context.applicationContext, Covid19TrackerDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}