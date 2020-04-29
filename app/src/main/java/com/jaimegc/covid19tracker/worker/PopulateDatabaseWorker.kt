package com.jaimegc.covid19tracker.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.toDomain
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream


class PopulateDatabaseWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {
    companion object {
        private val TAG = PopulateDatabaseWorker::class.java.simpleName
        private const val FOLDER = "data/"
        private const val FOLDER_DOWNLOAD = "/data"
        private const val JSON_FILE_EXTENSION = ".json"
        private const val ZIP_FILE_DATES = "dates.zip"
        private val START_DATE = Triple(2020, 1, 23)
        private val END_DATE = Triple(2020, 4, 28)
        private val START_DATE_SERVER = Triple(2020, 3, 10)
        private val END_DATE_SERVER = Triple(2020, 3, 15)
        private const val DATE_FORMATTER = "YYYY-MM-dd"
        private const val DOWNLOAD_JSONS_FROM_SERVER = false
    }

    override suspend fun doWork(): Result =
        try {
            val listDates = generateDateNames(START_DATE, END_DATE)
            val covidTrackers = mutableListOf<CovidTracker>()

            listDates.map { date ->
                val dateSrc = "$FOLDER$date$JSON_FILE_EXTENSION"

                val dataJson = applicationContext.assets.open(dateSrc).bufferedReader().use { it.readText() }
                Gson().fromJson(dataJson, CovidTrackerDto::class.java).also { covidTrackerDto ->
                    covidTrackers.add(covidTrackerDto.toDomain(getDateFromFileName(dateSrc)))
                }
            }

            if (covidTrackers.isNotEmpty()) {
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

    private fun generateDateNames(
        startDateTriple: Triple<Int, Int, Int>,
        endDateTriple: Triple<Int, Int, Int>
    ): List<String> {
        val formatter = SimpleDateFormat(DATE_FORMATTER)
        val dates = mutableListOf<String>()
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        startDate.set(startDateTriple.first, startDateTriple.second - 1, startDateTriple.third)
        endDate.set(endDateTriple.first, endDateTriple.second - 1, endDateTriple.third)

        while (startDate.timeInMillis <= endDate.timeInMillis) {
            dates.add(formatter.format(startDate.timeInMillis))
            startDate.add(Calendar.DATE, 1)
        }

        return dates
    }

    private suspend fun downloadAllJsons() {
        val listDates = generateDateNames(START_DATE_SERVER, END_DATE_SERVER)
        val covidTrackerApiClient: CovidTrackerApiClient by inject()
        val allRequests = mutableListOf<Deferred<Response<JsonObject>>>()

        coroutineScope {
            listDates.map { date ->
                allRequests.add(async { covidTrackerApiClient.getCovidTrackerByDateAsJson(date) })
            }
        }

        allRequests.awaitAll().mapIndexed { index, response ->
            if (response.isSuccessful && response.body() != null) {
                writeFileToInternalStorage(response.body().toString(),
                    "${listDates[index]}$JSON_FILE_EXTENSION", FOLDER_DOWNLOAD)
            }
        }
    }

    private suspend fun writeFileToInternalStorage(text: String, fileName: String, folder: String) {
        try {
            val directory = File(context.filesDir, folder)
            if (directory.exists().not()) directory.mkdirs()
            val file = File(directory, fileName)
            val writer = FileWriter(file)
            writer.append(text)
            writer.flush()
            writer.close()
        } catch (exception: IOException) {
            Log.e("Exception", "File write internal storage failed: $exception")
        }
    }

    // TODO: This is not ready
    private suspend fun openZipFile() {
        applicationContext.assets.open("${FOLDER}$ZIP_FILE_DATES").use { stream ->
            ZipInputStream(stream).use { zipStream ->

                val dataStream = DataInputStream(zipStream)
                stream.readBytes()
                stream.close()

                val zipFileeee = "${context.filesDir}${File.separator}Holita"

                val zipFiles = ZipFile(zipFileeee)

                var zipFile = zipStream.nextEntry
                var length = 0

                while (zipFile != null) {
                    if (zipFile.isDirectory.not()) {

                    }
                    zipFile = zipStream.nextEntry
                }
            }
        }
    }
}