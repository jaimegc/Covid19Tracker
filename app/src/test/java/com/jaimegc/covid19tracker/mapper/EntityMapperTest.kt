package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.data.room.mapper.toEntity
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.utils.ModelBuilder.country
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryOneStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.region
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.stats
import com.jaimegc.covid19tracker.utils.ModelBuilder.statsEmbedded
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegion
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegionEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegionStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegionStatsEntity
import com.jaimegc.covid19tracker.utils.ModelBuilder.worldStats
import com.jaimegc.covid19tracker.utils.ModelBuilder.worldStatsEntity
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