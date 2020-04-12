package com.jaimegc.covid19tracker.data.repository

sealed class CachePolicy {
    object NetworkOnly : CachePolicy()
    object LocalOnly : CachePolicy()
    object LocalFirst : CachePolicy()
}