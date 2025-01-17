package com.example.bookreadingapp.ui.screens

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.core.app.ApplicationProvider
import com.example.bookreadingapp.MainActivity
import com.example.bookreadingapp.R
import com.example.bookreadingapp.data.FileRepository
import com.example.bookreadingapp.ui.BookReaderViewModel
import com.example.bookreadingapp.ui.ViewModelFactory
import com.example.bookreadingapp.ui.theme.BookReadingAppTheme
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class LibraryScreenKtTest{
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val mockContext: Context = mock(Context::class.java)
    private val mockRepository: FileRepository = mock(FileRepository::class.java)

    @Test
    fun libraryScreenTextTest() {
        composeTestRule.setContent {
            BookReadingAppTheme {
                val viewModel: BookReaderViewModel = viewModel()
                LibraryScreen(bookReaderViewModel = BookReaderViewModel(
                    mockRepository,
                    mockContext
                ), navigateToTableOfContents = { })
            }
        }
        composeTestRule.onNodeWithText(context.getString(R.string.library_continue_reading_header)).assertExists(
            "No node with this text was found."
        )
        composeTestRule.onNodeWithText(context.getString(R.string.library_catalog_header)).assertExists(
            "No node with this text was found."
        )
    }
    @Test
    fun libraryBookTest() {
        composeTestRule.setContent {
            BookReadingAppTheme {
                LibraryBook(title = "The Importance of Being Earnest", image = R.drawable.book_cover_placeholder, bookReaderViewModel = viewModel(), isbn = 1234)
            }
        }
        composeTestRule.onNodeWithText("The Importance of Being Earnest").assertExists(
            "No book element with this title was found."
        )
        composeTestRule.onNodeWithTag("BookCover").assertExists(
            "No node with this image was found"
        )
    }
    @Test
    fun continueReadingShelfTest() {
        val viewModel: BookReaderViewModel = ViewModelProvider(
            composeTestRule.activity, ViewModelFactory(composeTestRule.activity.application)
        )[BookReaderViewModel::class.java]

        composeTestRule.setContent {
            BookReadingAppTheme {
                val libraryBookArray = ArrayList<String>()
                libraryBookArray.add("Twelfth Night")
                libraryBookArray.add("The Scottish Play")
                libraryBookArray.add("A woman of no importance")
                ContinueReadingShelf(libraryBookArray = libraryBookArray, bookReaderViewModel = viewModel, {})
            }
        }
        composeTestRule.onNodeWithText("Twelfth Night").assertExists(
            "No book element with this title was found."
        )
        composeTestRule.onNodeWithText("The Scottish Play").assertExists(
            "No book element with this title was found."
        )
        composeTestRule.onNodeWithText("A woman of no importance").assertExists(
            "No book element with this title was found."
        )
    }
    @Test
    fun allLibraryBooksTest() {
        composeTestRule.setContent {
            val viewModel: BookReaderViewModel = ViewModelProvider(
                composeTestRule.activity, ViewModelFactory(composeTestRule.activity.application)
            )[BookReaderViewModel::class.java]

            val unDownloadedBookArray = arrayListOf(
                "Book 1", "Book 3", "Book 5"
            )
            val libraryCoroutineScope = rememberCoroutineScope()
            val downloadingBooksArray = arrayListOf<String>()
            val downloadingBooksArrayUpdate: (String, Boolean) -> Unit = { book, isDownloading ->
                if (isDownloading) {
                    downloadingBooksArray.add(book)
                } else {
                    downloadingBooksArray.remove(book)
                }
            }
            val directoryContents = listOf("file1", "file2", "file3")

            BookReadingAppTheme {
                val libraryBookArray = ArrayList<String>()
                libraryBookArray.add("Twelfth Night")
                libraryBookArray.add("The Scottish Play")
                libraryBookArray.add("A woman of no importance")
                AllLibraryBooks(
                    libraryBookArray = libraryBookArray,
                    bookReaderViewModel = viewModel,
                    directoryContents = directoryContents,
                    downloadingBooksArray = downloadingBooksArray,
                    downloadingBooksArrayUpdate = downloadingBooksArrayUpdate,
                    unDownloadedBookArray = unDownloadedBookArray,
                    libraryCoroutineScope = libraryCoroutineScope
                )
            }
        }
        composeTestRule.onNodeWithText("Twelfth Night").assertExists(
            "No book element with this title was found."
        )
        composeTestRule.onNodeWithText("The Scottish Play").assertExists(
            "No book element with this title was found."
        )
        composeTestRule.onNodeWithText("A woman of no importance").assertExists(
            "No book element with this title was found."
        )
    }
}