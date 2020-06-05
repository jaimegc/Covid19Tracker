package com.jaimegc.covid19tracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.Either
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.usecase.GetAllDates
import com.jaimegc.covid19tracker.utils.FileUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject

class UpdateDatabaseWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    companion object {
        val TAG = UpdateDatabaseWorker::class.java.simpleName
    }

    private val fileUtils: FileUtils by inject()
    private val remote: RemoteCovidTrackerDatasource by inject()
    private val getAllDates: GetAllDates by inject()
    private val covidTrackerPreferences: CovidTrackerPreferences by inject()

    override suspend fun doWork(): Result {
        val currentDates = fileUtils.generateCurrentDates()
        val datesDB = getAllDates.getAllDates()
        val datesToDownload = mutableListOf<String>()
        val covidTrackers = mutableListOf<CovidTracker>()

        datesDB.map { dates ->
            datesToDownload.addAll(currentDates.minus(dates))
            datesToDownload.add(currentDates.last())
        }

        val allRequests = mutableListOf<Deferred<Either<DomainError, CovidTracker>>>()

        datesToDownload.map { date ->
            coroutineScope {
                allRequests.add(async { remote.getCovidTrackerByDate(date) })
            }
        }

        allRequests.awaitAll().map { result ->
            result.map { covidTracker ->
                covidTrackers.add(covidTracker)
            }
        }

        val localDs: LocalCovidTrackerDatasource by inject()
        localDs.populateDatabase(covidTrackers)

        covidTrackerPreferences.saveTime()

        return Result.success()
    }
}