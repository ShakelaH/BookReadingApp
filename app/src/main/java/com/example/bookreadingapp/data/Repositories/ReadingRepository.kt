package com.example.bookreadingapp.data.Repositories

import com.example.bookreadingapp.data.Dao.ReadingDao
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.Reading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch

class ReadingRepository(private val readingDao: ReadingDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var currentReading: Flow<Reading> = emptyFlow()
    private var currentBook : Flow<Book> = emptyFlow()

    private fun setCurrentReading(isbn: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            currentReading = readingDao.getReading(isbn)
        }
    }

    fun getCurrentReading(isbn: Int): Flow<Reading> {
        setCurrentReading(isbn)
        return currentReading
    }

    fun insertReading(reading: Reading) {
        coroutineScope.launch(Dispatchers.IO) {
            readingDao.insert(reading)
        }
    }

    fun updateReading(reading: Reading){
        coroutineScope.launch(Dispatchers.IO) {
            readingDao.update(reading)
        }
    }

    fun deleteReading(reading: Reading) {
        coroutineScope.launch(Dispatchers.IO) {
            readingDao.delete(reading)
        }
    }
}