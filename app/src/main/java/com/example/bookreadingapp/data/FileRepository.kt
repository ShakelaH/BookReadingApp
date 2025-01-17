package com.example.bookreadingapp.data

import android.content.Context
import android.os.Environment
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

// Source: Carlton Davis [link](https://gitlab.com/crdavis/networkandfileio/-/blob/master/app/src/main/java/com/example/networkandfileio/data/repository/FileRepository.kt?ref_type=heads)
open class FileRepository(private val context: Context) {

    fun createFile(directoryName: String, fileName: String): File {
        val downloadFolder = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), directoryName)
        if (!downloadFolder.exists()) downloadFolder.mkdirs()
        return File(downloadFolder, fileName)
    }

    // List directory contents
    fun listDirectoryContents(directoryName: String): List<String> {
        val downloadFolder = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), directoryName)
        return downloadFolder.listFiles()?.map { it.absolutePath } ?: emptyList()
    }

    fun listDownloadDirectoryContents(): List<String> {
        val downloadFolder = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), ".")
        return downloadFolder.listFiles()?.map { it.name } ?: emptyList()
    }

    fun unzipBook(bookTitle: String, file: File): Boolean {
        return try {
            if (file.exists()) {
                // Create a directory for the book inside the app's Downloads folder
                val bookPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), bookTitle)
                UnzipUtils.unzip(file, bookPath.absolutePath)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace() // Log the exception
            false
        }
    }


    // Download file from URL and save it in the specified location
    fun downloadFile(url: String, file: File): Boolean {
        return try {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful || response.body == null) {
                return false
            }

            response.body!!.byteStream().use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    copyStream(inputStream, outputStream)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace() // Log the exception
            false
        }
    }

    // Helper method to copy data from input to output stream
    private fun copyStream(input: InputStream, output: FileOutputStream) {
        val buffer = ByteArray(1024)
        var length: Int
        while (input.read(buffer).also { length = it } > 0) {
            output.write(buffer, 0, length)
        }
    }

    // Delete directory contents directly without IntentSender
    fun deleteDirectoryContents(directoryName: String) {
        val downloadFolder = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), directoryName)
        deleteDirectory(downloadFolder)//.listFiles()?.forEach { it.delete() }
    }

    // Recursive directory deletion code from baeldung: https://www.baeldung.com/kotlin/delete-directories-with-contents
    private fun deleteDirectory(directory: File) {
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    deleteDirectory(file)
                } else {
                    file.delete()
                }
            }
            directory.delete()
        }
    }
}
