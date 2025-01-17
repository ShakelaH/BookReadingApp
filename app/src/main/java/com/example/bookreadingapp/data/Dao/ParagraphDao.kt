package com.example.bookreadingapp.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookreadingapp.data.entities.Paragraph
import kotlinx.coroutines.flow.Flow

@Dao
interface ParagraphDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(paragraph: Paragraph)

    @Update
    suspend fun update(paragraph: Paragraph)

    @Delete
    suspend fun delete(paragraph: Paragraph)

    @Query("SELECT * from paragraphs WHERE paragraph_id = :id")
    fun getParagraph(id: Int): Flow<Paragraph>

    @Query("SELECT * FROM paragraphs WHERE chapter_id = :id ORDER BY paragraph_index ASC")
    fun getAllParagraphs(id: Int): Flow<List<Paragraph>>

    @Query("""
        SELECT 
        CASE 
            WHEN INSTR(p.paragraph_data, :searchWord) > 10 THEN 
                '...' || SUBSTR(p.paragraph_data, INSTR(p.paragraph_data, :searchWord) - 10, 10) || :searchWord || 
                SUBSTR(p.paragraph_data, INSTR(p.paragraph_data, :searchWord) + LENGTH(:searchWord), 10) || '...'
            ELSE 
                SUBSTR(p.paragraph_data, 1, INSTR(p.paragraph_data, :searchWord) - 1) || :searchWord || 
                SUBSTR(p.paragraph_data, INSTR(p.paragraph_data, :searchWord) + LENGTH(:searchWord), 10) || '...'
        END AS snippet,
        c.chapter_index AS chapterIndex,
        p.paragraph_index AS paragraphIndex
        FROM paragraphs p
        INNER JOIN chapters c ON c.chapter_id = p.chapter_id
        WHERE c.isbn = :isbn
          AND p.paragraph_data LIKE '%' || :searchWord || '%'
        ORDER BY c.chapter_index, p.paragraph_index
    """)
    fun searchParagraphSnippets(isbn: Int, searchWord: String): Flow<List<ParagraphSearchResult>>

    data class ParagraphSearchResult(
        val snippet: String,
        val chapterIndex: Int,
        val paragraphIndex: Int
    )
}