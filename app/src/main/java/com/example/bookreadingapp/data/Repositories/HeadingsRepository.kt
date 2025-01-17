package com.example.bookreadingapp.data.Repositories

import com.example.bookreadingapp.data.Dao.HeadingDao
import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Paragraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HeadingsRepository(private val headingDao: HeadingDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getAllHeadingsByChapter(chapterId: Int): Flow<List<Heading>> {
        return headingDao.getAllHeadings(chapterId)
    }

    fun insertHeading(heading: Heading) {
        coroutineScope.launch(Dispatchers.IO) {
            headingDao.insert(heading)
        }
    }

    fun deleteHeading(heading: Heading) {
        coroutineScope.launch(Dispatchers.IO) {
            headingDao.delete(heading)
        }
    }
}