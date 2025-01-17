package com.example.bookreadingapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tables",
    foreignKeys = [
        ForeignKey(
            entity = Chapter::class,
            parentColumns = arrayOf("chapter_id"),
            childColumns = arrayOf("chapter_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Table (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "table_id")
    val id: Int = 0,
    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,
    @ColumnInfo(name = "table_data")
    val tableData: String,
    @ColumnInfo(name = "table_index")
    val tableIndex: Int
)