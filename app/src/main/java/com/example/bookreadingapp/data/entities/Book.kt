package com.example.bookreadingapp.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a book
 *
 * @property title The title of the book
 * @property image A reference to the book's cover image
 * @property chapters A list of chapters in the book
 */
@Entity(tableName = "books")
data class Book (
    @PrimaryKey
    @ColumnInfo(name = "isbn")
    val id: Int,
    @ColumnInfo(name = "cover_url")
    val coverUrl: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String? = null,
    @ColumnInfo(name = "publisher")
    val publisher: String? = null,
)
