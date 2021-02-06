package com.jaimegc.covid19tracker.extension.kotest

import com.jaimegc.covid19tracker.common.extensions.formatCompactValue
import com.jaimegc.covid19tracker.common.extensions.formatDecimals
import com.jaimegc.covid19tracker.common.extensions.formatValue
import com.jaimegc.covid19tracker.common.extensions.percentage
import com.jaimegc.covid19tracker.common.extensions.percentageSymbol
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.*

class NumberExtensionKotestTest : StringSpec({

    beforeTest {
        Locale.setDefault(Locale.US)
    }

    "doubleToString" {
        "-5.50" shouldBe (-5.5).formatDecimals()
        "-2.48" shouldBe (-2.48).formatDecimals()
        "-2.48" shouldBe (-2.489999).formatDecimals()
        "-1.00" shouldBe (-1.0011).formatDecimals()
        "0.00" shouldBe 0.0.formatDecimals()
        "1.00" shouldBe 1.0.formatDecimals()
        "15.50" shouldBe 15.5.formatDecimals()
        "15.91" shouldBe 15.91356.formatDecimals()
        "88.50" shouldBe 88.4999999.formatDecimals()
        "100.00" shouldBe 99.99999.formatDecimals()
        "200.00" shouldBe 200.001111111.formatDecimals()
    }

    "longToString" {
        "-5" shouldBe (-5L).formatValue()
        "-2" shouldBe (-2L).formatValue()
        "-1" shouldBe (-1L).formatValue()
        "0" shouldBe 0L.formatValue()
        "1" shouldBe 1L.formatValue()
        "15" shouldBe 15L.formatValue()
        "88" shouldBe 88L.formatValue()
        "99" shouldBe 99L.formatValue()
        "200" shouldBe 200L.formatValue()
    }

    "floatToString" {
        "-5.5" shouldBe (-5.5f).formatValue()
        "-2.48" shouldBe (-2.48f).formatValue()
        "-2.49" shouldBe (-2.489999f).formatValue()
        "-1.001" shouldBe (-1.0011f).formatValue()
        "0" shouldBe 0.0f.formatValue()
        "1" shouldBe 1.0f.formatValue()
        "15.5" shouldBe 15.5f.formatValue()
        "15.914" shouldBe 15.91356f.formatValue()
        "88.5" shouldBe 88.4999999f.formatValue()
        "100" shouldBe 99.99999f.formatValue()
        "200.001" shouldBe 200.001111111f.formatValue()
    }

    "longToCompactString" {
        "1" shouldBe 1L.formatCompactValue()
        "10" shouldBe 10L.formatCompactValue()
        "100" shouldBe 100L.formatCompactValue()
        "1,000" shouldBe 1000L.formatCompactValue()
        "10,000" shouldBe 10000L.formatCompactValue()
        "100,000" shouldBe 100000L.formatCompactValue()
        "1.00M" shouldBe 1000000L.formatCompactValue()
        "10.00M" shouldBe 10000000L.formatCompactValue()
        "100.00M" shouldBe 100000000L.formatCompactValue()
        "1,000.00M" shouldBe 1000000000L.formatCompactValue()
        "10,000.00M" shouldBe 10000000000L.formatCompactValue()
        "100,000.00M" shouldBe 100000000000L.formatCompactValue()
    }

    "percentage" {
        "-99.99" shouldBe (-0.9999221).percentage()
        "-91.97" shouldBe (-0.9197221).percentage()
        "-69.93" shouldBe (-0.6993221).percentage()
        "-49.03" shouldBe (-0.4903221).percentage()
        "-22.10" shouldBe (-0.221).percentage()
        "-0.10" shouldBe (-0.001).percentage()
        "-0.03" shouldBe (-0.0003221).percentage()
        "0.00" shouldBe 0.0.percentage()
        "0.10" shouldBe 0.001.percentage()
        "22.10" shouldBe 0.221.percentage()
        "0.04" shouldBe 0.0003221.percentage()
        "49.03" shouldBe 0.4903221.percentage()
        "69.93" shouldBe 0.6993221.percentage()
        "91.97" shouldBe 0.9197221.percentage()
        "99.99" shouldBe 0.9999221.percentage()
    }

    "percentageWithSymbol" {
        "-0.99%" shouldBe (-0.9999221f).percentageSymbol(100.0f)
        "-0.91%" shouldBe (-0.9197221f).percentageSymbol(100.0f)
        "-0.69%" shouldBe (-0.6993221f).percentageSymbol(100.0f)
        "-0.49%" shouldBe (-0.4903221f).percentageSymbol(100.0f)
        "-0.22%" shouldBe (-0.221f).percentageSymbol(100.0f)
        "-0.00%" shouldBe (-0.001f).percentageSymbol(100.0f)
        "-0.00%" shouldBe (-0.0003221f).percentageSymbol(100.0f)
        "0.00%" shouldBe 0.0f.percentageSymbol(100.0f)
        "0.01%" shouldBe 0.001f.percentageSymbol(100.0f)
        "0.23%" shouldBe 0.221f.percentageSymbol(100.0f)
        "0.00%" shouldBe 0.0003221f.percentageSymbol(100.0f)
        "0.50%" shouldBe 0.4903221f.percentageSymbol(100.0f)
        "0.70%" shouldBe 0.6993221f.percentageSymbol(100.0f)
        "0.92%" shouldBe 0.9197221f.percentageSymbol(100.0f)
        "1.00%" shouldBe 0.9999221f.percentageSymbol(100.0f)
    }
})