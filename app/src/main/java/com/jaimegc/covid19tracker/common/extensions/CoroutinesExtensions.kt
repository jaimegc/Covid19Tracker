package com.jaimegc.covid19tracker.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main(work: suspend(() -> Unit)) =
    CoroutineScope(Dispatchers.Main).launch { work() }

fun io(work: suspend(() -> Unit)) =
    CoroutineScope(Dispatchers.IO).launch { work() }

fun default(work: suspend(() -> Unit)) =
    CoroutineScope(Dispatchers.Default).launch { work() }

fun unconfined(work: suspend(() -> Unit)) =
    CoroutineScope(Dispatchers.Unconfined).launch { work() }

fun LifecycleOwner.ioMain(workIO: suspend(() -> Unit), workMain: () -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        workIO()
        launch(Dispatchers.Main) { workMain() }
    }
}