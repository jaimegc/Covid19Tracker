package com.jaimegc.covid19tracker.data.repository

sealed class CachePolicy {
    object LocalOnly : CachePolicy()
    data class NetworkOnly(val isCacheExpired: Boolean) : CachePolicy()
    data class NetworkFirst(val isCacheExpired: Boolean) : CachePolicy()
}