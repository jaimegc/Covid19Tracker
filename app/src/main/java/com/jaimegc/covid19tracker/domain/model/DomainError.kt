package com.jaimegc.covid19tracker.domain.model

sealed class DomainError {
    object NoInternetError : DomainError()
    object SocketTimeoutError : DomainError()
    object GenericDomainError : DomainError()
    object ServerDomainError : DomainError()
    object ServerDataDomainError : DomainError()
    object ServerForbiddenDomainError : DomainError()
    data class MapperDomainError(val errorMessage: String = "Unknown Error") : DomainError()
    data class UnknownDatabaseError(val errorMessage: String = "Database Error") : DomainError()
    object DatabaseEmptyData : DomainError()
}
