package com.example.bookreadingapp.data

import java.io.*
import java.util.zip.ZipFile

// Source: Nitin Praksash https://gist.github.com/NitinPraksash9911/dea21ec4b8ae7df068f8f891187b6d1e
/**
 * UnzipUtils class extracts files and sub-directories of a standard zip file to
 * a destination directory.
 *
 */
object UnzipUtils {
    /**
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unzip(zipFilePath: File, destDirectory: String) {
        File(destDirectory).run {
            if (!exists()) {
                mkdirs()
            }
        }

        ZipFile(zipFilePath).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    // Sanitize the file name
                    val replacedFileName = entry.name.replace("[{}]".toRegex(), "")
                    val filePath = destDirectory + File.separator + replacedFileName

                    if (!entry.isDirectory) {
                        // Ensure the parent directory of the file exists
                        File(filePath).parentFile?.apply {
                            if (!exists()) mkdirs()
                        }
                        // Extract the file
                        extractFile(input, filePath)
                    } else {
                        // If it's a directory, create it (and any parent directories)
                        val dir = File(filePath)
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }
                    }
                }
            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * @param inputStream
     * @param destFilePath
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun extractFile(inputStream: InputStream, destFilePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(destFilePath))
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

    /**
     * Size of the buffer to read/write data
     */
    private const val BUFFER_SIZE = 4096
}

/**
Copyright 2020 Nitin Prakash
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */