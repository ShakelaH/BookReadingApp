package com.example.bookreadingapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookreadingapp.R
import com.example.bookreadingapp.ui.BookReaderViewModel

@Composable
fun HomeScreen(
    bookReaderViewModel: BookReaderViewModel, // might need later for images
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 10.dp)
            .padding(16.dp)
            .testTag("HomeTag")
    ) {
        AboutSection()
        Text(
            text = stringResource(R.string.getting_started),
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = modifier
                .padding(top = 10.dp)
        )
        ScreenDescriptionSection(
            header = stringResource(R.string.library),
            content = stringResource(R.string.library_body_text)
        )
        ScreenDescriptionSection(
            header = stringResource(R.string.reading),
            content = stringResource(R.string.reading_body_text)
        )
        ScreenDescriptionSection(
            header = stringResource(R.string.search),
            content = stringResource(R.string.search_body_text)
        )
    }
}

@Composable
fun AboutSection() {
    Column {
        Text(
            text = stringResource(R.string.about_us),
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider(
            color = Color.Blue,
            thickness = 1.dp
        )
        Text(
            text = stringResource(R.string.about_us_text),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ScreenDescriptionSection(header: String, content: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = header,
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.primary
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
