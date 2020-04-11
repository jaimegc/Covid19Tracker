package com.jaimegc.covid19tracker.data.api.extensions

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.config.ServerApiClient
import com.jaimegc.covid19tracker.data.api.error.Server403ApiException
import com.jaimegc.covid19tracker.data.api.error.Server404ApiException
import com.jaimegc.covid19tracker.data.api.error.Server500ApiException
import com.jaimegc.covid19tracker.data.api.error.ServerConnectionApiException
import com.jaimegc.covid19tracker.domain.model.DomainError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun Exception.apiException(): DomainError =
    when {
        this is HttpException -> toDomainError()
        this is UnknownHostException -> DomainError.NoInternetDomainError
        this is ServerConnectionApiException -> DomainError.NoInternetDomainError
        this is Server500ApiException -> DomainError.GenericDomainError
        this is Server404ApiException -> DomainError.GenericDomainError
        this is Server403ApiException -> DomainError.ServerForbiddenDomainError
        this is Server403ApiException -> DomainError.ServerForbiddenDomainError
        this is SocketTimeoutException -> DomainError.SocketTimeoutError
        this is ConnectException -> DomainError.NoInternetDomainError
        this is KotlinNullPointerException -> DomainError.ServerDataDomainError
        this.cause != null && this.cause is ConnectException -> DomainError.NoInternetDomainError
        // For all
        else -> DomainError.GenericDomainError
    }

fun HttpException.toDomainError(): DomainError =
    when (this.code()) {
        ServerApiClient.RESPONSE_ERROR_500 -> DomainError.ServerDomainError
        ServerApiClient.RESPONSE_ERROR_404 -> DomainError.ServerDomainError
        ServerApiClient.RESPONSE_ERROR_403 -> DomainError.ServerForbiddenDomainError
        else -> DomainError.ServerDomainError
    }

fun <T, R> mapResponse(response: T, mapper: (T) -> R): Either<DomainError, R> =
    try {
        Either.right(mapper(response))
    } catch (exception: Exception) {
        Either.left(DomainError.UnknownDomainError(exception.toString()))
    }

fun <T, R> mapParse(parse: T, mapper: (T) -> R): Either<DomainError, R> =
    try {
        Either.right(mapper(parse))
    } catch (exception: Exception) {
        Either.left(DomainError.ParseDomainError(exception.toString()))
    }