package com.jaimegc.covid19tracker.data.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionEntity
import com.jaimegc.covid19tracker.data.room.entities.SubRegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.WorldAndCountriesStatsPojo
import com.jaimegc.covid19tracker.data.room.views.RegionAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.SubRegionAndStatsDV
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CovidTrackerDao {

    @Transaction
    @Query("SELECT * FROM world_stats WHERE date_timestamp = :dateTimestamp")
    abstract fun getWorldAndCountriesStatsByDate(dateTimestamp: Long): Flow<WorldAndCountriesStatsPojo>

    @Transaction
    @Query("""
        SELECT * FROM world_stats 
        WHERE date_timestamp = (
            SELECT MAX(date_timestamp) FROM world_stats
        )
        """
    )
    abstract fun getWorldAndCountriesStatsByLastDate(): Flow<WorldAndCountriesStatsPojo>

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

    @Query("SELECT * FROM world_stats ORDER BY date_timestamp ASC")
    abstract fun getAll(): Flow<List<WorldStatsEntity>>

    @Query("SELECT date FROM world_stats ORDER BY date ASC")
    abstract suspend fun getAllDates(): List<String>
}

@Dao
abstract class CountryStatsDao {

    @Query("SELECT * FROM country_stats ORDER BY date_timestamp ASC")
    abstract fun getAll(): Flow<List<CountryStatsEntity>>

