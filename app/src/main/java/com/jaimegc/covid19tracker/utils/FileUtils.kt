package com.jaimegc.covid19tracker.utils

import android.content.Context
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import java.io.*
import java.util.zip.ZipInputStream

class FileUtils(private val context: Context) {

   suspend fun initDatabase() {
        try {
            if (context.getDatabasePath(Covid19TrackerDatabase.DATABASE_NAME).exists().not()) {
                val bis = BufferedInputStream(context.assets.open(
                    "${Covid19TrackerDatabase.DATABASE_NAME}.zip"))
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
}