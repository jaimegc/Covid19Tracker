package com.jaimegc.covid19tracker.extension.kotest

import com.jaimegc.covid19tracker.ModelFactoryTest.DATE
import com.jaimegc.covid19tracker.ModelFactoryTest.DATE_TIMESTAMP
import com.jaimegc.covid19tracker.common.extensions.dateToMilliseconds
import com.jaimegc.covid19tracker.common.extensions.millisecondsToDate
import com.jaimegc.covid19tracker.common.extensions.toLastUpdated
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.*

class DomainMapperKotestTest : StringSpec({

    beforeTest {
        Locale.setDefault(Locale.US)
    }

    "date string to milliseconds in UTC" {
        DATE.dateToMilliseconds() shouldBe DATE_TIMESTAMP
    }

    "date milliseconds to string in UTC" {
        DATE_TIMESTAMP.millisecondsToDate() shouldBe DATE
    }

    "add space last updated date UTC" {
        "$DATE 22:10UTC".toLastUpdated() shouldBe "$DATE 22:10 UTC"
    }
})