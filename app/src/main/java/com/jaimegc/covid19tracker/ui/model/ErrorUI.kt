package com.jaimegc.covid19tracker.ui.model

sealed class ErrorUI {
    object SomeError : ErrorUI()
}