package com.jaimegc.covid19tracker.mapper.kotest

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
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.*

class DomainMapperKotestTest : StringSpec({

   beforeTest {
      Locale.setDefault(Locale.US)
   }

   "covidTrackerDto to domain" {
      covidTracker shouldBe covidTrackerDto.toDomain(worldStats.date)
   }

   "covidTrackerTotalDto to domain" {
      worldStats shouldBe covidTrackerTotalDto.toDomain(worldStats.date, worldStats.updatedAt)
   }

   "worldAndCountriesStatsPojo to domain" {
      covidTracker.copy(listOf(covidTracker.countriesStats.first().copy(regionStats = null))) shouldBe
          worldAndCountriesStatsPojo.toDomain()
   }

   "listCountryEntity to domain" {
      listCountry shouldBe listCountryEntity.toDomain()
   }

   "listRegionEntity to domain" {
      listRegion shouldBe listRegionEntity.toDomain()
   }

   "countryAndStatsPojo to domain" {
      countryAndStats shouldBe countryAndStatsPojo.toDomain()
   }

   "regionAndStatsPojo to domain" {
      regionAndStats shouldBe regionAndStatsPojo.toDomain()
   }

   "subRegionAndStatsPojo to domain" {
      subRegionAndStats shouldBe subRegionAndStatsPojo.toDomain()
   }

   "countryAndOneStatsPojo to domain" {
      countryOneStats.copy(regionStats = null) shouldBe countryAndOneStatsPojo.toDomain()
   }

   "regionAndOneStatsPojo to domain" {
      regionOneStats shouldBe regionAndOneStatsPojo.toDomain()
   }

   "listWorldStatsEntity to domain" {
      listWorldStats shouldBe listWorldStatsEntity.toDomain()
   }

   "worldStatsEntity to domain" {
      worldStats shouldBe worldStatsEntity.toDomain()
   }

   "listCountryAndStatsPojo to domain" {
      listCountryAndStats shouldBe listCountryAndStatsPojo.toDomain()
   }

   "listRegionAndStatsPojo to domain" {
      listRegionAndStats shouldBe listRegionAndStatsPojo.toDomain()
   }

   "listSubRegionAndStatsPojo to domain" {
      listSubRegionAndStats shouldBe listSubRegionAndStatsPojo.toDomain()
   }

   "listCountryStatsEntity to stats domain" {
      listCountryOnlyStats shouldBe listCountryStatsEntity.toStatsDomain()
   }

   "listRegionStatsEntity to stats domain" {
      listRegionOnlyStats shouldBe listRegionStatsEntity.toStatsDomain()
   }

   "listRegionAndStatsDV to domain" {
      listRegionStats.copy(regionStats = listOf(listRegionStats.regionStats.first().copy(subRegionStats = null))) shouldBe
         listRegionAndStatsDV.toDomain()
   }

   "listSubRegionAndStatsDV to domain" {
      listSubRegionStats shouldBe listSubRegionAndStatsDV.toDomain()
   }

   "listRegionStatsEntity to region domain" {
      listOf(stats) shouldBe listRegionStatsEntity.toRegionDomain()
   }

   "listSubRegionStatsEntity to subregion domain" {
      listOf(stats) shouldBe listSubRegionStatsEntity.toSubRegionDomain()
   }

   "regionStatsEntity to domain" {
      stats shouldBe regionStatsEntity.toDomain(stats.date)
   }

   "subRegionStatsEntity to domain" {
      stats shouldBe subRegionStatsEntity.toDomain(stats.date)
   }

   "countryEntity to domain" {
      country shouldBe countryEntity.toDomain()
   }

   "regionEntity to domain" {
      region shouldBe regionEntity.toDomain()
   }

   "subRegionEntity to domain" {
      subRegion shouldBe subRegionEntity.toDomain()
   }

   "statsEmbedded to domain" {
      stats shouldBe statsEmbedded.toDomain(stats.date)
   }

   "countryStatsEntity to domain" {
      stats shouldBe countryStatsEntity.toDomain()
   }

   "listRegionAndStatsPojo to pojo region domain" {
      listRegionAndStats shouldBe listRegionAndStatsPojo.toPojoRegionDomain()
   }

   "listSubRegionAndStatsPojo to pojo subregion domain" {
      listSubRegionAndStats shouldBe listSubRegionAndStatsPojo.toPojoSubRegionDomain()
   }
})