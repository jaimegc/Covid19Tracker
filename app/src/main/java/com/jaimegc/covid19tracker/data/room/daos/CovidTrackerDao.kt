package com.jaimegc.covid19tracker.data.room.daos

import androidx.room.*
import com.jaimegc.covid19tracker.data.room.entities.CountryTodayStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.CovidTrackerAndWorldTodayStatsPojo
import com.jaimegc.covid19tracker.data.room.entities.CovidTrackerEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldTodayStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CovidTrackerDao {

    @Query("SELECT * FROM covid_tracker WHERE date =:date")
    abstract fun getByDate(date: String): Flow<CovidTrackerEntity>

    @Query("SELECT * FROM covid_tracker WHERE date =:date")
    abstract fun getByDateNew(date: String): Flow<CovidTrackerAndWorldTodayStatsPojo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(covidTracker: CovidTrackerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(covidTrackers: List<CovidTrackerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllCountryStats(worldStats: List<CountryTodayStatsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorldStats(worldStats: WorldTodayStatsEntity)

    @Transaction
    open suspend fun save(
        covidTracker: CovidTrackerEntity,
        countriesStats: List<CountryTodayStatsEntity>,
        worldStats: WorldTodayStatsEntity
    ) {
        insert(covidTracker)
        insertAllCountryStats(countriesStats)
        insertWorldStats(worldStats)
    }
}

@Dao
abstract class CountryTodayStatsDao {

    @Query("SELECT * FROM country_today_stats WHERE date =:date")
    abstract fun getByDate(date: String): Flow<List<CountryTodayStatsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(worldStats: CountryTodayStatsEntity)

    @Transaction
    open suspend fun update(worldStats: CountryTodayStatsEntity) {
        insert(worldStats)
    }
}

@Dao
abstract class WorldTodayStatsDao {

    @Query("SELECT * FROM world_today_stats WHERE date =:date")
    abstract fun getByDate(date: String): Flow<List<WorldTodayStatsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(worldStats: WorldTodayStatsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(worldStats: List<WorldTodayStatsEntity>)

    @Transaction
    open suspend fun update(worldStats: WorldTodayStatsEntity) {
        insert(worldStats)
    }
}