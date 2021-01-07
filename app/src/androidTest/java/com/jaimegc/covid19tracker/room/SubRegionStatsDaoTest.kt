package com.jaimegc.covid19tracker.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.country
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStatsDV
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.region
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionAndOneStatsPojo as sevillaPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionStatsEntity
import com.jaimegc.covid19tracker.data.room.daos.SubRegionStatsDao
import com.jaimegc.covid19tracker.data.room.entities.RegionEntity
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.utils.DatabaseTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubRegionStatsDaoTest : DatabaseTest() {

    private lateinit var subRegionStatsDao: SubRegionStatsDao

    private lateinit var almeriaPojo: SubRegionAndOneStatsPojo
    private lateinit var cordobaPojo: SubRegionAndOneStatsPojo
    private lateinit var granadaPojo: SubRegionAndOneStatsPojo
    private lateinit var huelvaPojo: SubRegionAndOneStatsPojo
    private lateinit var malagaPojo: SubRegionAndOneStatsPojo

    private lateinit var regionWithNoSubRegions: RegionEntity

    @Before
    fun setup() {
        subRegionStatsDao = database.subRegionStatsDao()
    }

    @Test
    fun getSubRegionAndStatsByCountryAndLastDate_shouldReturnSubRegionAndStatsDV() = runBlocking {
        subRegionStatsDao.getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(
            country.id,
            region.id
        ).take(1).collect {
            assertThat(it).isEqualTo(listSubRegionAndStatsDV)
        }
    }

    @Test
    fun getSubRegionAndStatsByCountryAndDate_shouldReturnSubRegionAndStatsDV() = runBlocking {
        subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(
            country.id,
            region.id,
            regionStats.stats.dateTimestamp
        ).take(1).collect {
            assertThat(it).isEqualTo(listSubRegionAndStatsDV)
        }
    }

    @Test
    fun getSubRegionAndAllStatsByCountryAndDate_shouldReturnSubRegionsAndStatsPojo() = runBlocking {
        subRegionStatsDao.getSubRegionAndAllStatsByCountryAndDateOrderByConfirmed(
            country.id,
            region.id,
        ).take(1).collect {
            assertThat(it).isEqualTo(listSubRegionAndStatsPojo)
        }
    }

    @Test
    fun getRegionAndAllStatsByCountryAndDate_withNoSubRegions_shouldReturnEmpty() = runBlocking {
        generateRegionWithNoSubRegions()

        subRegionStatsDao.getSubRegionAndAllStatsByCountryAndDateOrderByConfirmed(
            country.id,
            regionWithNoSubRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    @Test
    fun getSubRegionAndStatsWithMostConfirmed_shouldReturnFiveSubRegionsMostConfirmedOrderedById() = runBlocking {
        generateSubRegionsAndStats()

        subRegionStatsDao.getSubRegionsAndStatsWithMostConfirmed(
            country.id,
            region.id
        ).take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].subRegion).isEqualTo(almeriaPojo.subRegion)
            assertThat(it[0].subRegionStats).isEqualTo(almeriaPojo.subRegionStats)
            assertThat(it[0].isValid()).isEqualTo(almeriaPojo.isValid())
            assertThat(it[1].subRegion).isEqualTo(cordobaPojo.subRegion)
            assertThat(it[1].subRegionStats).isEqualTo(cordobaPojo.subRegionStats)
            assertThat(it[1].isValid()).isEqualTo(cordobaPojo.isValid())
            assertThat(it[2].subRegion).isEqualTo(granadaPojo.subRegion)
            assertThat(it[2].subRegionStats).isEqualTo(granadaPojo.subRegionStats)
            assertThat(it[2].isValid()).isEqualTo(granadaPojo.isValid())
            assertThat(it[3].subRegion).isEqualTo(huelvaPojo.subRegion)
            assertThat(it[3].subRegionStats).isEqualTo(huelvaPojo.subRegionStats)
            assertThat(it[3].isValid()).isEqualTo(huelvaPojo.isValid())
            assertThat(it[4].subRegion).isEqualTo(sevillaPojo.subRegion)
            assertThat(it[4].subRegionStats).isEqualTo(sevillaPojo.subRegionStats)
            assertThat(it[4].isValid()).isEqualTo(sevillaPojo.isValid())
        }
    }

    @Test
    fun getSubRegionAndStatsWithMostConfirmed_withNoSubRegions_shouldReturnEmpty() = runBlocking {
        generateRegionWithNoSubRegions()

        subRegionStatsDao.getSubRegionsAndStatsWithMostConfirmed(
            country.id,
            regionWithNoSubRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    @Test
    fun getSubRegionAndStatsWithMostDeaths_shouldReturnFiveSubRegionsMostDeathsOrderedById() = runBlocking {
        generateSubRegionsAndStats()

        subRegionStatsDao.getSubRegionsAndStatsWithMostDeaths(
            country.id,
            region.id
        ).take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].subRegion).isEqualTo(almeriaPojo.subRegion)
            assertThat(it[0].subRegionStats).isEqualTo(almeriaPojo.subRegionStats)
            assertThat(it[0].isValid()).isEqualTo(almeriaPojo.isValid())
            assertThat(it[1].subRegion).isEqualTo(cordobaPojo.subRegion)
            assertThat(it[1].subRegionStats).isEqualTo(cordobaPojo.subRegionStats)
            assertThat(it[1].isValid()).isEqualTo(cordobaPojo.isValid())
            assertThat(it[2].subRegion).isEqualTo(granadaPojo.subRegion)
            assertThat(it[2].subRegionStats).isEqualTo(granadaPojo.subRegionStats)
            assertThat(it[2].isValid()).isEqualTo(granadaPojo.isValid())
            assertThat(it[3].subRegion).isEqualTo(huelvaPojo.subRegion)
            assertThat(it[3].subRegionStats).isEqualTo(huelvaPojo.subRegionStats)
            assertThat(it[3].isValid()).isEqualTo(huelvaPojo.isValid())
            assertThat(it[4].subRegion).isEqualTo(malagaPojo.subRegion)
            assertThat(it[4].subRegionStats).isEqualTo(malagaPojo.subRegionStats)
            assertThat(it[4].isValid()).isEqualTo(malagaPojo.isValid())
        }
    }

    @Test
    fun getSubRegionAndStatsWithMostDeaths_withNoSubRegions_shouldReturnEmpty() = runBlocking {
        generateRegionWithNoSubRegions()

        subRegionStatsDao.getSubRegionsAndStatsWithMostDeaths(
            country.id,
            regionWithNoSubRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    @Test
    fun getSubRegionAndStatsWithMostOpenCases_shouldReturnFiveSubRegionsMostOpenCasesOrderedById() = runBlocking {
        generateSubRegionsAndStats()

        subRegionStatsDao.getSubRegionsAndStatsWithMostOpenCases(
            country.id,
            region.id
        ).take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].subRegion).isEqualTo(almeriaPojo.subRegion)
            assertThat(it[0].subRegionStats).isEqualTo(almeriaPojo.subRegionStats)
            assertThat(it[0].isValid()).isEqualTo(almeriaPojo.isValid())
            assertThat(it[1].subRegion).isEqualTo(cordobaPojo.subRegion)
            assertThat(it[1].subRegionStats).isEqualTo(cordobaPojo.subRegionStats)
            assertThat(it[1].isValid()).isEqualTo(cordobaPojo.isValid())
            assertThat(it[2].subRegion).isEqualTo(granadaPojo.subRegion)
            assertThat(it[2].subRegionStats).isEqualTo(granadaPojo.subRegionStats)
            assertThat(it[2].isValid()).isEqualTo(granadaPojo.isValid())
            assertThat(it[3].subRegion).isEqualTo(huelvaPojo.subRegion)
            assertThat(it[3].subRegionStats).isEqualTo(huelvaPojo.subRegionStats)
            assertThat(it[3].isValid()).isEqualTo(huelvaPojo.isValid())
            assertThat(it[4].subRegion).isEqualTo(sevillaPojo.subRegion)
            assertThat(it[4].subRegionStats).isEqualTo(sevillaPojo.subRegionStats)
            assertThat(it[4].isValid()).isEqualTo(sevillaPojo.isValid())
        }
    }

    @Test
    fun getSubRegionAndStatsWithMostOpenCases_withNoSubRegions_shouldReturnEmpty() = runBlocking {
        generateRegionWithNoSubRegions()

        subRegionStatsDao.getSubRegionsAndStatsWithMostOpenCases(
            country.id,
            regionWithNoSubRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    @Test
    fun getSubRegionAndStatsWithMostRecovered_shouldReturnFiveSubRegionsMostRecoveredOrderedById() = runBlocking {
        generateSubRegionsAndStats()

        subRegionStatsDao.getSubRegionsAndStatsWithMostRecovered(
            country.id,
            region.id
        ).take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].subRegion).isEqualTo(cordobaPojo.subRegion)
            assertThat(it[0].subRegionStats).isEqualTo(cordobaPojo.subRegionStats)
            assertThat(it[0].isValid()).isEqualTo(cordobaPojo.isValid())
            assertThat(it[1].subRegion).isEqualTo(granadaPojo.subRegion)
            assertThat(it[1].subRegionStats).isEqualTo(granadaPojo.subRegionStats)
            assertThat(it[1].isValid()).isEqualTo(granadaPojo.isValid())
            assertThat(it[2].subRegion).isEqualTo(huelvaPojo.subRegion)
            assertThat(it[2].subRegionStats).isEqualTo(huelvaPojo.subRegionStats)
            assertThat(it[2].isValid()).isEqualTo(huelvaPojo.isValid())
            assertThat(it[3].subRegion).isEqualTo(malagaPojo.subRegion)
            assertThat(it[3].subRegionStats).isEqualTo(malagaPojo.subRegionStats)
            assertThat(it[3].isValid()).isEqualTo(malagaPojo.isValid())
            assertThat(it[4].subRegion).isEqualTo(sevillaPojo.subRegion)
            assertThat(it[4].subRegionStats).isEqualTo(sevillaPojo.subRegionStats)
            assertThat(it[4].isValid()).isEqualTo(sevillaPojo.isValid())
        }
    }

    @Test
    fun getSubRegionAndStatsWithMostRecovered_withNoSubRegions_shouldReturnEmpty() = runBlocking {
        generateRegionWithNoSubRegions()

        subRegionStatsDao.getSubRegionsAndStatsWithMostRecovered(
            country.id,
            regionWithNoSubRegions.id
        ).take(1).collect {
            assertThat(it).isEmpty()
        }
    }

    private fun generateSubRegionsAndStats() = runBlocking {
        val almeria =
            subRegionEntity.copy(
                id = "almeria",
                name = "Almeria",
                nameEs = "Almeria",
                idRegionFk = region.id,
                idCountryFk = country.id
            )
        val cordoba =
            subRegionEntity.copy(
                id = "cordoba",
                name = "Cordoba",
                nameEs = "Cordoba",
                idRegionFk = region.id,
                idCountryFk = country.id
            )
        val granada =
            subRegionEntity.copy(
                id = "granada",
                name = "Granada",
                nameEs = "Granada",
                idRegionFk = region.id,
                idCountryFk = country.id
            )
        val huelva =
            subRegionEntity.copy(
                id = "huelva",
                name = "Huelva",
                nameEs = "Huelva",
                idRegionFk = region.id,
                idCountryFk = country.id
            )
        val malaga =
            subRegionEntity.copy(
                id = "malaga",
                name = "Malaga",
                nameEs = "Malaga",
                idRegionFk = region.id,
                idCountryFk = country.id
            )

        val almeriaStats = subRegionStatsEntity.copy(
            idRegionFk = region.id,
            idSubRegionFk = almeria.id,
            stats = subRegionStatsEntity.stats.copy(
                confirmed = 8000L,
                deaths = 4000L,
                openCases = 8000L,
                recovered = 4000L
            )
        )
        val cordobaStats = subRegionStatsEntity.copy(
            idRegionFk = region.id,
            idSubRegionFk = cordoba.id,
            stats = subRegionStatsEntity.stats.copy(
                confirmed = 7000L,
                deaths = 5000L,
                openCases = 7000L,
                recovered = 5000L
            )
        )
        val granadaStats = subRegionStatsEntity.copy(
            idRegionFk = region.id,
            idSubRegionFk = granada.id,
            stats = subRegionStatsEntity.stats.copy(
                confirmed = 6000L,
                deaths = 6000L,
                openCases = 6000L,
                recovered = 6000L
            )
        )
        val huelvaStats = subRegionStatsEntity.copy(
            idRegionFk = region.id,
            idSubRegionFk = huelva.id,
            stats = subRegionStatsEntity.stats.copy(
                confirmed = 5000L,
                deaths = 7000L,
                openCases = 5000L,
                recovered = 7000L
            )
        )
        val malagaStats = subRegionStatsEntity.copy(
            idRegionFk = region.id,
            idSubRegionFk = malaga.id,
            stats = subRegionStatsEntity.stats.copy(
                confirmed = 4000L,
                deaths = 8000L,
                openCases = 4000L,
                recovered = 8000L
            )
        )

        almeriaPojo = SubRegionAndOneStatsPojo(
            subRegion = almeria,
            subRegionStats = almeriaStats
        )

        cordobaPojo = SubRegionAndOneStatsPojo(
            subRegion = cordoba,
            subRegionStats = cordobaStats
        )

        granadaPojo = SubRegionAndOneStatsPojo(
            subRegion = granada,
            subRegionStats = granadaStats
        )

        huelvaPojo = SubRegionAndOneStatsPojo(
            subRegion = huelva,
            subRegionStats = huelvaStats
        )

        malagaPojo = SubRegionAndOneStatsPojo(
            subRegion = malaga,
            subRegionStats = malagaStats
        )

        covidTrackerDao.insertAllSubRegions(listOf(almeria, cordoba, granada, huelva, malaga))
        covidTrackerDao.insertAllSubRegionsStats(
            listOf(almeriaStats, cordobaStats, granadaStats, huelvaStats, malagaStats)
        )
    }

    private fun generateRegionWithNoSubRegions() = runBlocking {
        regionWithNoSubRegions =
            regionEntity.copy(id = "empty", name = "Empty", nameEs = "Empty")

        val regionWithNoSubRegionsStats = regionStatsEntity.copy(
            idCountryFk = country.id,
            idRegionFk = regionWithNoSubRegions.id,
            stats = regionStatsEntity.stats
        )

        covidTrackerDao.insertAllRegions(listOf(regionWithNoSubRegions))
        covidTrackerDao.insertAllRegionsStats(listOf(regionWithNoSubRegionsStats))
    }
}