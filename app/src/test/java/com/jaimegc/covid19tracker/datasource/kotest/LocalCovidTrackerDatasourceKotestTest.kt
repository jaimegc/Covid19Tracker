package com.jaimegc.covid19tracker.datasource.kotest

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.DATE
import com.jaimegc.covid19tracker.ModelFactoryTest.countryAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.countryOneStats
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTracker
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountry
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegion
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStatsDV
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionEmpty
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStatsDV
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listWorldStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.regionOneStats
import com.jaimegc.covid19tracker.ModelFactoryTest.worldAndCountriesStatsPojo
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.room.daos.CountryDao
import com.jaimegc.covid19tracker.data.room.daos.CountryStatsDao
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.data.room.daos.RegionDao
import com.jaimegc.covid19tracker.data.room.daos.RegionStatsDao
import com.jaimegc.covid19tracker.data.room.daos.SubRegionStatsDao
import com.jaimegc.covid19tracker.data.room.daos.WorldStatsDao
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.CountryStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionEntity
import com.jaimegc.covid19tracker.data.room.entities.RegionStatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.views.RegionAndStatsDV
import com.jaimegc.covid19tracker.data.room.views.SubRegionAndStatsDV
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.toStatsDomain
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class LocalCovidTrackerDatasourceKotestTest : ShouldSpec({

    val DATES = listOf("date1", "date2", "date3")
    val EMPTY_DATE = ""
    val ID_COUNTRY = "id_country"
    val ID_REGION = "id_region"

    val covidTrackerDao: CovidTrackerDao = mockk(relaxed = true)
    val worldStatsDao: WorldStatsDao = mockk()
    val countryStatsDao: CountryStatsDao = mockk()
    val countryDao: CountryDao = mockk()
    val regionDao: RegionDao = mockk()
    val regionStatsDao: RegionStatsDao = mockk()
    val subRegionStatsDao: SubRegionStatsDao = mockk()

    lateinit var local: LocalCovidTrackerDatasource
    
    beforeTest {
        MockKAnnotations.init(this)
        local =
            LocalCovidTrackerDatasource(
                covidTrackerDao,
                worldStatsDao,
                countryStatsDao,
                countryDao,
                regionDao,
                regionStatsDao,
                subRegionStatsDao
            )
    }

    should("get covid tracker by empty date should return last data if exists") {
        val flow = flow {
            emit(worldAndCountriesStatsPojo)
        }

        every { covidTrackerDao.getWorldAndCountriesStatsByLastDate() } returns flow

        val flowLocalDs = local.getCovidTrackerByDate(EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(worldAndCountriesStatsPojo.toDomain())
        }
        verify { covidTrackerDao.getWorldAndCountriesStatsByLastDate() }
        verify { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) wasNot Called }
    }

    should("get covid tracker by empty date should database empty error if date doesn't exist") {
        val flow = flow {
            emit(worldAndCountriesStatsPojo.copy(countriesStats = listOf()))
        }

        every { covidTrackerDao.getWorldAndCountriesStatsByLastDate() } returns flow

        val flowLocalDs = local.getCovidTrackerByDate(EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
        verify { covidTrackerDao.getWorldAndCountriesStatsByLastDate() }
        verify { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) wasNot Called }
    }

    should("get covid tracker by date should return data if exists") {
        val flow = flow {
            emit(worldAndCountriesStatsPojo)
        }

        every { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) } returns flow

        val flowLocalDs = local.getCovidTrackerByDate(DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(worldAndCountriesStatsPojo.toDomain())
        }
        verify { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) }
        verify { covidTrackerDao.getWorldAndCountriesStatsByLastDate() wasNot Called }
    }

    should("get covid tracker by date should return database empty error if date doesn't exist") {
        val flow = flow {
            emit(worldAndCountriesStatsPojo.copy(countriesStats = listOf()))
        }

        every { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) } returns flow

        val flowLocalDs = local.getCovidTrackerByDate(DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
        verify { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) }
        verify { covidTrackerDao.getWorldAndCountriesStatsByLastDate() wasNot Called }
    }

    should("get world and countries by empty date should return last data if exists") {
        val flow = flow {
            emit(worldAndCountriesStatsPojo)
        }

        every { covidTrackerDao.getWorldAndCountriesStatsByLastDate() } returns flow

        val flowLocalDs = local.getWorldAndCountriesByDate(EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(worldAndCountriesStatsPojo.toDomain())
        }
        verify { covidTrackerDao.getWorldAndCountriesStatsByLastDate() }
        verify { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) wasNot Called }
    }

    should("get world and countries by empty date should return database empty error if date doesn't exist") {
        val flow = flow {
            emit(worldAndCountriesStatsPojo.copy(countriesStats = listOf()))
        }

        every { covidTrackerDao.getWorldAndCountriesStatsByLastDate() } returns flow

        val flowLocalDs = local.getWorldAndCountriesByDate(EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
        verify { covidTrackerDao.getWorldAndCountriesStatsByLastDate() }
        verify { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) wasNot Called }
    }

    should("get world and countries by date should return data if exists") {
        val flow = flow {
            emit(worldAndCountriesStatsPojo)
        }

        every { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) } returns flow

        val flowLocalDs = local.getWorldAndCountriesByDate(DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(worldAndCountriesStatsPojo.toDomain())
        }
        verify { local.getCovidTrackerByDate(DATE) }
        verify { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) }
        verify { covidTrackerDao.getWorldAndCountriesStatsByLastDate() wasNot Called }
    }

    should("get world and countries by date should return database empty error if date doesn't exist") {
        val flow = flow {
            emit(worldAndCountriesStatsPojo.copy(countriesStats = listOf()))
        }

        every { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) } returns flow

        val flowLocalDs = local.getWorldAndCountriesByDate(DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
        verify { local.getCovidTrackerByDate(DATE) }
        verify { covidTrackerDao.getWorldAndCountriesStatsByDate(any()) }
        verify { covidTrackerDao.getWorldAndCountriesStatsByLastDate() wasNot Called }
    }

    should("get world all stats should return data if exists") {
        val flow = flow {
            emit(listWorldStatsEntity)
        }

        every { worldStatsDao.getAll() } returns flow

        val flowLocalDs = local.getWorldAllStats()

        flowLocalDs.collect { data ->
            data.shouldBeRight(listWorldStatsEntity.toDomain())
        }
    }

    should("get world all stats with empty data should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<WorldStatsEntity>())
        }

        every { worldStatsDao.getAll() } returns flow

        val flowLocalDs = local.getWorldAllStats()

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get country all stats should return data if exists") {
        val flow = flow {
            emit(listCountryStatsEntity)
        }

        every { countryStatsDao.getById(any()) } returns flow

        val flowLocalDs = local.getCountryAllStats(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listCountryStatsEntity.toStatsDomain())
        }
    }

    should("get country all stats with empty data should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<CountryStatsEntity>())
        }

        every { countryStatsDao.getById(any()) } returns flow

        val flowLocalDs = local.getCountryAllStats(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get region all stats should return data if exists") {
        val flow = flow {
            emit(listRegionStatsEntity)
        }

        every { regionStatsDao.getById(any(), any()) } returns flow

        val flowLocalDs = local.getRegionAllStats(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listRegionStatsEntity.toStatsDomain())
        }
    }

    should("get region all stats with empty data should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionStatsEntity>())
        }

        every { regionStatsDao.getById(any(), any()) } returns flow

        val flowLocalDs = local.getRegionAllStats(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get countries and stats ordered by confirmed should return data if exists") {
        val flow = flow {
            emit(listCountryAndStatsPojo)
        }

        every { countryStatsDao.getCountriesAndStatsOrderByConfirmed() } returns flow

        val flowLocalDs = local.getCountriesStatsOrderByConfirmed()

        flowLocalDs.collect { data ->
            data.shouldBeRight(listCountryAndStatsPojo.toDomain())
        }
    }

    should("get countries and stats ordered by confirmed with empty data should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<CountryAndStatsPojo>())
        }

        every { countryStatsDao.getCountriesAndStatsOrderByConfirmed() } returns flow

        val flowLocalDs = local.getCountriesStatsOrderByConfirmed()

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get regions and stats ordered by confirmed with empty date should return data if exists") {
        val flow = flow {
            emit(listRegionAndStatsDV)
        }

        every {
            regionStatsDao.getRegionAndStatsByCountryAndLastDateOrderByConfirmed(any())
        } returns flow

        val flowLocalDs = local.getRegionsStatsOrderByConfirmed(ID_COUNTRY, EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listRegionAndStatsDV.toDomain())
        }
        verify { regionStatsDao.getRegionAndStatsByCountryAndLastDateOrderByConfirmed(any()) }
        verify {
            regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any()) wasNot Called
        }
    }

    should("get regions and stats ordered by confirmed with empty data should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionAndStatsDV>())
        }

        every {
            regionStatsDao.getRegionAndStatsByCountryAndLastDateOrderByConfirmed(any())
        } returns flow

        val flowLocalDs = local.getRegionsStatsOrderByConfirmed(ID_COUNTRY, EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
        verify { regionStatsDao.getRegionAndStatsByCountryAndLastDateOrderByConfirmed(any()) }
        verify {
            regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any()) wasNot Called
        }
    }

    should("get regions and stats ordered by confirmed should return data if exists") {
        val flow = flow {
            emit(listRegionAndStatsDV)
        }

        every {
            regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any())
        } returns flow

        val flowLocalDs = local.getRegionsStatsOrderByConfirmed(ID_COUNTRY, DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listRegionAndStatsDV.toDomain())
        }
        verify { regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any()) }
        verify {
            regionStatsDao.getRegionAndStatsByCountryAndLastDateOrderByConfirmed(any()) wasNot Called
        }
    }

    should("get regions and stats ordered by confirmed should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionAndStatsDV>())
        }

        every {
            regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any())
        } returns flow

        val flowLocalDs = local.getRegionsStatsOrderByConfirmed(ID_COUNTRY, DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
        verify { regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any()) }
        verify {
            regionStatsDao.getRegionAndStatsByCountryAndLastDateOrderByConfirmed(any()) wasNot Called
        }
    }

    should("get subregions and stats ordered by confirmed with empty date should return data if exists") {
        val flow = flow {
            emit(listSubRegionAndStatsDV)
        }

        every {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listSubRegionAndStatsDV.toDomain())
        }
        verify {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(any(), any())
        }
        verify {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any(), any()) wasNot Called
        }
    }

    should("get subregions and stats ordered by confirmed with empty data should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<SubRegionAndStatsDV>())
        }

        every { subRegionStatsDao.getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(any(), any()) } returns flow

        val flowLocalDs = local.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
        verify {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(any(), any())
        }
        verify {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any(), any()) wasNot Called
        }
    }

    should("get subregions and stats ordered by confirmed should return data if exists") {
        val flow = flow {
            emit(listSubRegionAndStatsDV)
        }

        every {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listSubRegionAndStatsDV.toDomain())
        }
        verify {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any(), any())
        }
        verify {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(any(), any()) wasNot Called
        }
    }

    should("get subregions and stats ordered by confirmed should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<SubRegionAndStatsDV>())
        }

        every {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
        verify {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(any(), any(), any())
        }
        verify {
            subRegionStatsDao.getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(any(), any()) wasNot Called
        }
    }

    should("get regions all stats ordered by confirmed with empty date should return data if exists") {
        val flow = flow {
            emit(listRegionAndStatsPojo)
        }

        every {
            regionStatsDao.getRegionAndAllStatsByCountryAndDateOrderByConfirmed(any())
        } returns flow

        val flowLocalDs = local.getRegionsAllStatsOrderByConfirmed(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listRegionAndStatsPojo.toDomain())
        }
    }

    should("get regions all stats ordered by confirmed with empty data should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionAndStatsPojo>())
        }

        every {
            regionStatsDao.getRegionAndAllStatsByCountryAndDateOrderByConfirmed(any())
        } returns flow

        val flowLocalDs = local.getRegionsAllStatsOrderByConfirmed(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get subregions all stats ordered by confirmed with empty date should return data if exists") {
        val flow = flow {
            emit(listSubRegionAndStatsPojo)
        }

        every {
            subRegionStatsDao.getSubRegionAndAllStatsByCountryAndDateOrderByConfirmed(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAllStatsOrderByConfirmed(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listSubRegionAndStatsPojo.toDomain())
        }
    }

    should("get subregions all stats ordered by confirmed with empty data should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<SubRegionAndStatsPojo>())
        }

        every {
            subRegionStatsDao.getSubRegionAndAllStatsByCountryAndDateOrderByConfirmed(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAllStatsOrderByConfirmed(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get countries and stats most confirmed should return data if exists") {
        val flow = flow {
            emit(listCountryAndOneStatsPojo)
        }

        every {
            countryStatsDao.getCountriesAndStatsWithMostConfirmed()
        } returns flow

        val flowLocalDs = local.getCountriesAndStatsWithMostConfirmed()

        flowLocalDs.collect { data ->
            data.shouldBeRight(listCountryAndStats)
        }
    }

    should("get countries and stats most confirmed should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<CountryAndOneStatsPojo>())
        }

        every {
            countryStatsDao.getCountriesAndStatsWithMostConfirmed()
        } returns flow

        val flowLocalDs = local.getCountriesAndStatsWithMostConfirmed()

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get regions and stats most confirmed should return data if exists") {
        val flow = flow {
            emit(listRegionAndOneStatsPojo)
        }

        every {
            regionStatsDao.getRegionsAndStatsWithMostConfirmed(any())
        } returns flow

        val flowLocalDs = local.getRegionsAndStatsWithMostConfirmed(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeRight(Pair(MenuItemViewType.LineChartMostConfirmed, listRegionAndStats))
        }
    }

    should("get regions and stats most confirmed should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionAndOneStatsPojo>())
        }

        every {
            regionStatsDao.getRegionsAndStatsWithMostConfirmed(any())
        } returns flow

        val flowLocalDs = local.getRegionsAndStatsWithMostConfirmed(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get subregions and stats most confirmed should return data if exists") {
        val flow = flow {
            emit(listSubRegionAndOneStatsPojo)
        }

        every {
            subRegionStatsDao.getSubRegionsAndStatsWithMostConfirmed(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAndStatsWithMostConfirmed(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeRight(Pair(MenuItemViewType.LineChartMostConfirmed, listSubRegionAndStats))
        }
    }

    should("get subregions and stats most confirmed should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<SubRegionAndOneStatsPojo>())
        }

        every {
            subRegionStatsDao.getSubRegionsAndStatsWithMostConfirmed(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAndStatsWithMostConfirmed(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get countries and stats most deaths should return data if exists") {
        val flow = flow {
            emit(listCountryAndOneStatsPojo)
        }

        every {
            countryStatsDao.getCountriesAndStatsWithMostDeaths()
        } returns flow

        val flowLocalDs = local.getCountriesAndStatsWithMostDeaths()

        flowLocalDs.collect { data ->
            data.shouldBeRight(listCountryAndStats)
        }
    }

    should("get countries and stats most deaths should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<CountryAndOneStatsPojo>())
        }

        every {
            countryStatsDao.getCountriesAndStatsWithMostDeaths()
        } returns flow

        val flowLocalDs = local.getCountriesAndStatsWithMostDeaths()

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get regions and stats most deaths should return data if exists") {
        val flow = flow {
            emit(listRegionAndOneStatsPojo)
        }

        every {
            regionStatsDao.getRegionsAndStatsWithMostDeaths(any())
        } returns flow

        val flowLocalDs = local.getRegionsAndStatsWithMostDeaths(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeRight(Pair(MenuItemViewType.LineChartMostDeaths, listRegionAndStats))
        }
    }

    should("get regions and stats most deaths should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionAndOneStatsPojo>())
        }

        every {
            regionStatsDao.getRegionsAndStatsWithMostDeaths(any())
        } returns flow

        val flowLocalDs = local.getRegionsAndStatsWithMostDeaths(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get subregions and stats most deaths should return data if exists") {
        val flow = flow {
            emit(listSubRegionAndOneStatsPojo)
        }

        every {
            subRegionStatsDao.getSubRegionsAndStatsWithMostDeaths(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAndStatsWithMostDeaths(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeRight(Pair(MenuItemViewType.LineChartMostDeaths, listSubRegionAndStats))
        }
    }

    should("get subregions and stats most deaths should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<SubRegionAndOneStatsPojo>())
        }

        every {
            subRegionStatsDao.getSubRegionsAndStatsWithMostDeaths(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAndStatsWithMostDeaths(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get countries and stats most open cases should return data if exists") {
        val flow = flow {
            emit(listCountryAndOneStatsPojo)
        }

        every {
            countryStatsDao.getCountriesAndStatsWithMostOpenCases()
        } returns flow

        val flowLocalDs = local.getCountriesAndStatsWithMostOpenCases()

        flowLocalDs.collect { data ->
            data.shouldBeRight(listCountryAndStats)
        }
    }

    should("get countries and stats most open cases should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<CountryAndOneStatsPojo>())
        }

        every {
            countryStatsDao.getCountriesAndStatsWithMostOpenCases()
        } returns flow

        val flowLocalDs = local.getCountriesAndStatsWithMostOpenCases()

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get regions and stats most open cases should return data if exists") {
        val flow = flow {
            emit(listRegionAndOneStatsPojo)
        }

        every {
            regionStatsDao.getRegionsAndStatsWithMostOpenCases(any())
        } returns flow

        val flowLocalDs = local.getRegionsAndStatsWithMostOpenCases(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeRight(Pair(MenuItemViewType.LineChartMostOpenCases, listRegionAndStats))
        }
    }

    should("get regions and stats most open cases should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionAndOneStatsPojo>())
        }

        every {
            regionStatsDao.getRegionsAndStatsWithMostOpenCases(any())
        } returns flow

        val flowLocalDs = local.getRegionsAndStatsWithMostOpenCases(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get subregions and stats most open cases should return data if exists") {
        val flow = flow {
            emit(listSubRegionAndOneStatsPojo)
        }

        every {
            subRegionStatsDao.getSubRegionsAndStatsWithMostOpenCases(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAndStatsWithMostOpenCases(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeRight(Pair(MenuItemViewType.LineChartMostOpenCases, listSubRegionAndStats))
        }
    }

    should("get subregions and stats most open cases should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<SubRegionAndOneStatsPojo>())
        }

        every {
            subRegionStatsDao.getSubRegionsAndStatsWithMostOpenCases(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAndStatsWithMostOpenCases(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get countries and stats most recovered should return data if exists") {
        val flow = flow {
            emit(listCountryAndOneStatsPojo)
        }

        every {
            countryStatsDao.getCountriesAndStatsWithMostRecovered()
        } returns flow

        val flowLocalDs = local.getCountriesAndStatsWithMostRecovered()

        flowLocalDs.collect { data ->
            data.shouldBeRight(listCountryAndStats)
        }
    }

    should("get countries and stats most recovered should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<CountryAndOneStatsPojo>())
        }

        every {
            countryStatsDao.getCountriesAndStatsWithMostRecovered()
        } returns flow

        val flowLocalDs = local.getCountriesAndStatsWithMostRecovered()

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get regions and stats most recovered should return data if exists") {
        val flow = flow {
            emit(listRegionAndOneStatsPojo)
        }

        every {
            regionStatsDao.getRegionsAndStatsWithMostRecovered(any())
        } returns flow

        val flowLocalDs = local.getRegionsAndStatsWithMostRecovered(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeRight(Pair(MenuItemViewType.LineChartMostRecovered, listRegionAndStats))
        }
    }

    should("get regions and stats most recovered should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionAndOneStatsPojo>())
        }

        every {
            regionStatsDao.getRegionsAndStatsWithMostRecovered(any())
        } returns flow

        val flowLocalDs = local.getRegionsAndStatsWithMostRecovered(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get subregions and stats most recovered should return data if exists") {
        val flow = flow {
            emit(listSubRegionAndOneStatsPojo)
        }

        every {
            subRegionStatsDao.getSubRegionsAndStatsWithMostRecovered(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAndStatsWithMostRecovered(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeRight(Pair(MenuItemViewType.LineChartMostRecovered, listSubRegionAndStats))
        }
    }

    should("get subregions and stats most recovered should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<SubRegionAndOneStatsPojo>())
        }

        every {
            subRegionStatsDao.getSubRegionsAndStatsWithMostRecovered(any(), any())
        } returns flow

        val flowLocalDs = local.getSubRegionsAndStatsWithMostRecovered(ID_COUNTRY, ID_REGION)

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get countries should return data if exists") {
        val flow = flow {
            emit(listCountryEntity)
        }

        every { countryDao.getAll() } returns flow

        val flowLocalDs = local.getCountries()

        flowLocalDs.collect { data ->
            data.shouldBeRight(listCountry)
        }
    }

    should("get countries should return database empty error if data doesn't exists") {
        val flow = flow {
            emit(listOf<CountryEntity>())
        }

        every { countryDao.getAll() } returns flow

        val flowLocalDs = local.getCountries()

        flowLocalDs.collect { data ->
            data.shouldBeLeft(DomainError.DatabaseEmptyData)
        }
    }

    should("get regions by country should return data if exists") {
        val flow = flow {
            emit(listRegionEntity)
        }

        every { regionDao.getByCountry(any()) } returns flow

        val flowLocalDs = local.getRegionsByCountry(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listRegion)
        }
    }

    should("get regions by country should return empty list if data doesn't exists") {
        val flow = flow {
            emit(listOf<RegionEntity>())
        }

        every { regionDao.getByCountry(any()) } returns flow

        val flowLocalDs = local.getRegionsByCountry(ID_COUNTRY)

        flowLocalDs.collect { data ->
            data.shouldBeRight(listRegionEmpty)
        }
    }

    should("get country and stats by empty date should return last data if exists") {
        val flow = flow {
            emit(countryAndOneStatsPojo)
        }

        every { countryStatsDao.getCountryAndStatsByLastDate(any()) } returns flow

        val flowLocalDs = local.getCountryAndStatsByDate(ID_COUNTRY, EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(countryOneStats.copy(regionStats = null))
        }
        verify { countryStatsDao.getCountryAndStatsByLastDate(any()) }
        verify { countryStatsDao.getCountryAndStatsByDate(any(), any()) wasNot Called }
    }

    should("get country and stats by empty date should return database mapper error if data doesn't exists") {
        val flow = flow {
            emit(countryAndOneStatsPojo.copy(country = null))
        }

        every { countryStatsDao.getCountryAndStatsByLastDate(any()) } returns flow

        val flowLocalDs = local.getCountryAndStatsByDate(ID_COUNTRY, EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft()
            data.mapLeft { assertThat(it).isInstanceOf(DomainError.MapperDatabaseError::class.java) }
        }
        verify { countryStatsDao.getCountryAndStatsByLastDate(any()) }
        verify { countryStatsDao.getCountryAndStatsByDate(any(), any()) wasNot Called }
    }

    should("get country and stats by date should return last data if exists") {
        val flow = flow {
            emit(countryAndOneStatsPojo)
        }

        every { countryStatsDao.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowLocalDs = local.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(countryOneStats.copy(regionStats = null))
        }
        verify { countryStatsDao.getCountryAndStatsByDate(any(), any()) }
        verify { countryStatsDao.getCountryAndStatsByLastDate(any()) wasNot Called }
    }

    should("get country and stats by date should return database mapper error if data doesn't exists") {
        val flow = flow {
            emit(countryAndOneStatsPojo.copy(country = null))
        }

        every { countryStatsDao.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowLocalDs = local.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft()
            data.mapLeft { assertThat(it).isInstanceOf(DomainError.MapperDatabaseError::class.java) }
        }
        verify { countryStatsDao.getCountryAndStatsByDate(any(), any()) }
        verify { countryStatsDao.getCountryAndStatsByLastDate(any()) wasNot Called }
    }

    should("get region and stats by empty date should return last data if exists") {
        val flow = flow {
            emit(regionAndOneStatsPojo)
        }

        every { regionStatsDao.getRegionAndStatsByLastDate(any(), any()) } returns flow

        val flowLocalDs = local.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(regionOneStats)
        }
        verify { regionStatsDao.getRegionAndStatsByLastDate(any(), any()) }
        verify { regionStatsDao.getRegionAndStatsByDate(any(), any(), any()) wasNot Called }
    }

    should("get region and stats by empty date should return database mapper error if data doesn't exists") {
        val flow = flow {
            emit(regionAndOneStatsPojo.copy(region = null))
        }

        every { regionStatsDao.getRegionAndStatsByLastDate(any(), any()) } returns flow

        val flowLocalDs = local.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, EMPTY_DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft()
            data.mapLeft { assertThat(it).isInstanceOf(DomainError.MapperDatabaseError::class.java) }
        }
        verify { regionStatsDao.getRegionAndStatsByLastDate(any(), any()) }
        verify { regionStatsDao.getRegionAndStatsByDate(any(), any(), any()) wasNot Called }
    }

    should("get region and stats by date should return last data if exists") {
        val flow = flow {
            emit(regionAndOneStatsPojo)
        }

        every { regionStatsDao.getRegionAndStatsByDate(any(), any(), any()) } returns flow

        val flowLocalDs = local.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, DATE)

        flowLocalDs.collect { data ->
            data.shouldBeRight(regionOneStats)
        }
        verify { regionStatsDao.getRegionAndStatsByDate(any(), any(), any()) }
        verify { regionStatsDao.getRegionAndStatsByLastDate(any(), any()) wasNot Called }
    }

    should("get region and stats by date should return database mapper error if data doesn't exists") {
        val flow = flow {
            emit(regionAndOneStatsPojo.copy(region = null))
        }

        every { regionStatsDao.getRegionAndStatsByDate(any(), any(), any()) } returns flow

        val flowLocalDs = local.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, DATE)

        flowLocalDs.collect { data ->
            data.shouldBeLeft()
            data.mapLeft { assertThat(it).isInstanceOf(DomainError.MapperDatabaseError::class.java) }
        }
        verify { regionStatsDao.getRegionAndStatsByDate(any(), any(), any()) }
        verify { regionStatsDao.getRegionAndStatsByLastDate(any(), any()) wasNot Called }
    }

    should("populate database with covid tracker list should call populate database method") {
        coEvery {
            covidTrackerDao.populateDatabase(any(), any(), any(), any(), any(), any(), any())
        } returns Unit

        local.populateDatabase(listOf(covidTracker))

        coVerify { covidTrackerDao.populateDatabase(any(), any(), any(), any(), any(), any(), any()) }
    }

    should("save covid tracker should should call populate database method") {
        coEvery {
            covidTrackerDao.populateDatabase(any(), any(), any(), any(), any(), any(), any())
        } returns Unit

        local.save(covidTracker)

        coVerify { local.populateDatabase(listOf(covidTracker)) }
        coVerify { covidTrackerDao.populateDatabase(any(), any(), any(), any(), any(), any(), any()) }
    }

    should("get all dates should return a list with all dates") {
        coEvery { worldStatsDao.getAllDates() } returns DATES

        val data = local.getAllDates()

        coVerify { local.getAllDates() }
        data.shouldBeRight()
        data.map { dates ->
            dates shouldBe DATES
        }
    }
})