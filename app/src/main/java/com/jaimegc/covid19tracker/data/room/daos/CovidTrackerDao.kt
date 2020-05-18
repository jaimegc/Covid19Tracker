package com.jaimegc.covid19tracker.data.room.daos

import androidx.room.*
import com.jaimegc.covid19tracker.data.room.entities.*
import com.jaimegc.covid19tracker.data.room.pojos.*
import com.jaimegc.covid19tracker.data.room.views.RegionAndStatsDV
import kotlinx.coroutines.flow.Flow


@Dao
abstract class CovidTrackerDao {

    @Transaction
    @Query("SELECT * FROM world_stats WHERE date = :date")
    abstract fun getWorldAndCountriesStatsByDate(date: String): Flow<WorldAndCountriesStatsPojo>

    @Query("SELECT * FROM world_stats")
    abstract suspend fun getWorld(): WorldStatsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorldStats(worldStats: WorldStatsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllWorldsStats(worldsStats: List<WorldStatsEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllCountries(countries: List<CountryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllCountriesStats(countriesStats: List<CountryStatsEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllRegions(regions: List<RegionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllRegionsStats(regionsStats: List<RegionStatsEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllSubRegions(subRegions: List<SubRegionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllSubRegionsStats(subRegionsStats: List<SubRegionStatsEntity>)

    @Transaction
    open suspend fun populateDatabase(
        worldsStats: List<WorldStatsEntity>,
        countries: List<CountryEntity>,
        countriesStats: List<CountryStatsEntity>,
        regions: List<RegionEntity>,
        regionsStats: List<RegionStatsEntity>,
        subRegions: List<SubRegionEntity>,
        subRegionsStats: List<SubRegionStatsEntity>
    ) {
        insertAllWorldsStats(worldsStats)
        insertAllCountries(countries)
        insertAllCountriesStats(countriesStats)
        insertAllRegions(regions)
        insertAllRegionsStats(regionsStats)
        insertAllSubRegions(subRegions)
        insertAllSubRegionsStats(subRegionsStats)
    }
}

@Dao
abstract class WorldStatsDao {

    @Query("SELECT * FROM world_stats WHERE date = :date")
    abstract fun getByDate(date: String): Flow<List<WorldStatsEntity>>

    @Query("SELECT * FROM world_stats ORDER BY date ASC")
    abstract fun getAll(): Flow<List<WorldStatsEntity>>
}

@Dao
abstract class CountryStatsDao {

    @Query("SELECT * FROM country_stats ORDER BY date ASC")
    abstract fun getAll(): Flow<List<CountryStatsEntity>>

    @Query("SELECT * FROM country_stats WHERE id_country_fk = :idCountry ORDER BY date ASC")
    abstract fun getById(idCountry: String): Flow<List<CountryStatsEntity>>

    @Transaction
    @Query("""
        SELECT * FROM country, country_stats 
        WHERE country.id = country_stats.id_country_fk
        GROUP BY country.name
        ORDER BY country_stats.confirmed DESC""")
    abstract fun getCountriesAndStatsOrderByConfirmed(): Flow<List<CountryAndStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM country c, country_stats s
        WHERE c.id = s.id_country_fk AND s.confirmed > 2000 AND c.id IN (
            SELECT id FROM country 
                INNER JOIN (
                    SELECT id_country_fk, MAX(confirmed) AS maxConfirmed FROM country_stats 
                    GROUP BY id_country_fk
                ) statsMaxConfirmed
                ON country.id = statsMaxConfirmed.id_country_fk 
                ORDER BY statsMaxConfirmed.maxConfirmed DESC LIMIT 5
            )
        ORDER BY c.id ASC, s.confirmed ASC
        """)
    abstract fun getCountriesAndStatsWithMostConfirmed(): Flow<List<CountryAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM country c
        INNER JOIN country_stats s ON c.id = s.id_country_fk AND c.id IN (
            SELECT id FROM country 
                INNER JOIN (
                    SELECT id_country_fk, MAX(deaths) AS maxDeaths FROM country_stats 
                    GROUP BY id_country_fk
                ) statsMaxDeaths
                ON country.id = statsMaxDeaths.id_country_fk 
                ORDER BY statsMaxDeaths.maxDeaths DESC LIMIT 5
            )
        WHERE deaths > 100
        ORDER BY c.id ASC, s.deaths ASC
        """)
    abstract fun getCountriesAndStatsWithMostDeaths(): Flow<List<CountryAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM country c, country_stats s
        WHERE c.id = s.id_country_fk AND s.recovered > 2000 AND c.id IN (
            SELECT id FROM country 
                INNER JOIN (
                    SELECT id_country_fk, MAX(recovered) AS maxRecovered FROM country_stats 
                    GROUP BY id_country_fk
                ) statsMaxRecovered
                ON country.id = statsMaxRecovered.id_country_fk 
                ORDER BY statsMaxRecovered.maxRecovered DESC LIMIT 5
            )
        ORDER BY c.id ASC, s.recovered ASC
        """)
    abstract fun getCountriesAndStatsWithMostRecovered(): Flow<List<CountryAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM country c
        INNER JOIN country_stats s ON c.id = s.id_country_fk AND c.id IN (
            SELECT id FROM country 
                INNER JOIN (
                    SELECT id_country_fk, MAX(open_cases) AS maxOpenCases FROM country_stats 
                    GROUP BY id_country_fk
                ) statsMaxOpenCases
                ON country.id = statsMaxOpenCases.id_country_fk 
                ORDER BY statsMaxOpenCases.maxOpenCases DESC LIMIT 5
            )
        WHERE open_cases > 2000
        ORDER BY c.id ASC, s.open_cases ASC
        """)
    abstract fun getCountriesAndStatsWithMostOpenCases(): Flow<List<CountryAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM country c
        LEFT JOIN country_stats cs ON c.id = cs.id_country_fk
        WHERE c.id = :idCountry AND cs.date = :date
        """)
    abstract fun getCountryAndStatsByIdDate(idCountry: String, date: String): Flow<CountryAndOneStatsPojo>
}

@Dao
abstract class CountryDao {

    @Query("SELECT * FROM country WHERE name = :name")
    abstract fun getByName(name: String): Flow<List<CountryEntity>>

    @Query("SELECT * FROM country ORDER BY name ASC")
    abstract fun getAll(): Flow<List<CountryEntity>>
}

@Dao
abstract class RegionDao {

    @Query("SELECT * FROM region WHERE name = :name ORDER BY name ASC")
    abstract fun getByName(name: String): Flow<List<RegionEntity>>

    @Query("SELECT * FROM region ORDER BY name ASC")
    abstract fun getAll(): Flow<List<RegionEntity>>

    @Query("SELECT * FROM region WHERE id_country_fk = :idCountry ORDER BY name ASC")
    abstract fun getByCountry(idCountry: String): Flow<List<RegionEntity>>
}

@Dao
abstract class RegionStatsDao {
    @Transaction
    @Query("""
        SELECT * FROM region r
        LEFT JOIN region_stats s ON r.id = s.id_region_fk 
        WHERE r.id_country_fk = :idCountry AND s.date = :date
        ORDER BY s.confirmed DESC
        """)
    abstract fun getRegionAndStatsByCountryAndDateOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<List<RegionAndStatsDV>>

    @Transaction
    @Query("""
        SELECT * FROM region r
        LEFT JOIN region_stats s ON r.id = s.id_region_fk
        WHERE r.id_country_fk = :idCountry
        GROUP BY r.name
        ORDER BY s.confirmed DESC
        """)
    abstract fun getRegionAndAllStatsByCountryAndDateOrderByConfirmed(
        idCountry: String
    ): Flow<List<RegionAndStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM region r, region_stats s
        WHERE r.id = s.id_region_fk AND r.id_country_fk = :idCountry AND s.id_region_country_fk = :idCountry AND r.id IN (
            SELECT id FROM region 
                INNER JOIN (
                    SELECT id_region_fk, MAX(confirmed) AS maxConfirmed FROM region_stats 
                    GROUP BY id_region_fk
                ) statsMaxConfirmed
                ON region.id = statsMaxConfirmed.id_region_fk AND region.id_country_fk = :idCountry
                ORDER BY statsMaxConfirmed.maxConfirmed DESC LIMIT 5
            )
        ORDER BY r.id ASC, s.confirmed ASC
        """)
    abstract fun getRegionsAndStatsWithMostConfirmed(idCountry: String): Flow<List<RegionAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM region r, region_stats s
        WHERE r.id = s.id_region_fk AND r.id_country_fk = :idCountry AND s.id_region_country_fk = :idCountry AND r.id IN (
            SELECT id FROM region 
                INNER JOIN (
                    SELECT id_region_fk, MAX(deaths) AS maxDeaths FROM region_stats 
                    GROUP BY id_region_fk
                ) statsMaxDeaths
                ON region.id = statsMaxDeaths.id_region_fk AND region.id_country_fk = :idCountry
                ORDER BY statsMaxDeaths.maxDeaths DESC LIMIT 5
            )
        ORDER BY r.id ASC, s.deaths ASC
        """)
    abstract fun getRegionsAndStatsWithMostDeaths(idCountry: String): Flow<List<RegionAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM region r, region_stats s
        WHERE r.id = s.id_region_fk AND r.id_country_fk = :idCountry AND s.id_region_country_fk = :idCountry AND r.id IN (
            SELECT id FROM region 
                INNER JOIN (
                    SELECT id_region_fk, MAX(recovered) AS maxRecovered FROM region_stats 
                    GROUP BY id_region_fk
                ) statsMaxRecovered
                ON region.id = statsMaxRecovered.id_region_fk AND region.id_country_fk = :idCountry
                ORDER BY statsMaxRecovered.maxRecovered DESC LIMIT 5
            )
        ORDER BY r.id ASC, s.recovered ASC
        """)
    abstract fun getRegionsAndStatsWithMostRecovered(idCountry: String): Flow<List<RegionAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM region r, region_stats s
        WHERE r.id = s.id_region_fk AND r.id_country_fk = :idCountry AND s.id_region_country_fk = :idCountry AND r.id IN (
            SELECT id FROM region 
                INNER JOIN (
                    SELECT id_region_fk, MAX(open_cases) AS maxOpenCases FROM region_stats 
                    GROUP BY id_region_fk
                ) statsMaxOpenCases
                ON region.id = statsMaxOpenCases.id_region_fk AND region.id_country_fk = :idCountry
                ORDER BY statsMaxOpenCases.maxOpenCases DESC LIMIT 5
            )
        ORDER BY r.id ASC, s.open_cases ASC
        """)
    abstract fun getRegionsAndStatsWithMostOpenCases(idCountry: String): Flow<List<RegionAndOneStatsPojo>>
}
