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
    fun `listCountryAndOneStatsPojo to pojo countries ordered`() {
        assertEquals(listOf(countryAndStatsPojo), listCountryAndOneStatsPojo.toPojoCountriesOrdered())
    }

    @Test
    fun `listRegionAndOneStatsPojo to pojo regions ordered`() {
        assertEquals(listOf(regionAndStatsPojo), listRegionAndOneStatsPojo.toPojoRegionsOrdered())
    }

    @Test
    fun `listSubRegionAndOneStatsPojo to pojo subregions ordered`() {
        assertEquals(listOf(subRegionAndStatsPojo), listSubRegionAndOneStatsPojo.toPojoSubRegionsOrdered())
    }
}