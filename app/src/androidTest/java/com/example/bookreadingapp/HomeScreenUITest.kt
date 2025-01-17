package com.example.bookreadingapp

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookreadingapp.ui.BookReaderViewModel
import com.example.bookreadingapp.ui.screens.HomeScreen
import com.example.bookreadingapp.ui.theme.BookReadingAppTheme
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setup(){
        composeTestRule.setContent {
            BookReadingAppTheme {
                val viewModel: BookReaderViewModel = viewModel()
                HomeScreen(bookReaderViewModel = viewModel)
            }
        }
    }

    @Test
    fun display_about_section() {
        composeTestRule.onNodeWithText(context.getString(R.string.about_us)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.about_us_text)).assertExists()
    }

    @Test
    fun display_getting_started_text() {
        composeTestRule.onNodeWithText(context.getString(R.string.getting_started)).assertExists()
    }

    @Test
    fun display_library_section() {
        composeTestRule.onNodeWithText(context.getString(R.string.library)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.library_body_text)).assertExists()
    }

    @Test
    fun display_reading_section() {
        composeTestRule.onNodeWithText(context.getString(R.string.reading)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.reading_body_text)).assertExists()
    }

    @Test
    fun display_search_section() {
        composeTestRule.onNodeWithText(context.getString(R.string.search)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.search_body_text)).assertExists()
    }

    @Test
    fun home_screen_has_correct_tag() {
        composeTestRule.onNodeWithTag("HomeTag").assertExists()
    }
}