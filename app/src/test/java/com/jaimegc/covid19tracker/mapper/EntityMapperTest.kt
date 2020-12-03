package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.data.room.mapper.toEntity
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.ModelFactoryTest.country
import com.jaimegc.covid19tracker.ModelFactoryTest.countryEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.countryOneStats
import com.jaimegc.covid19tracker.ModelFactoryTest.countryStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.region
import com.jaimegc.covid19tracker.ModelFactoryTest.regionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.regionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.stats
import com.jaimegc.covid19tracker.ModelFactoryTest.statsEmbedded
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegion
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionStats
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionStatsEntity
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStats
import com.jaimegc.covid19tracker.ModelFactoryTest.worldStatsEntity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class EntityMapperTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun `countryOneStats to entity`() {
        assertEquals(countryEntity, countryOneStats.toEntity())
    }

    @Test
    fun `worldStats to entity`() {
        assertEquals(worldStatsEntity, worldStats.toEntity())
    }

    @Test
    fun `stats to embedded`() {
        assertEquals(stats, statsEmbedded.toDomain(stats.date))
    }

    @Test
    fun `countryStats to entity`() {
        assertEquals(countryStatsEntity, stats.toEntity(country.id))
    }

    @Test
    fun `region to entity`() {
        assertEquals(regionEntity, region.toEntity(country.id))
    }

    @Test
    fun `subRegion to entity`() {
        assertEquals(subRegionEntity, subRegion.toEntity(region.id, country.id))
    }

    @Test
    fun `regionStats to entity`() {
        assertEquals(regionStatsEntity, regionStats.toEntity(region.id, country.id))
    }

    @Test
    fun `subRegionStats to entity`() {
        assertEquals(subRegionStatsEntity, subRegionStats.toEntity(subRegion.id, region.id))
    }
}