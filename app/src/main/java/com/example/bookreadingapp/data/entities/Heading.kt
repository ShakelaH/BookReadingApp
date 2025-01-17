package com.example.bookreadingapp.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "headings",
    foreignKeys =[
        ForeignKey(
            entity = Chapter::class,
            parentColumns = arrayOf("chapter_id"),
            childColumns = arrayOf("chapter_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Heading(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "heading_id")
    val id: Int,
    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,
    @ColumnInfo(name = "heading_data")
    val headingData: String,
    @ColumnInfo(name = "heading_index")
    val headingIndex: Int
)