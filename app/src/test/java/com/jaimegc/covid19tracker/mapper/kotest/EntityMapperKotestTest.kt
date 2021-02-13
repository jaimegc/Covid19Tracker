package com.jaimegc.covid19tracker.mapper.kotest

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
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.*

class EntityMapperKotestTest : StringSpec({

    beforeTest {
        Locale.setDefault(Locale.US)
    }

    "countryOneStats to entity" {
        countryOneStats.toEntity() shouldBe countryEntity
    }

    "worldStats to entity" {
        worldStats.toEntity() shouldBe worldStatsEntity
    }

    "stats to embedded" {
        statsEmbedded.toDomain(stats.date) shouldBe stats
    }

    "countryStats to entity" {
        stats.toEntity(country.id) shouldBe countryStatsEntity
    }

    "region to entity" {
        region.toEntity(country.id) shouldBe regionEntity
    }

    "subRegion to entity" {
        subRegion.toEntity(region.id, country.id) shouldBe subRegionEntity
    }

    "regionStats to entity" {
        regionStats.toEntity(region.id, country.id) shouldBe regionStatsEntity
    }

    "subRegionStats to entity" {
        subRegionStats.toEntity(subRegion.id, region.id) shouldBe subRegionStatsEntity
    }
})