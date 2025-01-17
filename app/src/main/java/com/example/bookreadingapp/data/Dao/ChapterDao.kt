package com.example.bookreadingapp.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookreadingapp.data.entities.Chapter
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chapter: Chapter)

    @Update
    suspend fun update(chapter: Chapter)

    @Delete
    suspend fun delete(chapter: Chapter)

    @Query("SELECT * from chapters WHERE chapter_id = :id")
    fun getChapter(id: Int): Flow<Chapter>

    @Query("SELECT * from chapters WHERE chapter_index = :index AND isbn = :isbn")
    fun getBookChapterByIndex(index: Int, isbn: Int): Flow<Chapter>

    @Query("SELECT MAX(chapter_id) from chapters")
    fun getMostRecentChapterID(): Int

    @Query("SELECT * from chapters ORDER BY chapter_index ASC")
    fun getAllChapters(): Flow<List<Chapter>>

    @Query("SELECT * FROM chapters c INNER JOIN books b ON b.isbn = c.isbn WHERE b.isbn = :isbn ORDER BY chapter_index")
    fun getChaptersFromSpecificBook(isbn: Int): Flow<List<Chapter>>

//    @Query("SELECT tableData AS data, index, chapterId FROM table\n" +
//            "WHERE chapterId = 000003\n" +
//            "UNION SELECT imageData AS data, index, chapterId FROM image\n" +
//            "WHERE chapterId = 000003\n" +
//            "UNION SELECT paragraphData AS data, index, chapterId FROM paragraph\n" +
//            "WHERE chapterId = 000003\n" +
//            "UNION SELECT headingData AS data, index, chapterId FROM heading\n" +
//            "WHERE chapterId = 000003\n" +
//            "ORDER BY 2 ASC;\n")
//    fun getChapterElements(chapterId: Int) // not too sure what the return value of this function would be
}