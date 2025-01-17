package com.example.bookreadingapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bookreadingapp.ui.BookReaderViewModel

@Composable
private fun NextChapterButton(
    bookReaderViewModel: BookReaderViewModel,
    currentChapter: Int,
    incrementCurrentChapter: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(onClick = {
        bookReaderViewModel.updateChapter(currentChapter + 1)
        incrementCurrentChapter()
        bookReaderViewModel.setPageIndex(0)
    }) {
        Text(text = "→")
    }
}
@Composable
private fun PrevChapterButton(
    bookReaderViewModel : BookReaderViewModel,
    currentChapter: Int,
    decrementCurrentChapter: () -> Unit,
    modifier : Modifier = Modifier
){
    Button(onClick = {
        bookReaderViewModel.updateChapter(currentChapter - 1)
        decrementCurrentChapter()
        bookReaderViewModel.setPageIndex(0)
    }) {
        Text(text = "←")
    }
}

@Composable
fun NextAndPreviousChapterButtons(
    bookReaderViewModel : BookReaderViewModel,
    currentChapter: Int,
    incrementCurrentChapter: () -> Unit,
    decrementCurrentChapter: () -> Unit,
    modifier : Modifier = Modifier
){
    if (bookReaderViewModel.CurrentBook != null && bookReaderViewModel.CurrentChapter != null) {

        val maxChapter: Int = bookReaderViewModel.chapterRepository.getAllChaptersOfSpecificBook(
            bookReaderViewModel.CurrentBook!!.id
        ).maxBy { chapter -> chapter.chapterIndex }.chapterIndex

        Row(modifier = modifier) {
            if (currentChapter > 0) {
                PrevChapterButton(
                    bookReaderViewModel = bookReaderViewModel,
                    currentChapter = currentChapter,
                    decrementCurrentChapter = decrementCurrentChapter,
                    modifier = modifier
                )
            }
            if (currentChapter < maxChapter) {
                NextChapterButton(
                    bookReaderViewModel = bookReaderViewModel,
                    currentChapter = currentChapter,
                    incrementCurrentChapter = incrementCurrentChapter,
                    modifier = modifier
                )
            }
        }
    }
}