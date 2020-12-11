package com.jaimegc.covid19tracker.data.api.extensions

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.error.Server403ApiException
import com.jaimegc.covid19tracker.data.api.error.Server404ApiException
import com.jaimegc.covid19tracker.data.api.error.Server500ApiException
import com.jaimegc.covid19tracker.data.api.error.ServerConnectionApiException
import com.jaimegc.covid19tracker.domain.model.DomainError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val RESPONSE_ERROR_500: Int = 500
private const val RESPONSE_ERROR_404: Int = 404
private const val RESPONSE_ERROR_403: Int = 403

fun Exception.apiException(): DomainError =
    when {
        this is HttpException -> toDomainError()
        this is UnknownHostException -> DomainError.NoInternetError
        this is ServerConnectionApiException -> DomainError.NoInternetError
        this is Server500ApiException -> DomainError.GenericDomainError
        this is Server404ApiException -> DomainError.GenericDomainError
        this is Server403ApiException -> DomainError.ServerForbiddenDomainError
        this is Server403ApiException -> DomainError.ServerForbiddenDomainError
        this is SocketTimeoutException -> DomainError.SocketTimeoutError
        this is ConnectException -> DomainError.NoInternetError
        this is KotlinNullPointerException -> DomainError.ServerDataDomainError
        this.cause != null && this.cause is ConnectException -> DomainError.NoInternetError
        // For all
        else -> DomainError.GenericDomainError
    }

fun HttpException.toDomainError(): DomainError =
    when (this.code()) {
        RESPONSE_ERROR_500 -> DomainError.ServerDomainError
        RESPONSE_ERROR_404 -> DomainError.ServerDomainError
        RESPONSE_ERROR_403 -> DomainError.ServerForbiddenDomainError
        else -> DomainError.ServerDomainError
    }

fun <T, R> mapResponse(response: T, mapper: (T) -> R): Either<DomainError, R> =
    try {
        Either.right(mapper(response))
    } catch (exception: Exception) {
        Either.left(DomainError.MapperDomainError(exception.toString()))
    }