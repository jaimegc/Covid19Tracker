package com.jaimegc.covid19tracker.room

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.country
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.countryStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStatsDV
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.region
import com.jaimegc.covid19tracker.ModelFactoryTest.regionAndOneStatsPojo as andaluciaPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStatsEntity
import com.jaimegc.covid19tracker.data.room.daos.RegionStatsDao
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndOneStatsPojo
import com.jaimegc.covid19tracker.utils.DatabaseRobolectricTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegionStatsDaoRobolectricTest : DatabaseRobolectricTest() {

    private lateinit var regionStatsDao: RegionStatsDao

    private lateinit var asturiasPojo: RegionAndOneStatsPojo
    private lateinit var catalunaPojo: RegionAndOneStatsPojo
    private lateinit var extremaduraPojo: RegionAndOneStatsPojo
    private lateinit var galiciaPojo: RegionAndOneStatsPojo
    private lateinit var madridPojo: RegionAndOneStatsPojo

    private lateinit var countryWithNoRegions: CountryEntity

    @Before
    fun setup() {
        initDb()
        regionStatsDao = database.regionStatsDao()
    }

    @After
    fun close() = closeDb()

    @Test
    fun getById_shouldReturnStatsByCountryAndRegion() = runBlocking {
        regionStatsDao.getById(
            country.id,
            region.id
        ).take(1).collect {
            assertThat(it).isEqualTo(listRegionStatsEntity)
        }
    }

    @Test
    fun getRegionAndStatsByDate_shouldReturnRegionAndOneStatsPojo() = runBlocking {
        regionStatsDao.getRegionAndStatsByDate(
            country.id,
            region.id,
            regionStats.stats.dateTimestamp
        ).take(1).collect {
            assertThat(it.region).isEqualTo(andaluciaPojo.region)
            assertThat(it.regionStats).isEqualTo(andaluciaPojo.regionStats)
            assertThat(it.isValid()).isEqualTo(andaluciaPojo.isValid())
        }
    }

    @Test
    fun getRegionAndStatsByLastDate_shouldReturnRegionAndOneStatsPojo() = runBlocking {
        regionStatsDao.getRegionAndStatsByLastDate(
            country.id,
            region.id,
        ).take(1).collect {
            assertThat(it.region).isEqualTo(andaluciaPojo.region)
            assertThat(it.regionStats).isEqualTo(andaluciaPojo.regionStats)
            assertThat(it.isValid()).isEqualTo(andaluciaPojo.isValid())
        }
    }

    @Test
    fun getRegionAndStatsByCountryAndLastDate_shouldReturnRegionAndStatsDV() = runBlocking {
        regionStatsDao.getRegionAndStatsByCountryAndLastDateOrderByConfirmed(
            country.id
        ).take(1).collect {
            assertThat(it).isEqualTo(listRegionAndStatsDV)
        }
    }

    @Test
    fun getRegionAndStatsByCountryAndDate_shouldReturnRegionAndStatsDV() = runBlocking {
        regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(
            country.id,
            countryStatsEntity.dateTimestamp
        ).take(1).collect {
            assertThat(it).isEqualTo(listRegionAndStatsDV)
        }
    }

    @Test
    fun getRegionAndAllStatsByCountryAndDate_shouldReturnRegionsAndStatsPojo() = runBlocking {
        regionStatsDao.getRegionAndAllStatsByCountryAndDateOrderByConfirmed(
            country.id
        ).take(1).collect {
            assertThat(it).isEqualTo(listRegionAndStatsPojo)
        }
    }

    @Test
    fun getRegionAndAllStatsByCountryAndDate_withNoRegions_shouldReturnEmpty() = runBlocking {
        generateCountryWithNoRegions()

        regionStatsDao.getRegionAndAllStatsByCountryAndDateOrderByConfirmed(
            countryWithNoRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    @Test
    fun getRegionAndStatsWithMostConfirmed_shouldReturnFiveRegionsMostConfirmedOrderedById() = runBlocking {
        generateRegionsAndStats()

        regionStatsDao.getRegionsAndStatsWithMostConfirmed(
            country.id
        ).take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].region).isEqualTo(andaluciaPojo.region)
            assertThat(it[0].regionStats).isEqualTo(andaluciaPojo.regionStats)
            assertThat(it[0].isValid()).isEqualTo(andaluciaPojo.isValid())
            assertThat(it[1].region).isEqualTo(asturiasPojo.region)
            assertThat(it[1].regionStats).isEqualTo(asturiasPojo.regionStats)
            assertThat(it[1].isValid()).isEqualTo(asturiasPojo.isValid())
            assertThat(it[2].region).isEqualTo(catalunaPojo.region)
            assertThat(it[2].regionStats).isEqualTo(catalunaPojo.regionStats)
            assertThat(it[2].isValid()).isEqualTo(catalunaPojo.isValid())
            assertThat(it[3].region).isEqualTo(extremaduraPojo.region)
            assertThat(it[3].regionStats).isEqualTo(extremaduraPojo.regionStats)
            assertThat(it[3].isValid()).isEqualTo(extremaduraPojo.isValid())
            assertThat(it[4].region).isEqualTo(galiciaPojo.region)
            assertThat(it[4].regionStats).isEqualTo(galiciaPojo.regionStats)
            assertThat(it[4].isValid()).isEqualTo(galiciaPojo.isValid())
        }
    }

    @Test
    fun getRegionAndStatsWithMostConfirmed_withNoRegions_shouldReturnEmpty() = runBlocking {
        generateCountryWithNoRegions()

        regionStatsDao.getRegionsAndStatsWithMostConfirmed(
            countryWithNoRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    @Test
    fun getRegionAndStatsWithMostDeaths_shouldReturnFiveRegionsMostDeathsOrderedById() = runBlocking {
        generateRegionsAndStats()

        regionStatsDao.getRegionsAndStatsWithMostDeaths(
            country.id
        ).take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].region).isEqualTo(asturiasPojo.region)
            assertThat(it[0].regionStats).isEqualTo(asturiasPojo.regionStats)
            assertThat(it[0].isValid()).isEqualTo(asturiasPojo.isValid())
            assertThat(it[1].region).isEqualTo(catalunaPojo.region)
            assertThat(it[1].regionStats).isEqualTo(catalunaPojo.regionStats)
            assertThat(it[1].isValid()).isEqualTo(catalunaPojo.isValid())
            assertThat(it[2].region).isEqualTo(extremaduraPojo.region)
            assertThat(it[2].regionStats).isEqualTo(extremaduraPojo.regionStats)
            assertThat(it[2].isValid()).isEqualTo(extremaduraPojo.isValid())
            assertThat(it[3].region).isEqualTo(galiciaPojo.region)
            assertThat(it[3].regionStats).isEqualTo(galiciaPojo.regionStats)
            assertThat(it[3].isValid()).isEqualTo(galiciaPojo.isValid())
            assertThat(it[4].region).isEqualTo(madridPojo.region)
            assertThat(it[4].regionStats).isEqualTo(madridPojo.regionStats)
            assertThat(it[4].isValid()).isEqualTo(madridPojo.isValid())
        }
    }

    @Test
    fun getRegionAndStatsWithMostDeaths_withNoRegions_shouldReturnEmpty() = runBlocking {
        generateCountryWithNoRegions()

        regionStatsDao.getRegionsAndStatsWithMostDeaths(
            countryWithNoRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    @Test
    fun getRegionAndStatsWithMostOpenCases_shouldReturnFiveRegionsMostOpenCasesOrderedById() = runBlocking {
        generateRegionsAndStats()

        regionStatsDao.getRegionsAndStatsWithMostOpenCases(
            country.id
        ).take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].region).isEqualTo(andaluciaPojo.region)
            assertThat(it[0].regionStats).isEqualTo(andaluciaPojo.regionStats)
            assertThat(it[0].isValid()).isEqualTo(andaluciaPojo.isValid())
            assertThat(it[1].region).isEqualTo(asturiasPojo.region)
            assertThat(it[1].regionStats).isEqualTo(asturiasPojo.regionStats)
            assertThat(it[2].isValid()).isEqualTo(asturiasPojo.isValid())
            assertThat(it[2].region).isEqualTo(catalunaPojo.region)
            assertThat(it[2].regionStats).isEqualTo(catalunaPojo.regionStats)
            assertThat(it[2].isValid()).isEqualTo(catalunaPojo.isValid())
            assertThat(it[3].region).isEqualTo(extremaduraPojo.region)
            assertThat(it[3].regionStats).isEqualTo(extremaduraPojo.regionStats)
            assertThat(it[3].isValid()).isEqualTo(extremaduraPojo.isValid())
            assertThat(it[4].region).isEqualTo(galiciaPojo.region)
            assertThat(it[4].regionStats).isEqualTo(galiciaPojo.regionStats)
            assertThat(it[4].isValid()).isEqualTo(galiciaPojo.isValid())
        }
    }

    @Test
    fun getRegionAndStatsWithMostOpenCases_withNoRegions_shouldReturnEmpty() = runBlocking {
        generateCountryWithNoRegions()

        regionStatsDao.getRegionsAndStatsWithMostOpenCases(
            countryWithNoRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    @Test
    fun getRegionAndStatsWithMostRecovered_shouldReturnFiveRegionsMostRecoveredOrderedById() = runBlocking {
        generateRegionsAndStats()

        regionStatsDao.getRegionsAndStatsWithMostRecovered(
            country.id
        ).take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].region).isEqualTo(andaluciaPojo.region)
            assertThat(it[0].regionStats).isEqualTo(andaluciaPojo.regionStats)
            assertThat(it[0].isValid()).isEqualTo(andaluciaPojo.isValid())
            assertThat(it[1].region).isEqualTo(catalunaPojo.region)
            assertThat(it[1].regionStats).isEqualTo(catalunaPojo.regionStats)
            assertThat(it[1].isValid()).isEqualTo(catalunaPojo.isValid())
            assertThat(it[2].region).isEqualTo(extremaduraPojo.region)
            assertThat(it[2].regionStats).isEqualTo(extremaduraPojo.regionStats)
            assertThat(it[2].isValid()).isEqualTo(extremaduraPojo.isValid())
            assertThat(it[3].region).isEqualTo(galiciaPojo.region)
            assertThat(it[3].regionStats).isEqualTo(galiciaPojo.regionStats)
            assertThat(it[3].isValid()).isEqualTo(galiciaPojo.isValid())
            assertThat(it[4].region).isEqualTo(madridPojo.region)
            assertThat(it[4].regionStats).isEqualTo(madridPojo.regionStats)
            assertThat(it[4].isValid()).isEqualTo(madridPojo.isValid())
        }
    }

    @Test
    fun getRegionAndStatsWithMostRecovered_withNoRegions_shouldReturnEmpty() = runBlocking {
        generateCountryWithNoRegions()

        regionStatsDao.getRegionsAndStatsWithMostRecovered(
            countryWithNoRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    private fun generateRegionsAndStats() = runBlocking {
        val asturias =
            regionEntity.copy(id = "asturias", name = "Asturias", nameEs = "Asturias", idCountryFk = country.id)
        val cataluna =
            regionEntity.copy(id = "cataluna", name = "Cataluña", nameEs = "Cataluña", idCountryFk = country.id)
        val extremadura =
            regionEntity.copy(
                id = "extremadura",
                name = "Extremadura",
                nameEs = "Extremadura",
                idCountryFk = country.id
            )
        val galicia =
            regionEntity.copy(id = "galicia", name = "Galicia", nameEs = "Galicia", idCountryFk = country.id)
        val madrid =
            regionEntity.copy(id = "madrid", name = "Madrid", nameEs = "Madrid", idCountryFk = country.id)

        val asturiasStats = regionStatsEntity.copy(
            idCountryFk = country.id,
            idRegionFk = asturias.id,
            stats = regionStatsEntity.stats.copy(
                confirmed = 8000L,
                deaths = 4000L,
                openCases = 8000L,
                recovered = 4000L
            )
        )
        val catalunaStats = regionStatsEntity.copy(
            idCountryFk = country.id,
            idRegionFk = cataluna.id,
            stats = regionStatsEntity.stats.copy(
                confirmed = 7000L,
                deaths = 5000L,
                openCases = 7000L,
                recovered = 5000L
            )
        )
        val extremaduraStats = regionStatsEntity.copy(
            idCountryFk = country.id,
            idRegionFk = extremadura.id,
            stats = regionStatsEntity.stats.copy(
                confirmed = 6000L,
                deaths = 6000L,
                openCases = 6000L,
                recovered = 6000L
            )
        )
        val galiciaStats = regionStatsEntity.copy(
            idCountryFk = country.id,
            idRegionFk = galicia.id,
            stats = regionStatsEntity.stats.copy(
                confirmed = 5000L,
                deaths = 7000L,
                openCases = 5000L,
                recovered = 7000L
            )
        )
        val madridStats = regionStatsEntity.copy(
            idCountryFk = country.id,
            idRegionFk = madrid.id,
            stats = regionStatsEntity.stats.copy(
                confirmed = 4000L,
                deaths = 8000L,
                openCases = 4000L,
                recovered = 8000L
            )
        )

        asturiasPojo = RegionAndOneStatsPojo(
            region = asturias,
            regionStats = asturiasStats
        )

        catalunaPojo = RegionAndOneStatsPojo(
            region = cataluna,
            regionStats = catalunaStats
        )

        extremaduraPojo = RegionAndOneStatsPojo(
            region = extremadura,
            regionStats = extremaduraStats
        )

        galiciaPojo = RegionAndOneStatsPojo(
            region = galicia,
            regionStats = galiciaStats
        )

        madridPojo = RegionAndOneStatsPojo(
            region = madrid,
            regionStats = madridStats
        )

        covidTrackerDao.insertAllRegions(listOf(asturias, cataluna, extremadura, galicia, madrid))
        covidTrackerDao.insertAllRegionsStats(
            listOf(asturiasStats, catalunaStats, extremaduraStats, galiciaStats, madridStats)
        )
    }

    private fun generateCountryWithNoRegions() = runBlocking {
        countryWithNoRegions =
            countryEntity.copy(id = "empty", name = "Empty", nameEs = "Empty", code = "EM")

        val countryWithNoRegionsStats = countryStatsEntity.copy(
            idCountryFk = countryWithNoRegions.id,
            stats = countryStatsEntity.stats
        )

        covidTrackerDao.insertAllCountries(listOf(countryWithNoRegions))
        covidTrackerDao.insertAllCountriesStats(listOf(countryWithNoRegionsStats))
    }
}