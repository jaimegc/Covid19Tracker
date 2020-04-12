package com.jaimegc.covid19tracker.data.room.daos

import androidx.room.*
import com.jaimegc.covid19tracker.data.room.entities.CovidTrackerTotalEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CovidTrackerTotalDao {

    @Query("SELECT * FROM covid_tracker_total WHERE date =:date")
    abstract fun getByDate(date: String): Flow<List<CovidTrackerTotalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(covidTrackerTotal: CovidTrackerTotalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(covidTrackerTotals: List<CovidTrackerTotalEntity>)

    @Transaction
    open suspend fun update(covidTrackerTotal: CovidTrackerTotalEntity) {
        insert(covidTrackerTotal)
    }
}