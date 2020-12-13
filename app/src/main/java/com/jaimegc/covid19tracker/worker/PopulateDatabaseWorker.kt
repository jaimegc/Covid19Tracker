package com.jaimegc.covid19tracker.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.usecase.AddCovidTrackers
import com.jaimegc.covid19tracker.utils.FileUtils
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class PopulateDatabaseWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    companion object {
        private val TAG = PopulateDatabaseWorker::class.java.simpleName
        private const val FOLDER = "data/"
        private const val FOLDER_DOWNLOAD = "/data"
        private const val JSON_FILE_EXTENSION = ".json"
        // First day in the API
        private val START_DATE = Triple(2020, 1, 23)
        private val END_DATE = Triple(2020, 6, 13)
        // PLEASE, USE RESPONSIBLY
        private val START_DATE_SERVER = Triple(2020, 6, 5)
        private val END_DATE_SERVER = Triple(2020, 6, 6)
        private const val DOWNLOAD_JSONS_FROM_SERVER = false
    }

    private val fileUtils: FileUtils by inject()
    private val moshi =
        Moshi.Builder().build().adapter(CovidTrackerDto::class.java)

    override suspend fun doWork(): Result =
        try {
            val listDates = fileUtils.generateDates(START_DATE, END_DATE)
            val covidTrackers = mutableListOf<CovidTracker>()

            listDates.map { date ->
                val dateSrc = "$FOLDER$date$JSON_FILE_EXTENSION"

                val dataJson = applicationContext.assets.open(dateSrc).bufferedReader().use { it.readText() }

                moshi.fromJson(dataJson).also { covidTrackerDto ->
                    covidTrackerDto?.let {
                        covidTrackers.add(covidTrackerDto.toDomain(getDateFromFileName(dateSrc)))
                    }
                }
            }

            if (covidTrackers.isNotEmpty()) {
                // Tips: Use an emulator to generate the database
                val useCase: AddCovidTrackers by inject()
                useCase.addCovidTrackers(covidTrackers)
            }

            if (DOWNLOAD_JSONS_FROM_SERVER) downloadAllJsons()

            Result.success()
        } catch (exception: Exception) {
            Log.e(TAG, "Error populating database", exception)
            Result.failure()
        }

    private fun getDateFromFileName(date: String): String =
        date.replace(FOLDER, "").replace(JSON_FILE_EXTENSION, "")

    private suspend fun downloadAllJsons() {
        val listDates = fileUtils.generateDates(START_DATE_SERVER, END_DATE_SERVER)
        val covidTrackerApiClient: CovidTrackerApiClient by inject()
        val allRequests = mutableListOf<Deferred<Response<CovidTrackerDto>>>()

        coroutineScope {
            listDates.map { date ->
                allRequests.add(async { covidTrackerApiClient.getCovidTrackerByDateAsResponse(date) })
            }
        }

        // You can see another way of concurrent requests using Arrow in UpdatebaseWorker class
        allRequests.awaitAll().mapIndexed { index, response ->
            if (response.isSuccessful && response.body() != null) {
                fileUtils.writeFileToInternalStorage(
                    moshi.toJson(response.body()),
                    "${listDates[index]}$JSON_FILE_EXTENSION",
                    FOLDER_DOWNLOAD
                )
            }
        }
    }
}