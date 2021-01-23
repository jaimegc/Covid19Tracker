package com.jaimegc.covid19tracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import arrow.fx.IO
import arrow.fx.extensions.io.concurrent.parTraverse
import arrow.fx.fix
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.usecase.AddCovidTracker
import com.jaimegc.covid19tracker.domain.usecase.GetDates
import com.jaimegc.covid19tracker.util.FileUtils
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateDatabaseWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    companion object {
        val TAG = UpdateDatabaseWorker::class.java.simpleName
        const val DATA_PROGRESS = "DATA_PROGRESS"
        const val UPDATE_TIME_HOURS = 6L
    }

    private val fileUtils: FileUtils by inject()
    private val remote: RemoteCovidTrackerDatasource by inject()
    private val getDates: GetDates by inject()
    private val covidTrackerPreferences: CovidTrackerPreferences by inject()

    override suspend fun doWork(): Result {
        val currentDates = fileUtils.generateCurrentDates()
        val datesDB = getDates.getAllDates()
        val datesToDownload = mutableListOf<String>()
        val covidTrackers = mutableListOf<CovidTracker>()

        datesDB.map { dates ->
            datesToDownload.addAll(currentDates.minus(dates))
            currentDates.last().let { currentDate ->
                if (datesToDownload.contains(currentDate).not()) datesToDownload.add(currentDate)
            }
        }

        val datesToDownloadSize = datesToDownload.size

        val allRequests = downloadAllDates(datesToDownload)

        allRequests.map { date -> date.map { covidTracker -> covidTrackers.add(covidTracker) } }

        if (covidTrackers.isNotEmpty()) {
            if (datesToDownloadSize > 1) {
                setProgress(workDataOf(DATA_PROGRESS to context.getString(R.string.worker_start)))
                delay(500)
                setProgress(
                    workDataOf(
                        DATA_PROGRESS to context.getString(
                            R.string.worker_populating_database,
                            datesToDownload.first(),
                            datesToDownload.last()
                        )
                    )
                )
            }

            val useCase: AddCovidTracker by inject()
            useCase.addCovidTrackers(covidTrackers)
            // Sometimes this progress is not called
            setProgress(workDataOf(DATA_PROGRESS to context.getString(R.string.worker_finish)))

            // Save preferences if the current day was downloaded
            covidTrackers.firstOrNull { covidTracker ->
                covidTracker.worldStats.date == datesToDownload.last()
            }?.let { covidTrackerPreferences.saveTime() }

            return Result.success()
        } else {
            return Result.failure()
        }
    }

    private fun downloadAllDates(dates: List<String>) =
        dates.parTraverse { date ->
            IO.effect { remote.getCovidTrackerByDate(date) }
        }.fix().unsafeRunSync()
}