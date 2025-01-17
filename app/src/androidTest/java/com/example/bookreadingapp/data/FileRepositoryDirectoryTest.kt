package com.example.bookreadingapp.data

import android.content.Context
import android.os.Environment
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.File

class FileRepositoryDirectoryTest {

    @Test
    fun createFileTest() {
        val mockContext: Context = mock<Context>{
            on { getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) } doReturn File("testHome")
        }
        val repository = FileRepository(mockContext)

        val file = repository.createFile("testDir", "testFile")
        val expectedFile = File("testHome/testDir", "testFile")

        Assert.assertEquals(expectedFile, file)
    }

    @Test
    fun createFileTestWithNoDir() {
        val mockContext: Context = mock<Context>{
            on { getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) } doReturn null
        }
        val repository = FileRepository(mockContext)

        val file = repository.createFile("testDir", "testFile")
        val expectedFile = File("testDir", "testFile")

        Assert.assertEquals(expectedFile, file)
    }

    @Test
    fun listDirectoryContentsTest() {
        val mockContext: Context = mock<Context>{
            on { getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) } doReturn null
        }
        val repository = FileRepository(mockContext)

        val fileList = repository.listDirectoryContents("testHome")
        val expectedList = ArrayList<String>()

        Assert.assertEquals(expectedList, fileList)
    }
}