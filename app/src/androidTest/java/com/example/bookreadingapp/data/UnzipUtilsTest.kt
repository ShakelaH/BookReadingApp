package com.example.bookreadingapp.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookreadingapp.data.UnzipUtils.unzip
import org.junit.jupiter.api.Assertions.*
import java.nio.file.Files
import java.io.File
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@RunWith(AndroidJUnit4::class)
class UnzipUtilsTest {

    @Test
    fun testUnzipWorks() {
        val tempDir = Files.createTempDirectory("test-unzip").toFile()
        val zipFile = File(tempDir, "test.zip")
        val destDir = File(tempDir, "output")

        ZipOutputStream(zipFile.outputStream()).use { zipOut ->
            val files = listOf("file1.txt", "dir1/file2.txt", "dir2/file3.txt")
            files.forEach{ path ->
                val entry = ZipEntry(path)
                zipOut.putNextEntry(entry)
                zipOut.write("Content of $path".toByteArray())
                zipOut.closeEntry()
            }
        }

        unzip(zipFile, destDir.absolutePath)

        val expectedFiles = listOf(
            "file1.txt",
            "dir1/file2.txt",
            "dir2/file3.txt"
        )

        expectedFiles.forEach { path ->
            val extractedFile = File(destDir, path)
            assertTrue(extractedFile.exists(), "File $path should exist")
            assertEquals(
                "Content of $path",
                extractedFile.readText(),
                "Content of file $path should match"
            )
        }

        tempDir.deleteRecursively()
    }

    @Test
    fun testUnzipDoesNotWorkIfGivenWrongInfo() {
        val tempDir = Files.createTempDirectory("test-unzip").toFile()
        val zipFile = File(tempDir, "test.zip")
        val destDir = File(tempDir, "output")

        ZipOutputStream(zipFile.outputStream()).use { zipOut ->
            val files = listOf("file1.txt", "dir1/file2.txt", "dir2/file3.txt")
            files.forEach{ path ->
                val entry = ZipEntry(path)
                zipOut.putNextEntry(entry)
                zipOut.write("Content of $path".toByteArray())
                zipOut.closeEntry()
            }
        }

        unzip(zipFile, destDir.absolutePath)

        val expectedFiles = listOf(
            "no.txt",
            "dir1/why.txt",
            "dir2/yes.txt"
        )

        expectedFiles.forEach { path ->
            val extractedFile = File(destDir, path)
            assertFalse(extractedFile.exists(), "File $path should exist")
            assertNotEquals(
                "Content of $path",
                extractedFile.readText(),
                "Content of file $path should match"
            )
        }

        tempDir.deleteRecursively()
    }
}