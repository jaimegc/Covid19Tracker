package com.jaimegc.covid19tracker.extension

import com.jaimegc.covid19tracker.common.extensions.formatCompactValue
import com.jaimegc.covid19tracker.common.extensions.formatDecimals
import com.jaimegc.covid19tracker.common.extensions.formatValue
import com.jaimegc.covid19tracker.common.extensions.percentage
import com.jaimegc.covid19tracker.common.extensions.percentageSymbol
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class NumberExtensionTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun doubleToString() {
        assertEquals("-5.50", (-5.5).formatDecimals())
        assertEquals("-2.48", (-2.48).formatDecimals())
        assertEquals("-2.48", (-2.489999).formatDecimals())
        assertEquals("-1.00", (-1.0011).formatDecimals())
        assertEquals("0.00", 0.0.formatDecimals())
        assertEquals("1.00", 1.0.formatDecimals())
        assertEquals("15.50", 15.5.formatDecimals())
        assertEquals("15.91", 15.91356.formatDecimals())
        assertEquals("88.50", 88.4999999.formatDecimals())
        assertEquals("100.00", 99.99999.formatDecimals())
        assertEquals("200.00", 200.001111111.formatDecimals())
    }

    @Test
    fun longToString() {
        assertEquals("-5", (-5L).formatValue())
        assertEquals("-2", (-2L).formatValue())
        assertEquals("-1", (-1L).formatValue())
        assertEquals("0", 0L.formatValue())
        assertEquals("1", 1L.formatValue())
        assertEquals("15", 15L.formatValue())
        assertEquals("88", 88L.formatValue())
        assertEquals("99", 99L.formatValue())
        assertEquals("200", 200L.formatValue())
    }

    @Test
    fun floatToString() {
        assertEquals("-5.5", (-5.5f).formatValue())
        assertEquals("-2.48", (-2.48f).formatValue())
        assertEquals("-2.49", (-2.489999f).formatValue())
        assertEquals("-1.001", (-1.0011f).formatValue())
        assertEquals("0", 0.0f.formatValue())
        assertEquals("1", 1.0f.formatValue())
        assertEquals("15.5", 15.5f.formatValue())
        assertEquals("15.914", 15.91356f.formatValue())
        assertEquals("88.5", 88.4999999f.formatValue())
        assertEquals("100", 99.99999f.formatValue())
        assertEquals("200.001", 200.001111111f.formatValue())
    }

    @Test
    fun longToCompactString() {
        assertEquals("1", 1L.formatCompactValue())
        assertEquals("10", 10L.formatCompactValue())
        assertEquals("100", 100L.formatCompactValue())
        assertEquals("1,000", 1000L.formatCompactValue())
        assertEquals("10,000", 10000L.formatCompactValue())
        assertEquals("100,000", 100000L.formatCompactValue())
        assertEquals("1.00M", 1000000L.formatCompactValue())
        assertEquals("10.00M", 10000000L.formatCompactValue())
        assertEquals("100.00M", 100000000L.formatCompactValue())
        assertEquals("1,000.00M", 1000000000L.formatCompactValue())
        assertEquals("10,000.00M", 10000000000L.formatCompactValue())
        assertEquals("100,000.00M", 100000000000L.formatCompactValue())
    }

    @Test
    fun percentage() {
        assertEquals("-99.99", (-0.9999221).percentage())
        assertEquals("-91.97", (-0.9197221).percentage())
        assertEquals("-69.93", (-0.6993221).percentage())
        assertEquals("-49.03", (-0.4903221).percentage())
        assertEquals("-22.10", (-0.221).percentage())
        assertEquals("-0.10", (-0.001).percentage())
        assertEquals("-0.03", (-0.0003221).percentage())
        assertEquals("0.00", 0.0.percentage())
        assertEquals("0.10", 0.001.percentage())
        assertEquals("22.10", 0.221.percentage())
        assertEquals("0.04", 0.0003221.percentage())
        assertEquals("49.03", 0.4903221.percentage())
        assertEquals("69.93", 0.6993221.percentage())
        assertEquals("91.97", 0.9197221.percentage())
        assertEquals("99.99", 0.9999221.percentage())
    }

    @Test
    fun percentageWithSymbol() {
        assertEquals("-0.99%", (-0.9999221f).percentageSymbol(100.0f))
        assertEquals("-0.91%", (-0.9197221f).percentageSymbol(100.0f))
        assertEquals("-0.69%", (-0.6993221f).percentageSymbol(100.0f))
        assertEquals("-0.49%", (-0.4903221f).percentageSymbol(100.0f))
        assertEquals("-0.22%", (-0.221f).percentageSymbol(100.0f))
        assertEquals("-0.00%", (-0.001f).percentageSymbol(100.0f))
        assertEquals("-0.00%", (-0.0003221f).percentageSymbol(100.0f))
        assertEquals("0.00%", 0.0f.percentageSymbol(100.0f))
        assertEquals("0.01%", 0.001f.percentageSymbol(100.0f))
        assertEquals("0.23%", 0.221f.percentageSymbol(100.0f))
        assertEquals("0.00%", 0.0003221f.percentageSymbol(100.0f))
        assertEquals("0.50%", 0.4903221f.percentageSymbol(100.0f))
        assertEquals("0.70%", 0.6993221f.percentageSymbol(100.0f))
        assertEquals("0.92%", 0.9197221f.percentageSymbol(100.0f))
        assertEquals("1.00%", 0.9999221f.percentageSymbol(100.0f))
    }
}