package com.jaimegc.covid19tracker.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule

abstract class UseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineRule()

    @MockK
    internal lateinit var repository: CovidTrackerRepository

    @Before
    fun init() {
        MockKAnnotations.init(this)
    }
}