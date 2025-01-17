package com.example.bookreadingapp.data

import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.Table
import com.example.bookreadingapp.ui.BookReaderViewModel

class HtmlToBook {

    private fun getStartingChapterId(bookReaderViewModel: BookReaderViewModel) : Int{
        var baseChapterId = 0
        try{
            baseChapterId = bookReaderViewModel.chapterRepository.getMostRecentChapter()
            baseChapterId += 1
        }
        catch (e: Exception){
            baseChapterId = 0;
        }
        return baseChapterId
    }

    suspend fun convert(bookPath: String, isbn: Int, bookReaderViewModel: BookReaderViewModel) {

        val parser : HtmlParser = HtmlParser()
        parser.HtmlParser(bookPath)

        var index = 0

        val baseChapterId: Int = getStartingChapterId(bookReaderViewModel)

        bookReaderViewModel.insertBookInDB(
            Book(
                id = isbn,
                title = parser.getTitle(),
                author = "",
                coverUrl = "")
        )

        parser.getChapters().forEach { chapter ->
            //Check to see if there are no chapters
            val chapterId = baseChapterId + index
            bookReaderViewModel.chapterRepository.insertChapter(
                Chapter(
                    id = chapterId,
                    isbn = isbn,
                    chapterTitle = parser.getChapterTitle(chapter),
                    chapterIndex = index
                )
            )
            index++
        }

        index = 0
        parser.getChapters().forEach{chapter ->
            var chapterElementIndex = 0
            val chapterId = baseChapterId + index

            parser.getChapterContents(chapter).forEach{
                if (it.first == "image"){
                    bookReaderViewModel.imageRepository.insertImage(
                        Image(
                            id = 0,
                            chapterId = chapterId,
                            imageData = it.second.filterNot { it == '{' || it == '}' },
                            imageIndex = chapterElementIndex
                        )
                    )
                }
                else if (it.first == "paragraph"){
                    bookReaderViewModel.paragraphRepository.insertParagraph(
                        Paragraph(
                            id = 0,
                            chapterId = chapterId,
                            paragraphData =  it.second,
                            paragraphIndex = chapterElementIndex
                        )
                    )
                }
                else if (it.first == "heading"){
                    bookReaderViewModel.headingRepository.insertHeading(
                        Heading(
                            id = 0,
                            chapterId = chapterId,
                            headingData =  it.second,
                            headingIndex = chapterElementIndex
                        )
                    )
                }
                else if (it.first == "table"){
                    bookReaderViewModel.tableRepository.insertTable(
                        Table(
                            id = 0,
                            chapterId = chapterId,
                            tableData =  it.second,
                            tableIndex = chapterElementIndex
                        )
                    )
                }
                chapterElementIndex++
            }
            index++
        }
    }
}