package com.jaimegc.covid19tracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import arrow.core.Either
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.usecase.GetAllDates
import com.jaimegc.covid19tracker.utils.FileUtils
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class UpdateDatabaseWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    companion object {
        val TAG = UpdateDatabaseWorker::class.java.simpleName
        const val DATA_PROGRESS = "DATA_PROGRESS"
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

        val datesToDownloadSize = datesToDownload.size

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

        if (datesToDownloadSize > 1) {
            setProgress(workDataOf(DATA_PROGRESS to context.getString(R.string.worker_start)))
            delay(500)
            setProgress(workDataOf(DATA_PROGRESS to context.getString(
                R.string.worker_populating_database, datesToDownload.first(), datesToDownload.last())))
        }

        val localDs: LocalCovidTrackerDatasource by inject()
        localDs.populateDatabase(covidTrackers)
        // Sometimes this progress is not called
        setProgress(workDataOf(DATA_PROGRESS to context.getString(R.string.worker_finish)))

        covidTrackerPreferences.saveTime()

        return Result.success()
    }
}