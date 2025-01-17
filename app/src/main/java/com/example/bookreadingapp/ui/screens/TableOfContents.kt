package com.example.bookreadingapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookreadingapp.R
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.ui.BookReaderViewModel

@Composable
fun TableOfContentsHeader(
    modifier: Modifier = Modifier,
    book: Book,
){
    Column (
        modifier = modifier
            .testTag("TableTag"),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text= stringResource(R.string.table_of_contents_header),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text= book.title,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
fun TableOfContentsChapterList(
    modifier: Modifier = Modifier,
    book: Book,
    bookReaderViewModel : BookReaderViewModel = viewModel(),
    navigateToReading: () -> Unit
){
    val chapters: List<Chapter> = bookReaderViewModel.chapterRepository.getAllChaptersOfSpecificBook(book.id)

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        for (chapter in chapters) {
            Row ( modifier = modifier){
                Button(onClick = {
                    bookReaderViewModel.updateChapter(chapter.chapterIndex)
                    bookReaderViewModel.setPageIndex(0)
                    navigateToReading()
                }) {
                    Text(
                        text=chapter.chapterTitle
                    )
                }
            }
        }
    }

}

@Composable
fun TableOfContentsScreen(
    modifier: Modifier = Modifier,
    bookReaderViewModel : BookReaderViewModel = viewModel(),
    navigateToReading: () -> Unit
) {

    Column (
        modifier = modifier.padding(vertical=dimensionResource(R.dimen.library_screen_column_padding)),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        if (bookReaderViewModel.CurrentBook == null){
            Text(
                text= stringResource(R.string.no_book_selected_message),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        else{
            TableOfContentsHeader(modifier = modifier,
                book = bookReaderViewModel.CurrentBook!!
            )
            TableOfContentsChapterList(modifier = modifier,
                book = bookReaderViewModel.CurrentBook!!, navigateToReading = navigateToReading,
                bookReaderViewModel=bookReaderViewModel)
        }
    }
}