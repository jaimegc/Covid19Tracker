package com.jaimegc.covid19tracker.domain.model

sealed class DomainError {
    object NoInternetError : DomainError()
    object SocketTimeoutError : DomainError()
    object GenericDomainError : DomainError()
    object ServerDomainError : DomainError()
    object ServerDataDomainError : DomainError()
    object ServerForbiddenDomainError : DomainError()
    data class MapperDomainError(val errorMessage: String = "Mapper Domain Error") : DomainError()
    data class UnknownDatabaseError(val errorMessage: String = "Unknown Database Error") : DomainError()
    data class MapperDatabaseError(val errorMessage: String = "Mapper Database Error") : DomainError()
    object DatabaseEmptyData : DomainError()
}
