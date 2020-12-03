package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.toPojoRegionDomain
import com.jaimegc.covid19tracker.domain.model.toPojoSubRegionDomain
import com.jaimegc.covid19tracker.domain.model.toRegionDomain
import com.jaimegc.covid19tracker.domain.model.toStatsDomain
import com.jaimegc.covid19tracker.domain.model.toSubRegionDomain
import com.jaimegc.covid19tracker.ModelFactoryTest.country
import com.jaimegc.covid19tracker.ModelFactoryTest.countryAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.countryAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.countryAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.countryOneStats
import com.jaimegc.covid19tracker.ModelFactoryTest.countryStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTracker
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTrackerDto
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTrackerTotalDto
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountry
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryOnlyStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegion
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStatsDV
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionOnlyStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStatsDV
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.listWorldStats
import com.jaimegc.covid19tracker.ModelFactoryTest.listWorldStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.region
import com.jaimegc.covid19tracker.ModelFactoryTest.regionAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.regionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.regionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionOneStats
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.stats
import com.jaimegc.covid19tracker.ModelFactoryTest.statsEmbedded
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegion
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionAndStats
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.worldAndCountriesStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStats
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStatsEntity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class DomainMapperTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun `covidTrackerDto to domain`() {
        assertEquals(covidTracker, covidTrackerDto.toDomain(worldStats.date))
    }

    @Test
    fun `covidTrackerTotalDto to domain`() {
        assertEquals(worldStats, covidTrackerTotalDto.toDomain(worldStats.date, worldStats.updatedAt))
    }

    @Test
    fun `worldAndCountriesStatsPojo to domain`() {
        assertEquals(covidTracker.copy(listOf(covidTracker.countriesStats.first().copy(regionStats = null))),
            worldAndCountriesStatsPojo.toDomain())
    }

    @Test
    fun `listCountryEntity to domain`() {
        assertEquals(listCountry, listCountryEntity.toDomain())
    }

    @Test
    fun `listRegionEntity to domain`() {
        assertEquals(listRegion, listRegionEntity.toDomain())
    }

    @Test
    fun `countryAndStatsPojo to domain`() {
        assertEquals(countryAndStats, countryAndStatsPojo.toDomain())
    }

    @Test
    fun `regionAndStatsPojo to domain`() {
        assertEquals(regionAndStats, regionAndStatsPojo.toDomain())
    }

    @Test
    fun `subRegionAndStatsPojo to domain`() {
        assertEquals(subRegionAndStats, subRegionAndStatsPojo.toDomain())
    }

    @Test
    fun `countryAndOneStatsPojo to domain`() {
        assertEquals(countryOneStats.copy(regionStats = null), countryAndOneStatsPojo.toDomain())
    }

    @Test
    fun `regionAndOneStatsPojo to domain`() {
        assertEquals(regionOneStats, regionAndOneStatsPojo.toDomain())
    }

    @Test
    fun `listWorldStatsEntity to domain`() {
        assertEquals(listWorldStats, listWorldStatsEntity.toDomain())
    }

    @Test
    fun `worldStatsEntity to domain`() {
        assertEquals(worldStats, worldStatsEntity.toDomain())
    }

    @Test
    fun `listCountryAndStatsPojo to domain`() {
        assertEquals(listCountryAndStats, listCountryAndStatsPojo.toDomain())
    }

    @Test
    fun `listRegionAndStatsPojo to domain`() {
        assertEquals(listRegionAndStats, listRegionAndStatsPojo.toDomain())
    }

    @Test
    fun `listSubRegionAndStatsPojo to domain`() {
        assertEquals(listSubRegionAndStats, listSubRegionAndStatsPojo.toDomain())
    }

    @Test
    fun `listCountryStatsEntity to stats domain`() {
        assertEquals(listCountryOnlyStats, listCountryStatsEntity.toStatsDomain())
    }

    @Test
    fun `listRegionStatsEntity to stats domain`() {
        assertEquals(listRegionOnlyStats, listRegionStatsEntity.toStatsDomain())
    }

    @Test
    fun `listRegionAndStatsDV to domain`() {
        assertEquals(listRegionStats.copy(regionStats = listOf(listRegionStats.regionStats.first().copy(subRegionStats = null))),
            listRegionAndStatsDV.toDomain())
    }

    @Test
    fun `listSubRegionAndStatsDV to domain`() {
        assertEquals(listSubRegionStats, listSubRegionAndStatsDV.toDomain())
    }

    @Test
    fun `listRegionStatsEntity to region domain`() {
        assertEquals(listOf(stats), listRegionStatsEntity.toRegionDomain())
    }

    @Test
    fun `listSubRegionStatsEntity to subregion domain`() {
        assertEquals(listOf(stats), listSubRegionStatsEntity.toSubRegionDomain())
    }

    @Test
    fun `regionStatsEntity to domain`() {
        assertEquals(stats, regionStatsEntity.toDomain(stats.date))
    }

    @Test
    fun `subRegionStatsEntity to domain`() {
        assertEquals(stats, subRegionStatsEntity.toDomain(stats.date))
    }

    @Test
    fun `countryEntity to domain`() {
        assertEquals(country, countryEntity.toDomain())
    }

    @Test
    fun `regionEntity to domain`() {
        assertEquals(region, regionEntity.toDomain())
    }

    @Test
    fun `subRegionEntity to domain`() {
        assertEquals(subRegion, subRegionEntity.toDomain())
    }

    @Test
    fun `statsEmbedded to domain`() {
        assertEquals(stats, statsEmbedded.toDomain(stats.date))
    }

    @Test
    fun `countryStatsEntity to domain`() {
        assertEquals(stats, countryStatsEntity.toDomain())
    }

    @Test
    fun `listRegionAndStatsPojo to pojo region domain`() {
        assertEquals(listRegionAndStats, listRegionAndStatsPojo.toPojoRegionDomain())
    }

    @Test
    fun `listSubRegionAndStatsPojo to pojo subregion domain`() {
        assertEquals(listSubRegionAndStats, listSubRegionAndStatsPojo.toPojoSubRegionDomain())
    }
}