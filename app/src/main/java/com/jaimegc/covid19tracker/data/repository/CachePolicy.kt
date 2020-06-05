package com.jaimegc.covid19tracker.data.repository

sealed class CachePolicy {
    object LocalOnly : CachePolicy()
    data class LocalFirst(val isCacheExpired: Boolean) : CachePolicy()
}