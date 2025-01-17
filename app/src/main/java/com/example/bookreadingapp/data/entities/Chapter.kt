package com.example.bookreadingapp.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.bookreadingapp.data.Dao.ParagraphDao

/**
 * Data class representing a chapter in a book
 *
 * @property chapterTitle The title of the chapter
 * @property paragraphs A list of paragraphs that make up the chapter content.
 */
@Entity(
    tableName = "chapters",
    foreignKeys =[
        ForeignKey(
            entity = Book::class,
            parentColumns = arrayOf("isbn"),
            childColumns = arrayOf("isbn"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Chapter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "chapter_id")
    val id: Int,
    @ColumnInfo(name = "isbn")
    val isbn: Int,
    @ColumnInfo(name = "chapter_title")
    val chapterTitle: String,
    @ColumnInfo(name = "chapter_index")
    val chapterIndex: Int
) {
    override fun equals(other: Any?): Boolean {
        if (other is Chapter){
            //Assuming that each chapter has a unique title
            val returnValue = this.chapterTitle == other.chapterTitle
            return returnValue
        }
        return false
    }

    /**
     * Generates a hash code for the chapter
     */
//    override fun hashCode(): Int {
//        var result = chapterTitle.hashCode()
//        result = 31 * result + paragraphs.hashCode()
//        return result
//    }
}