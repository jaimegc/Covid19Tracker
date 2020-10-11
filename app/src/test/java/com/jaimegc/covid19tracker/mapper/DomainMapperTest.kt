package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.toPojoRegionDomain
import com.jaimegc.covid19tracker.domain.model.toPojoSubRegionDomain
import com.jaimegc.covid19tracker.domain.model.toRegionDomain
import com.jaimegc.covid19tracker.domain.model.toStatsDomain
import com.jaimegc.covid19tracker.domain.model.toSubRegionDomain
import com.jaimegc.covid19tracker.utils.ModelBuilder.country
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryAndOneStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryAndStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryOneStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTracker
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTrackerDto
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTrackerTotalDto
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountry
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryAndStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryOnlyStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegion
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionAndStatsDV
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionAndStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionOnlyStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionAndStatsDV
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionAndStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.listWorldStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.listWorldStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.region
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionAndOneStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionAndStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionOneStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.stats
import com.jaimegc.covid19tracker.utils.ModelBuilder.statsEmbedded
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegion
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegionAndStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegionAndStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegionEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegionStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.worldAndCountriesStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.worldStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.worldStatsEntity
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
    fun covidTrackerDtoToDomain() {
        assertEquals(covidTracker, covidTrackerDto.toDomain(worldStats.date))
    }

    @Test
    fun covidTrackerTotalDtoToDomain() {
        assertEquals(worldStats, covidTrackerTotalDto.toDomain(worldStats.date, worldStats.updatedAt))
    }

    @Test
    fun worldAndCountriesStatsPojoToDomain() {
        assertEquals(covidTracker.copy(listOf(covidTracker.countriesStats.first().copy(regionStats = null))),
            worldAndCountriesStatsPojo.toDomain())
    }

    @Test
    fun listCountryEntityToDomain() {
        assertEquals(listCountry, listCountryEntity.toDomain())
    }

    @Test
    fun listRegionEntityToDomain() {
        assertEquals(listRegion, listRegionEntity.toDomain())
    }

    @Test
    fun countryAndStatsPojoToDomain() {
        assertEquals(countryAndStats, countryAndStatsPojo.toDomain())
    }

    @Test
    fun regionAndStatsPojoToDomain() {
        assertEquals(regionAndStats, regionAndStatsPojo.toDomain())
    }

    @Test
    fun subRegionAndStatsPojoToDomain() {
        assertEquals(subRegionAndStats, subRegionAndStatsPojo.toDomain())
    }

    @Test
    fun countryAndOneStatsPojoToDomain() {
        assertEquals(countryOneStats.copy(regionStats = null), countryAndOneStatsPojo.toDomain())
    }

    @Test
    fun regionAndOneStatsPojoToDomain() {
        assertEquals(regionOneStats, regionAndOneStatsPojo.toDomain())
    }

    @Test
    fun listWorldStatsEntityToDomain() {
        assertEquals(listWorldStats, listWorldStatsEntity.toDomain())
    }

    @Test
    fun worldStatsEntityToDomain() {
        assertEquals(worldStats, worldStatsEntity.toDomain())
    }

    @Test
    fun listCountryAndStatsPojoToDomain() {
        assertEquals(listCountryAndStats, listCountryAndStatsPojo.toDomain())
    }

    @Test
    fun listRegionAndStatsPojoToDomain() {
        assertEquals(listRegionAndStats, listRegionAndStatsPojo.toDomain())
    }

    @Test
    fun listSubRegionAndStatsPojoToDomain() {
        assertEquals(listSubRegionAndStats, listSubRegionAndStatsPojo.toDomain())
    }

    @Test
    fun listCountryStatsEntityToStatsDomain() {
        assertEquals(listCountryOnlyStats, listCountryStatsEntity.toStatsDomain())
    }

    @Test
    fun listRegionStatsEntityToStatsDomain() {
        assertEquals(listRegionOnlyStats, listRegionStatsEntity.toStatsDomain())
    }

    @Test
    fun listRegionAndStatsDVToDomain() {
        assertEquals(listRegionStats.copy(regionStats = listOf(listRegionStats.regionStats.first().copy(subRegionStats = null))),
            listRegionAndStatsDV.toDomain())
    }

    @Test
    fun listSubRegionAndStatsDVToDomain() {
        assertEquals(listSubRegionStats, listSubRegionAndStatsDV.toDomain())
    }

    @Test
    fun listRegionStatsEntityToRegionDomain() {
        assertEquals(listOf(stats), listRegionStatsEntity.toRegionDomain())
    }

    @Test
    fun listSubRegionStatsEntityToSubRegionDomain() {
        assertEquals(listOf(stats), listSubRegionStatsEntity.toSubRegionDomain())
    }

    @Test
    fun regionStatsEntityToDomain() {
        assertEquals(stats, regionStatsEntity.toDomain(stats.date))
    }

    @Test
    fun subRegionStatsEntityToDomain() {
        assertEquals(stats, subRegionStatsEntity.toDomain(stats.date))
    }

    @Test
    fun countryEntityToDomain() {
        assertEquals(country, countryEntity.toDomain())
    }

    @Test
    fun regionEntityToDomain() {
        assertEquals(region, regionEntity.toDomain())
    }

    @Test
    fun subRegionEntityToDomain() {
        assertEquals(subRegion, subRegionEntity.toDomain())
    }

    @Test
    fun statsEmbeddedToDomain() {
        assertEquals(stats, statsEmbedded.toDomain(stats.date))
    }

    @Test
    fun countryStatsEntityToDomain() {
        assertEquals(stats, countryStatsEntity.toDomain())
    }

    @Test
    fun listRegionAndStatsPojoToPojoRegionDomain() {
        assertEquals(listRegionAndStats, listRegionAndStatsPojo.toPojoRegionDomain())
    }

    @Test
    fun listSubRegionAndStatsPojoToPojoSubRegionDomain() {
        assertEquals(listSubRegionAndStats, listSubRegionAndStatsPojo.toPojoSubRegionDomain())
    }
}