package com.jaimegc.covid19tracker.util

import com.jaimegc.covid19tracker.common.extensions.millisecondsToDate

class TimeProvider {
    fun getCurrentTime(): String = System.currentTimeMillis().millisecondsToDate()

    fun getCurrentTimeMillis(): Long = System.currentTimeMillis()
}