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

    "double to string" {
        (-5.5).formatDecimals() shouldBe "-5.50"
        (-2.48).formatDecimals() shouldBe "-2.48"
        (-2.489999).formatDecimals() shouldBe "-2.48"
        (-1.0011).formatDecimals() shouldBe "-1.00"
        0.0.formatDecimals() shouldBe "0.00"
        1.0.formatDecimals() shouldBe "1.00"
        15.5.formatDecimals() shouldBe "15.50"
        15.91356.formatDecimals() shouldBe "15.91"
        88.4999999.formatDecimals() shouldBe "88.50"
        99.99999.formatDecimals() shouldBe "100.00"
        200.001111111.formatDecimals() shouldBe "200.00"
    }

    "long to string" {
        (-5L).formatValue() shouldBe "-5"
        (-2L).formatValue() shouldBe "-2"
        (-1L).formatValue() shouldBe "-1"
        0L.formatValue() shouldBe "0"
        1L.formatValue() shouldBe "1"
        15L.formatValue() shouldBe "15"
        88L.formatValue() shouldBe "88"
        99L.formatValue() shouldBe "99"
        200L.formatValue() shouldBe "200"
    }

    "float to string" {
        (-5.5f).formatValue() shouldBe "-5.5"
        (-2.48f).formatValue() shouldBe "-2.48"
        (-2.489999f).formatValue() shouldBe "-2.49"
        (-1.0011f).formatValue() shouldBe "-1.001"
        0.0f.formatValue() shouldBe "0"
        1.0f.formatValue() shouldBe "1"
        15.5f.formatValue() shouldBe "15.5"
        15.91356f.formatValue() shouldBe "15.914"
        88.4999999f.formatValue() shouldBe "88.5"
        99.99999f.formatValue() shouldBe "100"
        200.001111111f.formatValue() shouldBe "200.001"
    }

    "long to compact string" {
        1L.formatCompactValue() shouldBe "1"
        10L.formatCompactValue() shouldBe "10"
        100L.formatCompactValue() shouldBe "100"
        1000L.formatCompactValue() shouldBe "1,000"
        10000L.formatCompactValue() shouldBe "10,000"
        100000L.formatCompactValue() shouldBe "100,000"
        1000000L.formatCompactValue() shouldBe "1.00M"
        10000000L.formatCompactValue() shouldBe "10.00M"
        100000000L.formatCompactValue() shouldBe "100.00M"
        1000000000L.formatCompactValue() shouldBe "1,000.00M"
        10000000000L.formatCompactValue() shouldBe "10,000.00M"
        100000000000L.formatCompactValue() shouldBe "100,000.00M"
    }

    "percentage" {
        (-0.9999221).percentage() shouldBe "-99.99"
        (-0.9197221).percentage() shouldBe "-91.97"
        (-0.6993221).percentage() shouldBe "-69.93"
        (-0.4903221).percentage() shouldBe "-49.03"
        (-0.221).percentage() shouldBe "-22.10"
        (-0.001).percentage() shouldBe "-0.10"
        (-0.0003221).percentage() shouldBe "-0.03"
        0.0.percentage() shouldBe "0.00"
        0.001.percentage() shouldBe "0.10"
        0.221.percentage() shouldBe "22.10"
        0.0003221.percentage() shouldBe "0.04"
        0.4903221.percentage() shouldBe "49.03"
        0.6993221.percentage() shouldBe "69.93"
        0.9197221.percentage() shouldBe "91.97"
        0.9999221.percentage() shouldBe "99.99"
    }

    "percentage with symbol" {
        (-0.9999221f).percentageSymbol(100.0f) shouldBe "-0.99%"
        (-0.9197221f).percentageSymbol(100.0f) shouldBe "-0.91%"
        (-0.6993221f).percentageSymbol(100.0f) shouldBe "-0.69%"
        (-0.4903221f).percentageSymbol(100.0f) shouldBe "-0.49%"
        (-0.221f).percentageSymbol(100.0f) shouldBe "-0.22%"
        (-0.001f).percentageSymbol(100.0f) shouldBe "-0.00%"
        (-0.0003221f).percentageSymbol(100.0f) shouldBe "-0.00%"
        0.0f.percentageSymbol(100.0f) shouldBe "0.00%"
        0.001f.percentageSymbol(100.0f) shouldBe "0.01%"
        0.221f.percentageSymbol(100.0f) shouldBe "0.23%"
        0.0003221f.percentageSymbol(100.0f) shouldBe "0.00%"
        0.4903221f.percentageSymbol(100.0f) shouldBe "0.50%"
        0.6993221f.percentageSymbol(100.0f) shouldBe "0.70%"
        0.9197221f.percentageSymbol(100.0f) shouldBe "0.92%"
        0.9999221f.percentageSymbol(100.0f) shouldBe "1.00%"
    }
})