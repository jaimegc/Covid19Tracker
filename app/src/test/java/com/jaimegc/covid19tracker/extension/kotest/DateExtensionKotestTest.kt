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

    "dateStringToMillisecondsInUTC" {
        DATE_TIMESTAMP shouldBe DATE.dateToMilliseconds()
    }

    "dateMillisecondsToStringInUTC" {
        DATE shouldBe DATE_TIMESTAMP.millisecondsToDate()
    }

    "addSpaceLastUpdatedDateUTC" {
        "$DATE 22:10 UTC" shouldBe "$DATE 22:10UTC".toLastUpdated()
    }
})