package com.example.bookreadingapp.data.Repositories

import com.example.bookreadingapp.data.Dao.ChapterDao
import com.example.bookreadingapp.data.entities.Chapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChaptersRepository(private val chapterDao: ChapterDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var currentChapter: Flow<Chapter> = emptyFlow()
    private var mostRecentChapterId: Int = 0

    private var allChaptersFromSpecificBook: List<Chapter> = emptyList()

    private fun setAllChaptersOfSpecificBook(isbn: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            chapterDao.getChaptersFromSpecificBook(isbn).collect {
                allChaptersFromSpecificBook = it
            }
        }
    }

    fun getAllChaptersOfSpecificBook(isbn: Int): List<Chapter> {
        setAllChaptersOfSpecificBook(isbn)
        return allChaptersFromSpecificBook
    }

    private fun setCurrentChapter(chapterIndex: Int, bookISBN: Int) {
//        coroutineScope.launch(Dispatchers.IO) {
            currentChapter = chapterDao.getBookChapterByIndex(index = chapterIndex, isbn = bookISBN)
//        }
    }

    fun getCurrentChapter(chapterIndex: Int, bookISBN: Int): Flow<Chapter> {
        setCurrentChapter(chapterIndex, bookISBN)
        return currentChapter
    }

    private suspend fun setMostRecentChapter() {
        coroutineScope.launch(Dispatchers.IO) {
            mostRecentChapterId = chapterDao.getMostRecentChapterID()
        }.join()
    }
    fun getMostRecentChapter(): Int {
        runBlocking {
            setMostRecentChapter()
        }
        return mostRecentChapterId
    }

    fun insertChapter(chapter: Chapter) {
        coroutineScope.launch(Dispatchers.IO) {
            chapterDao.insert(chapter)
        }
    }
}