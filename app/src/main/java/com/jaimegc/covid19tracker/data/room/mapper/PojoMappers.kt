package com.jaimegc.covid19tracker.data.room.mapper

import com.jaimegc.covid19tracker.data.room.pojos.CountryAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.CountryAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.RegionAndStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.data.room.pojos.SubRegionAndStatsPojo
import com.jaimegc.covid19tracker.domain.model.ListRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.RegionAndStats
import com.jaimegc.covid19tracker.domain.model.SubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.toRegionDomain
import com.jaimegc.covid19tracker.domain.model.toSubRegionDomain

fun List<CountryAndOneStatsPojo>.toPojoCountriesOrdered(): List<CountryAndStatsPojo> =
    this.groupBy { it.country }.let { mapCountries ->
        val listCountryAndStatsPojo = mutableListOf<CountryAndStatsPojo>()
        mapCountries.map { countryStats ->
            listCountryAndStatsPojo.add(
                CountryAndStatsPojo(
                    countryStats.key,
                    countryStats.value.map { stats -> stats.countryStats!! }
                )
            )
        }
        listCountryAndStatsPojo
    }

fun List<RegionAndOneStatsPojo>.toPojoRegionsOrdered(): List<RegionAndStatsPojo> =
    this.groupBy { it.region }.let { mapRegions ->
        val listRegionAndStatsPojo = mutableListOf<RegionAndStatsPojo>()
        mapRegions.map { regionStats ->
            listRegionAndStatsPojo.add(
                RegionAndStatsPojo(
                    regionStats.key,
                    regionStats.value.map { stats -> stats.regionStats!! }
                )
            )
        }
        listRegionAndStatsPojo
    }

fun List<SubRegionAndOneStatsPojo>.toPojoSubRegionsOrdered(): List<SubRegionAndStatsPojo> =
    this.groupBy { it.subRegion }.let { mapSubRegions ->
        val listSubRegionAndStatsPojo = mutableListOf<SubRegionAndStatsPojo>()
        mapSubRegions.map { subRegionStats ->
            listSubRegionAndStatsPojo.add(
                SubRegionAndStatsPojo(
                    subRegionStats.key,
                    subRegionStats.value.map { stats -> stats.subRegionStats!! }
                )
            )
        }
        listSubRegionAndStatsPojo
    }

fun List<RegionAndStatsPojo>.toPojoRegionDomain(): ListRegionAndStats =
    ListRegionAndStats(
        map { entity ->
            RegionAndStats(
                region = entity.region!!.toDomain(),
                stats = entity.stats.toRegionDomain()
            )
        }
    )

fun List<SubRegionAndStatsPojo>.toPojoSubRegionDomain(): ListSubRegionAndStats =
    ListSubRegionAndStats(
        map { entity ->
            SubRegionAndStats(
                subRegion = entity.subRegion!!.toDomain(),
                stats = entity.stats.toSubRegionDomain()
            )
        }
    )