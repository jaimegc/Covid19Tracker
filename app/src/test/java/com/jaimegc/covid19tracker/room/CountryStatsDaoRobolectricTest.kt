package com.jaimegc.covid19tracker.room

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.country
import com.jaimegc.covid19tracker.ModelFactoryTest.countryAndOneStatsPojo as spainPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.countryStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryStatsEntity
import com.jaimegc.covid19tracker.data.room.daos.CountryStatsDao
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndOneStatsPojo
import com.jaimegc.covid19tracker.util.DatabaseRobolectricTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CountryStatsDaoRobolectricTest : DatabaseRobolectricTest() {

    private lateinit var countryStatsDao: CountryStatsDao

    private lateinit var germanyPojo: CountryAndOneStatsPojo
    private lateinit var francePojo: CountryAndOneStatsPojo
    private lateinit var italyPojo: CountryAndOneStatsPojo
    private lateinit var australiaPojo: CountryAndOneStatsPojo
    private lateinit var egyptPojo: CountryAndOneStatsPojo

    @Before
    fun setup() {
        countryStatsDao = database.countryStatsDao()
    }

    @Test
    fun getAll_shouldReturnCountries() = runBlocking {
        countryStatsDao.getAll().take(1).collect {
            assertThat(it).isEqualTo(listCountryStatsEntity)
        }
    }

    @Test
    fun getById_shouldReturnStatsByCountry() = runBlocking {
        countryStatsDao.getById(
            country.id
        ).take(1).collect {
            assertThat(it).isEqualTo(listCountryStatsEntity)
        }
    }

    @Test
    fun getCountryAndStatsByLastDateByCountry_shouldReturnCountryAndOneStatsPojo() = runBlocking {
        countryStatsDao.getCountryAndStatsByLastDate(
            country.id
        ).take(1).collect {
            assertThat(it.country).isEqualTo(spainPojo.country)
            assertThat(it.countryStats).isEqualTo(spainPojo.countryStats)
            assertThat(it.isValid()).isEqualTo(spainPojo.isValid())
        }
    }

    @Test
    fun getCountryAndStatsByDate_shouldReturnCountryAndOneStatsPojo() = runBlocking {
        countryStatsDao.getCountryAndStatsByDate(
            country.id,
            spainPojo.countryStats!!.dateTimestamp
        ).take(1).collect {
            assertThat(it.country).isEqualTo(spainPojo.country)
            assertThat(it.countryStats).isEqualTo(spainPojo.countryStats)
            assertThat(it.isValid()).isEqualTo(spainPojo.isValid())
        }
    }

    @Test
    fun getCountriesAndStatsWithMostConfirmed_shouldReturnFiveCountriesMostConfirmedOrderedById() = runBlocking {
        generateCountriesAndStats()

        countryStatsDao.getCountriesAndStatsWithMostConfirmed().take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].country).isEqualTo(australiaPojo.country)
            assertThat(it[0].countryStats).isEqualTo(australiaPojo.countryStats)
            assertThat(it[0].isValid()).isEqualTo(australiaPojo.isValid())
            assertThat(it[1].country).isEqualTo(francePojo.country)
            assertThat(it[1].countryStats).isEqualTo(francePojo.countryStats)
            assertThat(it[1].isValid()).isEqualTo(francePojo.isValid())
            assertThat(it[2].country).isEqualTo(germanyPojo.country)
            assertThat(it[2].countryStats).isEqualTo(germanyPojo.countryStats)
            assertThat(it[2].isValid()).isEqualTo(germanyPojo.isValid())
            assertThat(it[3].country).isEqualTo(italyPojo.country)
            assertThat(it[3].countryStats).isEqualTo(italyPojo.countryStats)
            assertThat(it[3].isValid()).isEqualTo(italyPojo.isValid())
            assertThat(it[4].country).isEqualTo(spainPojo.country)
            assertThat(it[4].countryStats).isEqualTo(spainPojo.countryStats)
            assertThat(it[4].isValid()).isEqualTo(spainPojo.isValid())
        }
    }

    @Test
    fun getCountriesAndStatsWithMostDeaths_shouldReturnFiveCountriesMostDeathsOrderedById() = runBlocking {
        generateCountriesAndStats()

        countryStatsDao.getCountriesAndStatsWithMostDeaths().take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].country).isEqualTo(australiaPojo.country)
            assertThat(it[0].countryStats).isEqualTo(australiaPojo.countryStats)
            assertThat(it[0].isValid()).isEqualTo(australiaPojo.isValid())
            assertThat(it[1].country).isEqualTo(egyptPojo.country)
            assertThat(it[1].countryStats).isEqualTo(egyptPojo.countryStats)
            assertThat(it[1].isValid()).isEqualTo(egyptPojo.isValid())
            assertThat(it[2].country).isEqualTo(francePojo.country)
            assertThat(it[2].countryStats).isEqualTo(francePojo.countryStats)
            assertThat(it[2].isValid()).isEqualTo(francePojo.isValid())
            assertThat(it[3].country).isEqualTo(germanyPojo.country)
            assertThat(it[3].countryStats).isEqualTo(germanyPojo.countryStats)
            assertThat(it[3].isValid()).isEqualTo(germanyPojo.isValid())
            assertThat(it[4].country).isEqualTo(italyPojo.country)
            assertThat(it[4].countryStats).isEqualTo(italyPojo.countryStats)
            assertThat(it[4].isValid()).isEqualTo(italyPojo.isValid())
        }
    }

    @Test
    fun getCountriesAndStatsWithMostOpenCases_shouldReturnFiveCountriesMostOpenCasesOrderedById() = runBlocking {
        generateCountriesAndStats()

        countryStatsDao.getCountriesAndStatsWithMostOpenCases().take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].country).isEqualTo(australiaPojo.country)
            assertThat(it[0].countryStats).isEqualTo(australiaPojo.countryStats)
            assertThat(it[0].isValid()).isEqualTo(australiaPojo.isValid())
            assertThat(it[1].country).isEqualTo(francePojo.country)
            assertThat(it[1].countryStats).isEqualTo(francePojo.countryStats)
            assertThat(it[1].isValid()).isEqualTo(francePojo.isValid())
            assertThat(it[2].country).isEqualTo(germanyPojo.country)
            assertThat(it[2].countryStats).isEqualTo(germanyPojo.countryStats)
            assertThat(it[2].isValid()).isEqualTo(germanyPojo.isValid())
            assertThat(it[3].country).isEqualTo(italyPojo.country)
            assertThat(it[3].countryStats).isEqualTo(italyPojo.countryStats)
            assertThat(it[3].isValid()).isEqualTo(italyPojo.isValid())
            assertThat(it[4].country).isEqualTo(spainPojo.country)
            assertThat(it[4].countryStats).isEqualTo(spainPojo.countryStats)
            assertThat(it[4].isValid()).isEqualTo(spainPojo.isValid())
        }
    }

    @Test
    fun getCountriesAndStatsWithMostRecovered_shouldReturnFiveCountriesMostRecoveredOrderedById() = runBlocking {
        generateCountriesAndStats()

        countryStatsDao.getCountriesAndStatsWithMostRecovered().take(1).collect {
            assertThat(it).hasSize(5)
            assertThat(it[0].country).isEqualTo(australiaPojo.country)
            assertThat(it[0].countryStats).isEqualTo(australiaPojo.countryStats)
            assertThat(it[0].isValid()).isEqualTo(australiaPojo.isValid())
            assertThat(it[1].country).isEqualTo(egyptPojo.country)
            assertThat(it[1].countryStats).isEqualTo(egyptPojo.countryStats)
            assertThat(it[1].isValid()).isEqualTo(egyptPojo.isValid())
            assertThat(it[2].country).isEqualTo(francePojo.country)
            assertThat(it[2].countryStats).isEqualTo(francePojo.countryStats)
            assertThat(it[2].isValid()).isEqualTo(francePojo.isValid())
            assertThat(it[3].country).isEqualTo(italyPojo.country)
            assertThat(it[3].countryStats).isEqualTo(italyPojo.countryStats)
            assertThat(it[3].isValid()).isEqualTo(italyPojo.isValid())
            assertThat(it[4].country).isEqualTo(spainPojo.country)
            assertThat(it[4].countryStats).isEqualTo(spainPojo.countryStats)
            assertThat(it[4].isValid()).isEqualTo(spainPojo.isValid())
        }
    }

    private fun generateCountriesAndStats() = runBlocking {
        val germany =
            countryEntity.copy(id = "germany", name = "Germany", nameEs = "Alemania", code = "DE")
        val france =
            countryEntity.copy(id = "france", name = "France", nameEs = "Francia", code = "FR")
        val italy =
            countryEntity.copy(id = "italy", name = "Italy", nameEs = "Italia", code = "IT")
        val australia =
            countryEntity.copy(id = "australia", name = "Australia", nameEs = "Australia", code = "AU")
        val egypt =
            countryEntity.copy(id = "egypt", name = "Egypt", nameEs = "Egipto", code = "EG")

        val germanyStats = countryStatsEntity.copy(
            idCountryFk = germany.id,
            stats = countryStatsEntity.stats.copy(
                confirmed = 8000L,
                deaths = 4000L,
                openCases = 8000L,
                recovered = 4000L
            )
        )
        val franceStats = countryStatsEntity.copy(
            idCountryFk = france.id,
            stats = countryStatsEntity.stats.copy(
                confirmed = 7000L,
                deaths = 5000L,
                openCases = 7000L,
                recovered = 5000L
            )
        )
        val italyStats = countryStatsEntity.copy(
            idCountryFk = italy.id,
            stats = countryStatsEntity.stats.copy(
                confirmed = 6000L,
                deaths = 6000L,
                openCases = 6000L,
                recovered = 6000L
            )
        )
        val australiaStats = countryStatsEntity.copy(
            idCountryFk = australia.id,
            stats = countryStatsEntity.stats.copy(
                confirmed = 5000L,
                deaths = 7000L,
                openCases = 5000L,
                recovered = 7000L
            )
        )
        val egyptStats = countryStatsEntity.copy(
            idCountryFk = egypt.id,
            stats = countryStatsEntity.stats.copy(
                confirmed = 4000L,
                deaths = 8000L,
                openCases = 4000L,
                recovered = 8000L
            )
        )

        germanyPojo = CountryAndOneStatsPojo(
            country = germany,
            countryStats = germanyStats
        )

        francePojo = CountryAndOneStatsPojo(
            country = france,
            countryStats = franceStats
        )

        italyPojo = CountryAndOneStatsPojo(
            country = italy,
            countryStats = italyStats
        )

        australiaPojo = CountryAndOneStatsPojo(
            country = australia,
            countryStats = australiaStats
        )

        egyptPojo = CountryAndOneStatsPojo(
            country = egypt,
            countryStats = egyptStats
        )

        covidTrackerDao.insertAllCountries(listOf(germany, france, italy, australia, egypt))
        covidTrackerDao.insertAllCountriesStats(
            listOf(germanyStats, franceStats, italyStats, australiaStats, egyptStats)
        )
    }
}