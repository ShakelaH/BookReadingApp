package com.example.bookreadingapp.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookreadingapp.data.entities.Heading
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(heading: Heading)

    @Update
    suspend fun update(heading: Heading)

    @Delete
    suspend fun delete(heading: Heading)

    @Query("SELECT * from headings WHERE heading_id = :id")
    fun getHeading(id: Int): Flow<Heading>

    @Query("SELECT * FROM headings WHERE chapter_id = :id GROUP BY chapter_id ORDER BY heading_index ASC")
    fun getAllHeadings(id: Int): Flow<List<Heading>>
}