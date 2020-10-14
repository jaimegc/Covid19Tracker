package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.data.room.mapper.toPojoCountriesOrdered
import com.jaimegc.covid19tracker.data.room.mapper.toPojoRegionsOrdered
import com.jaimegc.covid19tracker.data.room.mapper.toPojoSubRegionsOrdered
import com.jaimegc.covid19tracker.utils.ModelBuilder.countryAndStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.listCountryAndOneStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.listRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.listSubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.regionAndStatsPojo
import com.jaimegc.covid19tracker.utils.ModelBuilder.subRegionAndStatsPojo
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class PojoMapperTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun listCountryAndOneStatsPojoToPojoCountriesOrdered() {
        assertEquals(listOf(countryAndStatsPojo), listCountryAndOneStatsPojo.toPojoCountriesOrdered())
    }

    @Test
    fun listRegionAndOneStatsPojoToPojoRegionsOrdered() {
        assertEquals(listOf(regionAndStatsPojo), listRegionAndOneStatsPojo.toPojoRegionsOrdered())
    }

    @Test
    fun listSubRegionAndOneStatsPojoToPojoSubRegionsOrdered() {
        assertEquals(listOf(subRegionAndStatsPojo), listSubRegionAndOneStatsPojo.toPojoSubRegionsOrdered())
    }
}