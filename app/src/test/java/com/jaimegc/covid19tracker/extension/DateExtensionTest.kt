package com.jaimegc.covid19tracker.extension

import com.jaimegc.covid19tracker.ModelFactoryTest.DATE
import com.jaimegc.covid19tracker.ModelFactoryTest.DATE_TIMESTAMP
import com.jaimegc.covid19tracker.common.extensions.dateToMilliseconds
import com.jaimegc.covid19tracker.common.extensions.millisecondsToDate
import com.jaimegc.covid19tracker.common.extensions.toLastUpdated
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class DateExtensionTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun `date string to milliseconds in UTC`() {
        assertEquals(DATE_TIMESTAMP, DATE.dateToMilliseconds())
    }

    @Test
    fun `date milliseconds to string in UTC`() {
        assertEquals(DATE, DATE_TIMESTAMP.millisecondsToDate())
    }

    @Test
    fun `add space last updated date UTC`() {
        assertEquals("$DATE 22:10 UTC", "$DATE 22:10UTC".toLastUpdated())
    }
}