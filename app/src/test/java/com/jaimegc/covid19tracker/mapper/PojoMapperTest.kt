package com.jaimegc.covid19tracker.mapper

import com.jaimegc.covid19tracker.data.room.mapper.toPojoCountriesOrdered
import com.jaimegc.covid19tracker.data.room.mapper.toPojoRegionsOrdered
import com.jaimegc.covid19tracker.data.room.mapper.toPojoSubRegionsOrdered
import com.jaimegc.covid19tracker.ModelFactoryTest.countryAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listCountryAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.listSubRegionAndOneStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.regionAndStatsPojo
import com.jaimegc.covid19tracker.ModelFactoryTest.subRegionAndStatsPojo
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