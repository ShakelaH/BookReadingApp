package com.example.bookreadingapp

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookreadingapp.data.FileRepository
import com.example.bookreadingapp.ui.BookReaderViewModel
import com.example.bookreadingapp.BottomNavigationBar
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.ui.ViewModelFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class BottomNavigationUiTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavController

    @Before
    fun setup() {
        val mockContext = mock(Context::class.java)
        val mockRepository = mock(FileRepository::class.java)

        val viewModel: BookReaderViewModel = ViewModelProvider(
            composeTestRule.activity, ViewModelFactory(composeTestRule.activity.application)
        )[BookReaderViewModel::class.java]

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            NavigationHost(navController = navController as NavHostController, viewModel = viewModel)
            BottomNavigationBar(navController = navController as NavHostController, viewModel = BookReaderViewModel(
                repository = mockRepository,
                context = mockContext
            ))
        }
    }


    @Test
    fun testBottomNavigationFromHomeToLibraryScreen() {
        composeTestRule.apply {
            onNodeWithTag("Library").performClick()


            waitForIdle()

            val route = navController.currentDestination?.route
            Assert.assertEquals( NavRoutes.Library.route, route )
        }
    }

    @Test
    fun testBottomNavigationFromHomeToReadingScreen() {
        composeTestRule.apply {
            onNodeWithTag("Reading").performClick()


            waitForIdle()

            val route = navController.currentDestination?.route
            Assert.assertEquals( NavRoutes.Reading.route, route )
        }
    }

    @Test
    fun testBottomNavigationFromHomeToSearchScreen() {
        composeTestRule.apply {
            onNodeWithTag("Search").performClick()


            waitForIdle()

            val route = navController.currentDestination?.route
            Assert.assertEquals( NavRoutes.Search.route, route )
        }
    }

    @Test
    fun testBottomNavigationFromHomeToTableOfContents() {
        composeTestRule.apply {
            onNodeWithTag("TableOfContents").performClick()


            waitForIdle()

            val route = navController.currentDestination?.route
            Assert.assertEquals( NavRoutes.TableOfContents.route, route )
        }
    }

    @Test
    fun testBottomNavigationHomeScreenWorks(){
        composeTestRule.apply{
            onNodeWithText("Getting Started").assertIsDisplayed().performClick()
        }
        composeTestRule.onNodeWithTag("HomeTag").assertExists().assertIsDisplayed()
    }
}