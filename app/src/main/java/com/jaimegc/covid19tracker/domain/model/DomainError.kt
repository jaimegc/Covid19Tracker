package com.jaimegc.covid19tracker.domain.model

sealed class DomainError {
    object NoInternetDomainError : DomainError()
    object SocketTimeoutError : DomainError()
    object GenericDomainError : DomainError()
    object ServerDomainError : DomainError()
    object ServerDataDomainError : DomainError()
    object ServerForbiddenDomainError : DomainError()
    object EmptyData : DomainError()
    data class UnknownDomainError(val errorMessage: String = "Unknown Error") : DomainError()
    data class ParseDomainError(val errorMessage: String = "Parse Error") : DomainError()
    data class NotIndexStringFoundDomainError(val key: String) : DomainError()
    object DatabaseDomainError : DomainError()
}
