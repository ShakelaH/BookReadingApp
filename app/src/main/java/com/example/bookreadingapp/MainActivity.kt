package com.example.bookreadingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookreadingapp.data.entities.Reading
import com.example.bookreadingapp.ui.BookReaderViewModel
import com.example.bookreadingapp.ui.ViewModelFactory
import com.example.bookreadingapp.ui.theme.BookReadingAppTheme
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass as calculateWindowSizeClass1

class MainActivity : ComponentActivity() {
    private val viewModel: BookReaderViewModel by viewModels {
        ViewModelFactory(this.applicationContext) // Use application context to prevent memory leaks
    }


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookReadingAppTheme {
                Surface(tonalElevation = 5.dp){
                    val windowSize = calculateWindowSizeClass1(this)
                    Scaffold(
                        modifier = Modifier.fillMaxSize()) { innerPadding ->
                        BookReadingApp(
                            windowSizeClass = windowSize.widthSizeClass,
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val currentBookID = viewModel.CurrentBook?.id
        val currentChapter = viewModel.getChapter()?.id
        val currentIndex = viewModel.getPageIndex()
        if (currentBookID != null && currentChapter !=null) {
            val reading = Reading(isbn =currentBookID, chapterId = currentChapter, index = currentIndex)
            viewModel.readingRepository.insertReading(reading)
        }

    }
}

