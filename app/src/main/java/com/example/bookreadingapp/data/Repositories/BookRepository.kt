package com.example.bookreadingapp.data.Repositories

import com.example.bookreadingapp.data.Dao.BookDao
import com.example.bookreadingapp.data.entities.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class BookRepository(private val bookDao: BookDao) {
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()
    private var currentBook : Flow<Book> = emptyFlow()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertBook(newBook: Book) {
        coroutineScope.launch(Dispatchers.IO) {
            bookDao.insert(newBook)
        }
    }

    private fun setCurrentBook(isbn: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            currentBook = bookDao.getBook(isbn)
        }
    }

    fun getCurrentBook(isbn: Int): Flow<Book> {
        setCurrentBook(isbn)
        return currentBook
    }

    fun deleteBook(book: Book) {
        coroutineScope.launch(Dispatchers.IO) {
            bookDao.delete(book)
        }
    }

    fun deleteAllBooks() {
        coroutineScope.launch(Dispatchers.IO) {
            bookDao.deleteAllBooks()
        }
    }
}