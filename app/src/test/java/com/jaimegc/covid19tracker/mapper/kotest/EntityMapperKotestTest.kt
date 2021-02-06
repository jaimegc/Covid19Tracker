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
        countryEntity shouldBe countryOneStats.toEntity()
    }

    "worldStats to entity" {
        worldStatsEntity shouldBe worldStats.toEntity()
    }

    "stats to embedded" {
        stats shouldBe statsEmbedded.toDomain(stats.date)
    }

    "countryStats to entity" {
        countryStatsEntity shouldBe stats.toEntity(country.id)
    }

    "region to entity" {
        regionEntity shouldBe region.toEntity(country.id)
    }

    "subRegion to entity" {
        subRegionEntity shouldBe subRegion.toEntity(region.id, country.id)
    }

    "regionStats to entity" {
        regionStatsEntity shouldBe regionStats.toEntity(region.id, country.id)
    }

    "subRegionStats to entity" {
        subRegionStatsEntity shouldBe subRegionStats.toEntity(subRegion.id, region.id)
    }
})