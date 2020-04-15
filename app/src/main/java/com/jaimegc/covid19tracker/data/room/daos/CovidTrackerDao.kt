package com.jaimegc.covid19tracker.data.room.daos

import androidx.room.*
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldAndCountriesPojo
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CovidTrackerDao {

    @Transaction
    @Query("SELECT * FROM world_stats WHERE date =:date")
    abstract fun getWorldAndCountriesByDate(date: String): Flow<WorldAndCountriesPojo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorld(worldStats: WorldStatsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllCountryStats(worldStats: List<CountryStatsEntity>)

    @Transaction
    open suspend fun save(
        worldStats: WorldStatsEntity,
        countriesStats: List<CountryStatsEntity>
    ) {
        insertWorld(worldStats)
        insertAllCountryStats(countriesStats)
    }
}

@Dao
abstract class WorldStatsDao {

    @Query("SELECT * FROM world_stats WHERE date =:date")
    abstract fun getByDate(date: String): Flow<List<WorldStatsEntity>>
}

@Dao
abstract class CountryStatsDao {

    @Query("SELECT * FROM country_stats WHERE date =:date")
    abstract fun getByDate(date: String): Flow<List<CountryStatsEntity>>
}