package com.example.bookreadingapp.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// bridging table
@Entity(
    tableName = "readings",
    foreignKeys =[
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("isbn"),
            childColumns = arrayOf("isbn"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Chapter::class,
            parentColumns = arrayOf("chapter_id"),
            childColumns = arrayOf("chapter_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reading (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "reading_id")
    val readingId: Int = 0,
    @ColumnInfo(name = "isbn")
    val isbn: Int,
    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,
    @ColumnInfo(name = "index")
    val index: Int,
)