    @Query("SELECT * FROM country_stats WHERE id_country_fk = :idCountry ORDER BY date_timestamp ASC")
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
                LEFT JOIN (
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
        LEFT JOIN country_stats s ON c.id = s.id_country_fk AND c.id IN (
            SELECT id FROM country 
                LEFT JOIN (
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
                LEFT JOIN (
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
        LEFT JOIN country_stats s ON c.id = s.id_country_fk AND c.id IN (
            SELECT id FROM country 
                LEFT JOIN (
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
        WHERE c.id = :idCountry AND cs.date_timestamp = :dateTimestamp
        """)
    abstract fun getCountryAndStatsByDate(idCountry: String, dateTimestamp: Long): Flow<CountryAndOneStatsPojo>

    @Transaction
    @Query("""
        SELECT * FROM country c
        LEFT JOIN country_stats cs ON c.id = cs.id_country_fk
        WHERE c.id = :idCountry AND cs.date_timestamp = (
            SELECT MAX(date_timestamp) FROM country_stats
        )
        """)
    abstract fun getCountryAndStatsByLastDate(idCountry: String): Flow<CountryAndOneStatsPojo>
}

@Dao
abstract class CountryDao {

    @Query("SELECT * FROM country ORDER BY name ASC")
    abstract fun getAll(): Flow<List<CountryEntity>>
}

@Dao
abstract class RegionDao {

    @Query("SELECT * FROM region ORDER BY name ASC")
    abstract fun getAll(): Flow<List<RegionEntity>>

    @Query("SELECT * FROM region WHERE id_country_fk = :idCountry ORDER BY name ASC")
    abstract fun getByCountry(idCountry: String): Flow<List<RegionEntity>>
}

@Dao
abstract class RegionStatsDao {
    @Query("""
        SELECT * FROM region_stats 
        WHERE id_region_fk = :idRegion AND id_region_country_fk = :idCountry
        ORDER BY date_timestamp ASC""")
    abstract fun getById(idCountry: String, idRegion: String): Flow<List<RegionStatsEntity>>

    @Transaction
    @Query("""
        SELECT * FROM region r
        LEFT JOIN region_stats s ON r.id = s.id_region_fk
        WHERE r.id = :idRegion AND r.id_country_fk = :idCountry AND s.date_timestamp = :dateTimestamp
        """)
    abstract fun getRegionAndStatsByDate(
        idCountry: String,
        idRegion: String,
        dateTimestamp: Long
    ): Flow<RegionAndOneStatsPojo>

    @Transaction
    @Query("""
        SELECT * FROM region r
        LEFT JOIN region_stats s ON r.id = s.id_region_fk
        WHERE r.id = :idRegion AND r.id_country_fk = :idCountry AND s.date_timestamp = (
            SELECT MAX(date_timestamp) FROM region_stats
        )
        """)
    abstract fun getRegionAndStatsByLastDate(idCountry: String, idRegion: String): Flow<RegionAndOneStatsPojo>

    @Transaction
    @Query("""
        SELECT * FROM region r
        LEFT JOIN region_stats s ON r.id = s.id_region_fk 
        WHERE r.id_country_fk = :idCountry AND s.date_timestamp = :dateTimestamp
        ORDER BY s.confirmed DESC
        """)
    abstract fun getRegionAndStatsByCountryAndDateOrderByConfirmed(
        idCountry: String,
        dateTimestamp: Long
    ): Flow<List<RegionAndStatsDV>>

    @Transaction
    @Query("""
        SELECT * FROM region r
        LEFT JOIN region_stats s ON r.id = s.id_region_fk 
        WHERE r.id_country_fk = :idCountry AND s.date_timestamp = (
            SELECT MAX(date_timestamp) FROM region_stats
        )
        ORDER BY s.confirmed DESC
        """)
    abstract fun getRegionAndStatsByCountryAndLastDateOrderByConfirmed(
        idCountry: String
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
                LEFT JOIN (
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
                LEFT JOIN (
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
                LEFT JOIN (
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
                LEFT JOIN (
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

@Dao
abstract class SubRegionStatsDao {
    @Transaction
    @Query("""
        SELECT * FROM sub_region r
        LEFT JOIN sub_region_stats s ON r.id = s.id_sub_region_fk AND r.id_region_fk = s.id_sub_region_region_fk
        WHERE r.id_region_fk = :idRegion AND r.id_country_fk = :idCountry AND s.date_timestamp = :dateTimestamp
        ORDER BY s.confirmed DESC
        """)
    abstract fun getSubRegionAndStatsByCountryAndDateOrderByConfirmed(
        idCountry: String,
        idRegion: String,
        dateTimestamp: Long
    ): Flow<List<SubRegionAndStatsDV>>

    @Transaction
    @Query("""
        SELECT * FROM sub_region r
        LEFT JOIN sub_region_stats s ON r.id = s.id_sub_region_fk AND r.id_region_fk = s.id_sub_region_region_fk
        WHERE r.id_region_fk = :idRegion AND r.id_country_fk = :idCountry AND s.date_timestamp = (
            SELECT MAX(date_timestamp) FROM sub_region_stats
        )
        ORDER BY s.confirmed DESC
        """)
    abstract fun getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<List<SubRegionAndStatsDV>>

    @Transaction
    @Query("""
        SELECT * FROM sub_region r
        LEFT JOIN sub_region_stats s ON r.id = s.id_sub_region_fk
        WHERE r.id_region_fk = :idRegion AND r.id_country_fk = :idCountry
        GROUP BY r.name
        ORDER BY s.confirmed DESC
        """)
    abstract fun getSubRegionAndAllStatsByCountryAndDateOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<List<SubRegionAndStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM sub_region r, sub_region_stats s
        WHERE r.id = s.id_sub_region_fk AND r.id_region_fk = s.id_sub_region_region_fk 
            AND r.id_country_fk = :idCountry AND s.id_sub_region_region_fk = :idRegion AND r.id IN (
                SELECT id FROM sub_region 
                    LEFT JOIN (
                        SELECT id_sub_region_fk, MAX(confirmed) AS maxConfirmed FROM sub_region_stats 
                        GROUP BY id_sub_region_fk
                    ) statsMaxConfirmed
                    ON sub_region.id = statsMaxConfirmed.id_sub_region_fk AND sub_region.id_country_fk = :idCountry 
                        AND sub_region.id_region_fk = :idRegion
                    ORDER BY statsMaxConfirmed.maxConfirmed DESC LIMIT 5
                )
        ORDER BY r.id ASC, s.confirmed ASC
        """)
    abstract fun getSubRegionsAndStatsWithMostConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<List<SubRegionAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM sub_region r, sub_region_stats s
        WHERE r.id = s.id_sub_region_fk AND r.id_region_fk = s.id_sub_region_region_fk 
            AND r.id_country_fk = :idCountry AND s.id_sub_region_region_fk = :idRegion AND r.id IN (
                SELECT id FROM sub_region 
                    LEFT JOIN (
                        SELECT id_sub_region_fk, MAX(deaths) AS maxDeaths FROM sub_region_stats 
                        GROUP BY id_sub_region_fk
                    ) statsMaxDeaths
                    ON sub_region.id = statsMaxDeaths.id_sub_region_fk AND sub_region.id_country_fk = :idCountry 
                        AND sub_region.id_region_fk = :idRegion
                    ORDER BY statsMaxDeaths.maxDeaths DESC LIMIT 5
                )
        ORDER BY r.id ASC, s.deaths ASC
        """)
    abstract fun getSubRegionsAndStatsWithMostDeaths(
        idCountry: String,
        idRegion: String
    ): Flow<List<SubRegionAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM sub_region r, sub_region_stats s
        WHERE r.id = s.id_sub_region_fk AND r.id_region_fk = s.id_sub_region_region_fk 
            AND r.id_country_fk = :idCountry AND s.id_sub_region_region_fk = :idRegion AND r.id IN (
                SELECT id FROM sub_region 
                    LEFT JOIN (
                        SELECT id_sub_region_fk, MAX(recovered) AS maxRecovered FROM sub_region_stats 
                        GROUP BY id_sub_region_fk
                    ) statsMaxRecovered
                    ON sub_region.id = statsMaxRecovered.id_sub_region_fk AND sub_region.id_country_fk = :idCountry 
                        AND sub_region.id_region_fk = :idRegion
                    ORDER BY statsMaxRecovered.maxRecovered DESC LIMIT 5
                )
        ORDER BY r.id ASC, s.recovered ASC
        """)
    abstract fun getSubRegionsAndStatsWithMostRecovered(
        idCountry: String,
        idRegion: String
    ): Flow<List<SubRegionAndOneStatsPojo>>

    @Transaction
    @Query("""
        SELECT * FROM sub_region r, sub_region_stats s
        WHERE r.id = s.id_sub_region_fk AND r.id_region_fk = s.id_sub_region_region_fk 
            AND r.id_country_fk = :idCountry AND s.id_sub_region_region_fk = :idRegion AND r.id IN (
                SELECT id FROM sub_region 
                    LEFT JOIN (
                        SELECT id_sub_region_fk, MAX(open_cases) AS maxOpenCases FROM sub_region_stats 
                        GROUP BY id_sub_region_fk
                    ) statsMaxOpenCases
                    ON sub_region.id = statsMaxOpenCases.id_sub_region_fk AND sub_region.id_country_fk = :idCountry 
                        AND sub_region.id_region_fk = :idRegion
                    ORDER BY statsMaxOpenCases.maxOpenCases DESC LIMIT 5
                )
        ORDER BY r.id ASC, s.open_cases ASC
        """)
    abstract fun getSubRegionsAndStatsWithMostOpenCases(
        idCountry: String,
        idRegion: String
    ): Flow<List<SubRegionAndOneStatsPojo>>
}
