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
      covidTrackerDto.toDomain(worldStats.date) shouldBe covidTracker
   }

   "covidTrackerTotalDto to domain" {
      covidTrackerTotalDto.toDomain(worldStats.date, worldStats.updatedAt) shouldBe worldStats
   }

   "worldAndCountriesStatsPojo to domain" {
      worldAndCountriesStatsPojo.toDomain() shouldBe
          covidTracker.copy(listOf(covidTracker.countriesStats.first().copy(regionStats = null)))
   }

   "listCountryEntity to domain" {
      listCountryEntity.toDomain() shouldBe listCountry
   }

   "listRegionEntity to domain" {
      listRegionEntity.toDomain() shouldBe listRegion
   }

   "countryAndStatsPojo to domain" {
      countryAndStatsPojo.toDomain() shouldBe countryAndStats
   }

   "regionAndStatsPojo to domain" {
      regionAndStatsPojo.toDomain() shouldBe regionAndStats
   }

   "subRegionAndStatsPojo to domain" {
      subRegionAndStatsPojo.toDomain() shouldBe subRegionAndStats
   }

   "countryAndOneStatsPojo to domain" {
      countryAndOneStatsPojo.toDomain() shouldBe countryOneStats.copy(regionStats = null)
   }

   "regionAndOneStatsPojo to domain" {
      regionAndOneStatsPojo.toDomain() shouldBe regionOneStats
   }

   "listWorldStatsEntity to domain" {
      listWorldStatsEntity.toDomain() shouldBe listWorldStats
   }

   "worldStatsEntity to domain" {
      worldStatsEntity.toDomain() shouldBe worldStats
   }

   "listCountryAndStatsPojo to domain" {
      listCountryAndStatsPojo.toDomain() shouldBe listCountryAndStats
   }

   "listRegionAndStatsPojo to domain" {
      listRegionAndStatsPojo.toDomain() shouldBe listRegionAndStats
   }

   "listSubRegionAndStatsPojo to domain" {
      listSubRegionAndStatsPojo.toDomain() shouldBe listSubRegionAndStats
   }

   "listCountryStatsEntity to stats domain" {
      listCountryStatsEntity.toStatsDomain() shouldBe listCountryOnlyStats
   }

   "listRegionStatsEntity to stats domain" {
      listRegionStatsEntity.toStatsDomain() shouldBe listRegionOnlyStats
   }

   "listRegionAndStatsDV to domain" {
      listRegionAndStatsDV.toDomain() shouldBe
          listRegionStats.copy(regionStats = listOf(listRegionStats.regionStats.first().copy(subRegionStats = null)))
   }

   "listSubRegionAndStatsDV to domain" {
      listSubRegionAndStatsDV.toDomain() shouldBe listSubRegionStats
   }

   "listRegionStatsEntity to region domain" {
      listRegionStatsEntity.toRegionDomain() shouldBe listOf(stats)
   }

   "listSubRegionStatsEntity to subregion domain" {
      listSubRegionStatsEntity.toSubRegionDomain() shouldBe listOf(stats)
   }

   "regionStatsEntity to domain" {
      regionStatsEntity.toDomain(stats.date) shouldBe stats
   }

   "subRegionStatsEntity to domain" {
      subRegionStatsEntity.toDomain(stats.date) shouldBe stats
   }

   "countryEntity to domain" {
      countryEntity.toDomain() shouldBe country
   }

   "regionEntity to domain" {
      regionEntity.toDomain() shouldBe region
   }

   "subRegionEntity to domain" {
      subRegionEntity.toDomain() shouldBe subRegion
   }

   "statsEmbedded to domain" {
      statsEmbedded.toDomain(stats.date) shouldBe stats
   }

   "countryStatsEntity to domain" {
      countryStatsEntity.toDomain() shouldBe stats
   }

   "listRegionAndStatsPojo to pojo region domain" {
      listRegionAndStatsPojo.toPojoRegionDomain() shouldBe listRegionAndStats
   }

   "listSubRegionAndStatsPojo to pojo subregion domain" {
      listSubRegionAndStatsPojo.toPojoSubRegionDomain() shouldBe listSubRegionAndStats
   }
})