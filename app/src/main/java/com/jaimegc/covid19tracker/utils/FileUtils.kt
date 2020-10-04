package com.jaimegc.covid19tracker.utils

import android.content.Context
import android.util.Log
import com.jaimegc.covid19tracker.common.extensions.DATE_FORMATTER
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.zip.ZipInputStream

class FileUtils(private val context: Context) {

    companion object {
        // First day in the API
        private val START_DATE_SERVER = Triple(2020, 1, 23)
    }

    suspend fun initDatabase() {
        try {
            if (context.getDatabasePath(Covid19TrackerDatabase.DATABASE_NAME).exists().not()) {
                val bis = BufferedInputStream(
                    context.assets.open(
                        "${Covid19TrackerDatabase.DATABASE_NAME}.zip"
                    )
                )
                val zis = ZipInputStream(bis).also { it.nextEntry }
                val destinationFile = File(context.filesDir, Covid19TrackerDatabase.DATABASE_NAME)
                val inputStream = BufferedInputStream(zis)
                destinationFile.parentFile?.mkdirs()

                saveFile(destinationFile, inputStream)
            } else {
                File(context.filesDir, Covid19TrackerDatabase.DATABASE_NAME).let { file ->
                    file.exists().let { exists -> if (exists) file.delete() }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private suspend fun saveFile(file: File, bufferedInputStream: BufferedInputStream) {
        var length = 0
        val buffer = ByteArray(2048)
        val fileOutputStream = FileOutputStream(file)
        val bufferedOutputStream = BufferedOutputStream(fileOutputStream, 2048)

        while (length != -1) {
            length = bufferedInputStream.read(buffer, 0, 2048)
            if (length != -1) {
                bufferedOutputStream.write(buffer, 0, length)
            } else {
                bufferedOutputStream.flush()
                bufferedOutputStream.close()
                bufferedInputStream.close()
            }
        }
    }

    fun generateCurrentDates(): List<String> =
        Calendar.getInstance().let { date ->
            generateDates(
                START_DATE_SERVER,
                Triple(
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DAY_OF_MONTH)
                )
            )
        }

    fun generateDates(
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

    suspend fun writeFileToInternalStorage(text: String, fileName: String, folder: String) {
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
}