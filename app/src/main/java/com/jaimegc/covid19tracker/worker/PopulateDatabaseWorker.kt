package com.jaimegc.covid19tracker.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jaimegc.covid19tracker.common.extensions.DATE_FORMATTER
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.utils.FileUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
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
        private val END_DATE = Triple(2020, 5, 24)
        // PLEASE, USE RESPONSIBLY
        private val START_DATE_SERVER = Triple(2020, 3, 10)
        private val END_DATE_SERVER = Triple(2020, 3, 15)
        private const val DOWNLOAD_JSONS_FROM_SERVER = false
    }

    private val fileUtils: FileUtils by inject()

    override suspend fun doWork(): Result =
        try {
            val listDates = fileUtils.generateDates(START_DATE, END_DATE)
            val covidTrackers = mutableListOf<CovidTracker>()

            listDates.map { date ->
                val dateSrc = "$FOLDER$date$JSON_FILE_EXTENSION"

                val dataJson = applicationContext.assets.open(dateSrc).bufferedReader().use { it.readText() }
                Gson().fromJson(dataJson, CovidTrackerDto::class.java).also { covidTrackerDto ->
                    covidTrackers.add(covidTrackerDto.toDomain(getDateFromFileName(dateSrc)))
                }
            }

            if (covidTrackers.isNotEmpty()) {
                // Tips: Use an emulator to generate the database
                val localDs: LocalCovidTrackerDatasource by inject()
                localDs.populateDatabase(covidTrackers)
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
        val allRequests = mutableListOf<Deferred<Response<JsonObject>>>()

        coroutineScope {
            listDates.map { date ->
                allRequests.add(async { covidTrackerApiClient.getCovidTrackerByDateAsJson(date) })
            }
        }

        allRequests.awaitAll().mapIndexed { index, response ->
            if (response.isSuccessful && response.body() != null) {
                fileUtils.writeFileToInternalStorage(response.body().toString(),
                    "${listDates[index]}$JSON_FILE_EXTENSION", FOLDER_DOWNLOAD)
            }
        }
    }
}