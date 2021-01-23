package com.jaimegc.covid19tracker.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import arrow.core.Either
import com.jaimegc.covid19tracker.ScreenStateFactoryTest
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.home.InitializeDatabaseActivity
import com.jaimegc.covid19tracker.ui.home.MainActivity
import com.jaimegc.covid19tracker.util.FileUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

@RunWith(AndroidJUnit4ClassRunner::class)
class InitializeDatabaseActivityTest : KoinTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var scenario: ActivityScenario<InitializeDatabaseActivity>
    private val fileUtils = mockk<FileUtils>()

    private lateinit var mockModule: Module

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Before
    fun setUp() {
        mockModule = module(createdAtStart = true, override = true) {
            single {
                Covid19TrackerDatabase.buildTest(get())
            }
            single {
                fileUtils
            }
        }

        mockRequests()

        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
        unloadKoinModules(mockModule)
    }

    private fun mockRequests() {
        // Empty requests for UpdateDatabaseWorker
        every { fileUtils.generateCurrentDates() } returns listOf()

        declareMock<GetCovidTracker> {
            every { getCovidTrackerByDate(any()) } returns flow {
                emit(Either.right(ScreenStateFactoryTest.stateCovidTrackerEmptyData))
            }
        }
    }

    @Test
    fun ifDatabaseExists_shouldNotCallInitDatabase() {
        every { fileUtils.databaseExists() } returns true

        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(InitializeDatabaseActivity::class.java)

        coVerify(exactly = 0) { fileUtils.initDatabase() }

        scenario.close()
    }

    @Test
    fun ifDatabaseDoesNotExist_shouldCallInitDatabase() {
        every { fileUtils.databaseExists() } returns false
        coEvery { fileUtils.initDatabase() } returns Unit

        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(InitializeDatabaseActivity::class.java)

        coVerify { fileUtils.initDatabase() }

        scenario.close()
    }

    @Test
    fun ifDatabaseExists_shouldOpenMainActivityDirectly() {
        every { fileUtils.databaseExists() } returns true

        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(InitializeDatabaseActivity::class.java)

        intended(hasComponent(MainActivity::class.java.name))

        scenario.close()
    }

    @Test
    fun ifDatabaseDoesNotExist_shouldOpenMainActivityAfterInitDatabase() {
        every { fileUtils.databaseExists() } returns false
        coEvery { fileUtils.initDatabase() } returns Unit

        loadKoinModules(mockModule)

        scenario = ActivityScenario.launch(InitializeDatabaseActivity::class.java)

        intended(hasComponent(MainActivity::class.java.name))

        scenario.close()
    }
}