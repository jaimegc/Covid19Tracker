package com.jaimegc.covid19tracker.extensions

import arrow.core.Either
import arrow.core.Left
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {

    fun main(work: suspend(() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch { work() }

    fun io(work: suspend(() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch { work() }

    fun default(work: suspend(() -> Unit)) =
        CoroutineScope(Dispatchers.Default).launch { work() }

    fun unconfined(work: suspend(() -> Unit)) =
        CoroutineScope(Dispatchers.Unconfined).launch { work() }
}

fun <B, C>Either<B, C>.ola(f: (B) -> C) =
    fold(::Left, { Either.Right(f) })

