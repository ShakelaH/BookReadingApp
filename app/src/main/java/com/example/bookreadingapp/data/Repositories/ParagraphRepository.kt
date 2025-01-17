package com.example.bookreadingapp.data.Repositories

import com.example.bookreadingapp.data.Dao.ParagraphDao
import com.example.bookreadingapp.data.entities.Paragraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ParagraphRepository(private val paragraphDao: ParagraphDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getAllParagraphsByChapter(chapterId: Int): Flow<List<Paragraph>> {
        return paragraphDao.getAllParagraphs(chapterId)
    }

    fun getAllParagraphsFromBook(isbn: Int, searchWord: String): Flow<List<ParagraphDao.ParagraphSearchResult>> {
        return paragraphDao.searchParagraphSnippets(isbn, searchWord)
    }

    fun insertParagraph(paragraph: Paragraph) {
        coroutineScope.launch(Dispatchers.IO) {
            paragraphDao.insert(paragraph)
        }
    }

    fun deleteParagraph(paragraph: Paragraph) {
        coroutineScope.launch(Dispatchers.IO) {
            paragraphDao.delete(paragraph)
        }
    }
}