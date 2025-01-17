package com.example.bookreadingapp.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "paragraphs",
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
data class Paragraph (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "paragraph_id")
    val id: Int = 0,
    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,
    @ColumnInfo(name = "paragraph_data")
    val paragraphData: String,
    @ColumnInfo(name = "paragraph_index")
    val paragraphIndex: Int
)
