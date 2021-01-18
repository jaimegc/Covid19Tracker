package com.jaimegc.covid19tracker.worker

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.ListenableWorker.Result
import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.DATE
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTracker
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.usecase.AddCovidTracker
import com.jaimegc.covid19tracker.domain.usecase.GetDates
import com.jaimegc.covid19tracker.utils.FileUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.MockProviderRule
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UpdateDatabaseWorkerRobolectricTest : AutoCloseKoinTest() {
    private lateinit var mockModule: Module
    private lateinit var context: Context
    private val fileUtils = mockk<FileUtils>()
    private val getDates = mockk<GetDates>()
    private val addCovidTracker = mockk<AddCovidTracker>(relaxed = true)
    private val remoteDatasource = mockk<RemoteCovidTrackerDatasource>()
    private val preferences = mockk<CovidTrackerPreferences>(relaxed = true)

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context

        mockModule = module(createdAtStart = true, override = true) {
            single {
                Covid19TrackerDatabase.buildTest(get())
            }
            single {
                fileUtils
            }
            single {
                getDates
            }
            single {
                addCovidTracker
            }
            single {
                remoteDatasource
            }
            single {
                preferences
            }
        }
    }

    @Test
    fun updateDatabaseWorker_shouldReturnSuccess() = runBlocking {
        every { fileUtils.generateCurrentDates() } returns listOf(DATE)
        coEvery { getDates.getAllDates() } returns Either.right(listOf(DATE))
        coEvery {
            remoteDatasource.getCovidTrackerByDate(any())
        } returns Either.right(covidTracker)

        loadKoinModules(mockModule)

        val worker = TestListenableWorkerBuilder<UpdateDatabaseWorker>(context).build()

        val result = worker.doWork()
        assertThat(result).isEqualTo(Result.success())

        coVerify { remoteDatasource.getCovidTrackerByDate(any()) }
        coVerify { addCovidTracker.addCovidTrackers(any()) }
        verify { preferences.saveTime() }
    }

    @Test
    fun updateDatabaseWorkerWithNoInternet_shouldReturnFailure() = runBlocking {
        every { fileUtils.generateCurrentDates() } returns listOf(DATE)
        coEvery { getDates.getAllDates() } returns Either.right(listOf(DATE))
        coEvery {
            remoteDatasource.getCovidTrackerByDate(any())
        } returns Either.left(DomainError.NoInternetError)

        loadKoinModules(mockModule)

        val worker = TestListenableWorkerBuilder<UpdateDatabaseWorker>(context).build()

        val result = worker.doWork()
        assertThat(result).isEqualTo(Result.failure())

        coVerify { remoteDatasource.getCovidTrackerByDate(any()) }
        coVerify(exactly = 0) { addCovidTracker.addCovidTrackers(any()) }
        verify(exactly = 0) { preferences.saveTime() }
    }

    @Test
    fun updateDatabaseWorkerWithServerError_shouldReturnFailure() = runBlocking {
        every { fileUtils.generateCurrentDates() } returns listOf(DATE)
        coEvery { getDates.getAllDates() } returns Either.right(listOf(DATE))
        coEvery {
            remoteDatasource.getCovidTrackerByDate(any())
        } returns Either.left(DomainError.ServerDomainError)

        loadKoinModules(mockModule)

        val worker = TestListenableWorkerBuilder<UpdateDatabaseWorker>(context).build()

        val result = worker.doWork()
        assertThat(result).isEqualTo(Result.failure())

        coVerify { remoteDatasource.getCovidTrackerByDate(any()) }
        coVerify(exactly = 0) { addCovidTracker.addCovidTrackers(any()) }
        verify(exactly = 0) { preferences.saveTime() }
    }

    @Test
    fun updateDatabaseWorkerWithServerTimeout_shouldReturnFailure() = runBlocking {
        every { fileUtils.generateCurrentDates() } returns listOf(DATE)
        coEvery { getDates.getAllDates() } returns Either.right(listOf(DATE))
        coEvery {
            remoteDatasource.getCovidTrackerByDate(any())
        } returns Either.left(DomainError.SocketTimeoutError)

        loadKoinModules(mockModule)

        val worker = TestListenableWorkerBuilder<UpdateDatabaseWorker>(context).build()

        val result = worker.doWork()
        assertThat(result).isEqualTo(Result.failure())

        coVerify { remoteDatasource.getCovidTrackerByDate(any()) }
        coVerify(exactly = 0) { addCovidTracker.addCovidTrackers(any()) }
        verify(exactly = 0) { preferences.saveTime() }
    }

    @Test
    fun updateDatabaseWorkerWithCurrentDateNotDownloaded_shouldNotCallSaveTime() = runBlocking {
        every { fileUtils.generateCurrentDates() } returns listOf("2020-10-04")
        coEvery { getDates.getAllDates() } returns Either.right(listOf(DATE, "2020-10-03"))
        coEvery {
            remoteDatasource.getCovidTrackerByDate(any())
        } returns Either.right(covidTracker)

        loadKoinModules(mockModule)

        val worker = TestListenableWorkerBuilder<UpdateDatabaseWorker>(context).build()

        val result = worker.doWork()
        assertThat(result).isEqualTo(Result.success())

        coVerify { remoteDatasource.getCovidTrackerByDate(any()) }
        coVerify { addCovidTracker.addCovidTrackers(any()) }
        verify(exactly = 0) { preferences.saveTime() }
    }
}