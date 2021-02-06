package com.jaimegc.covid19tracker.util.kotest

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

object ProjectConfig : AbstractProjectConfig() {

    override val parallelism = 1
    override val isolationMode = IsolationMode.InstancePerTest

    private val testDispatcher = TestCoroutineDispatcher()

    override fun beforeAll() {
        super.beforeAll()
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })

        Dispatchers.setMain(testDispatcher)
    }

    override fun afterAll() {
        super.afterAll()
        ArchTaskExecutor.getInstance().setDelegate(null)
        Dispatchers.resetMain()
    }

    fun advanceUntilIdle() = testDispatcher.advanceUntilIdle()

    fun advanceTimeBy(delayMs: Long) = testDispatcher.advanceTimeBy(delayMs)
}