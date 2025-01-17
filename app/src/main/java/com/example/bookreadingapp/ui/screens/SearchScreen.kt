package com.example.bookreadingapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bookreadingapp.R
import com.example.bookreadingapp.ui.BookReaderViewModel

/**
 * A composable function that represents a search screen where users can search for paragraphs
 * within a book based on a searchs. It displays a list of snippets,
 * allows the user to click on any snippet to navigate to that chapter and paragraph, and
 * provides navigation to the reading screen
 *
 * @param bookReaderViewModel The view model that holds the state and logic for managing
 *        the search functionality and paragraph snippets
 * @param onSearch A callback function that gets called when a snippet is clicked.
 *        It takes two arguments: the chapter ID and paragraph index of the selected snippet.
 * @param navigateToLibrary A callback function that navigates to the library screen when clicked.
 */
@Composable
fun SearchScreen(
    bookReaderViewModel: BookReaderViewModel,
    modifier: Modifier = Modifier,
    onSearch: (Int, Int) -> Unit,
    navigateToLibrary: () -> Unit
) {
    // Collecting the paragraph snippets as state from the view model
    val paragraphSnippets by bookReaderViewModel.allParagraphsFromBook.collectAsState()
    Column(
        modifier = modifier
            .padding(bottom = 10.dp)
            .padding(16.dp)
            .testTag("SearchTag"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row containing the search input (TextField) and search button
        Row(
            modifier = modifier
                .padding(bottom = 10.dp)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            var text by remember { mutableStateOf("") }

            TextField(
                value = text,
                onValueChange = {  text = it },
                label = { Text(stringResource(R.string.search_text)) },
                shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 0.dp),
            )
            // Button to trigger the search functionality
            Button(
                onClick = {
                    bookReaderViewModel.updateSearchText(text)
                    bookReaderViewModel.allParagraphsFromCurrentlyReading()
                },
                modifier = Modifier
                    .height(56.dp)
                    .padding(start = 0.dp),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {
                Text(stringResource(R.string.search))
            }
        }
        // Displaying the last searched word
        Text(
            text = stringResource(R.string.last_searched_word) + bookReaderViewModel.searchText
        )
        // LazyColumn to display the list of search results (paragraph snippets)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp)
        ) {
            // Iterating through each paragraph snippet to display it in the list
            items(paragraphSnippets) { snippet ->
                // Button for each snippet, onClick triggers the search with chapterId and paragraphIndex
                Button(
                    onClick = { onSearch(snippet.chapterIndex, snippet.paragraphIndex) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // displaying snippet and chapter/paragraph details inside the button
                    Column {
                        // Showing the snippet text with ellipsis if it overflows
                        Text(
                            text = snippet.snippet,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        // showing chapter and paragraph index
                        Text(
                            text = "Chapter: ${snippet.chapterIndex}, Paragraph: ${snippet.paragraphIndex}",
                        )
                    }
                }
            }
        }

        Button(
            onClick = navigateToLibrary
        ) {
            Text(stringResource(R.string.go_to_library))
        }
    }
}