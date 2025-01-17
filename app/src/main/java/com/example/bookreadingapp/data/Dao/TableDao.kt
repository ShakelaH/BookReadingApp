package com.example.bookreadingapp.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookreadingapp.data.entities.Table
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(table: Table)

    @Update
    suspend fun update(table: Table)

    @Delete
    suspend fun delete(table: Table)

    @Query("SELECT * from tables WHERE table_id = :id")
    fun getTable(id: Int): Flow<Table>

    @Query("SELECT * from tables WHERE chapter_id = :id")
    fun getAllTables(id: Int): Flow<List<Table>>
}