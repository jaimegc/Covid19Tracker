package com.jaimegc.covid19tracker.data.room.daos

import androidx.room.*
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.StatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.WorldAndCountriesStatsPojo
import kotlinx.coroutines.flow.Flow


@Dao
abstract class CovidTrackerDao {

    @Transaction
    @Query("SELECT * FROM world_stats WHERE date =:date")
    abstract fun getWorldAndCountriesStatsByDate(date: String): Flow<WorldAndCountriesStatsPojo>

    @Query("SELECT * FROM world_stats")
    abstract suspend fun getWorld(): WorldStatsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorldStats(worldStats: WorldStatsEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllWorldsStats(worldsStats: List<WorldStatsEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllCountries(countries: List<CountryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllCountriesStats(countriesStats: List<StatsEntity>)

    @Transaction
    open suspend fun save(
        worldStats: WorldStatsEntity,
        countries: List<CountryEntity>,
        countriesStats: List<StatsEntity>
    ) {
        insertWorldStats(worldStats)
        insertAllCountries(countries)
        insertAllCountriesStats(countriesStats)
    }

    @Transaction
    open suspend fun populateDatabase(
        worldsStats: List<WorldStatsEntity>,
        countries: List<CountryEntity>,
        countriesStats: List<StatsEntity>
    ) {
        insertAllWorldsStats(worldsStats)
        insertAllCountries(countries)
        insertAllCountriesStats(countriesStats)
    }
}

@Dao
abstract class WorldStatsDao {

    @Query("SELECT * FROM world_stats WHERE date =:date")
    abstract fun getByDate(date: String): Flow<List<WorldStatsEntity>>

    @Query("SELECT * FROM world_stats ORDER BY date ASC")
    abstract fun getAll(): Flow<List<WorldStatsEntity>>
}

@Dao
abstract class CountryStatsDao {

    @Query("SELECT * FROM country WHERE name =:name")
    abstract fun getByName(name: String): Flow<List<CountryEntity>>

    @Query("""
        SELECT * FROM country, stats 
        WHERE country.id = stats.id_country_fk
        GROUP BY country.name
        ORDER BY stats.confirmed DESC""")
    abstract fun getCountriesAndStatsOrderByConfirmed(): Flow<List<CountryAndStatsPojo>>

    @Query("""
        SELECT * FROM country c
        INNER JOIN stats s ON c.id = s.id_country_fk AND c.id IN
        (SELECT id FROM country 
            INNER JOIN (
                SELECT id_country_fk, MAX(confirmed) AS maxConfirmed FROM stats 
                GROUP BY id_country_fk) statsMaxConfirmed
                ON country.id = statsMaxConfirmed.id_country_fk 
                ORDER BY statsMaxConfirmed.maxConfirmed DESC LIMIT 5
            )
        WHERE confirmed > 10000
        ORDER BY c.id ASC, s.confirmed ASC
        """)
    abstract fun getCountriesAndStatsWithMostConfirmed(): Flow<List<CountryAndOneStatsPojo>>
}