package com.example.bookreadingapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookreadingapp.data.Dao.BookDao
import com.example.bookreadingapp.data.Dao.ChapterDao
import com.example.bookreadingapp.data.Dao.HeadingDao
import com.example.bookreadingapp.data.Dao.ImageDao
import com.example.bookreadingapp.data.Dao.ParagraphDao
import com.example.bookreadingapp.data.Dao.ReadingDao
import com.example.bookreadingapp.data.Dao.TableDao
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.Reading
import com.example.bookreadingapp.data.entities.Table

// Single instance
@Database(entities = [
    Book::class,
    Chapter::class,
    Heading::class,
    Image::class,
    Paragraph::class,
    Reading::class,
    Table::class],
    version = 1, exportSchema = false)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun BookDao(): BookDao
    abstract fun ChapterDao(): ChapterDao
    abstract fun HeadingDao(): HeadingDao
    abstract fun ImageDao(): ImageDao
    abstract fun ParagraphDao(): ParagraphDao
    abstract fun ReadingDao(): ReadingDao
    abstract fun TableDao(): TableDao

    companion object {
        @Volatile
        private var Instance: LibraryDatabase? = null

        fun getDatabase(context: Context): LibraryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LibraryDatabase::class.java, "bookReading_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}