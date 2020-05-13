package com.jaimegc.covid19tracker.common

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import java.util.*

/**
 * This LiveData will deliver values even when they are
 * posted very quickly one after another.
 * https://stackoverflow.com/questions/56097647/can-we-use-livedata-without-loosing-any-value
 */
class QueueLiveData<T> : MutableLiveData<T>() {
    private val queuedValues: Queue<T> = LinkedList<T>()

    @Synchronized
    override fun postValue(value: T) {
        queuedValues.offer(value)
        super.postValue(value)
    }

    @MainThread
    @Synchronized
    override fun setValue(value: T) {
        queuedValues.remove(value)
        queuedValues.offer(value)

        while (!queuedValues.isEmpty()) super.setValue(queuedValues.poll())
    }
}