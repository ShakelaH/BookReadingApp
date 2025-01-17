package com.example.bookreadingapp.ui

import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.example.bookreadingapp.R

object NavBarItems {

    val BarItems = listOf(

        BarItem(
            title = "Home",
            image = R.drawable.baseline_home_24,
            route = "home",
        ),
        BarItem(
            title = "Library",
            // This image will change eventually, the actual image looks massive so this will be a
            // placeholder
            image = R.drawable.baseline_library_books_24,
            route = "library"
        ),
        BarItem(
            title = "Reading",
            image = R.drawable.baseline_chrome_reader_mode_24,
            route = "reading"
        ),
        BarItem(
            title = "Search",
            image = R.drawable.baseline_search_24,
            route = "search"
        ),
        BarItem(
            title = "TableOfContents",
            image = R.drawable.baseline_list_alt_24,
            route = "tableofcontents"
        )
    )
}