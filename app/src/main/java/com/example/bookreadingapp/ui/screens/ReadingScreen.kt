package com.example.bookreadingapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.bookreadingapp.R
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.Table
import com.example.bookreadingapp.ui.BookReaderViewModel
import com.example.bookreadingapp.ui.components.ImageFromDatabase
import com.example.bookreadingapp.ui.components.NextAndPreviousChapterButtons
import com.example.bookreadingapp.ui.components.TableComposable
import kotlinx.coroutines.launch

@Composable
fun ChapterTitle(
    chapter : Chapter,
    modifier: Modifier = Modifier
){
    Text(
        text = chapter.chapterTitle,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall
    )
}

//implemented w/ only text for now
fun createPages(pageHeight: Dp, fontSize: Dp, chapterContents: List<String>): List<List<String>>{
    val chapterPages = chapterContents.chunked(25)
    return chapterPages
}

private fun findImageOnDevice(imagePathDB: String, directory: List<String>) : String?{
    directory.forEach {
        if (it.contains(imagePathDB.drop(7))){
            return it
        }
    }
    return null
}

@Composable
fun pages(chapterContents: Map<Int, Any>, bookReaderViewModel: BookReaderViewModel)
: List<Pair<Int, @Composable () -> Unit>> {
    val title = bookReaderViewModel.CurrentBook!!.title
    val bookDirectory = bookReaderViewModel.getDirectoryImages(title)

    val pages = mutableListOf<Pair<Int, @Composable ()-> Unit >>()
    chapterContents.forEach { (K, V) ->
        pages += when(V){
            is Heading -> Pair(K, {Text(V.headingData)})
            is Paragraph -> Pair(K, {Text(V.paragraphData)})
            is Image -> Pair(K, {ImageFromDatabase(findImageOnDevice(V.imageData, bookDirectory))})
            is Table -> Pair(K, {TableComposable().CreateTableComposable(tableString = V.tableData)})
            else -> Pair(K, {Text("")})
        }
    }
    return pages
}

fun chapterToMap(chapterID : Int){

}

@Composable
fun Page(composables: List<@Composable ()-> Unit>){
    Column{
        composables.forEach{ it() }
    }
}

@Composable
fun ReadingScreen(bookReaderViewModel: BookReaderViewModel){
    if (bookReaderViewModel.CurrentChapter != null) {
        //val pages = createPages(dimensionResource(id = R.dimen.page_height), dimensionResource(R.dimen.reading_p_font_size), chapter)
        val pages2 = pages(bookReaderViewModel.getContentsByChapterID(bookReaderViewModel.CurrentChapter!!.id), bookReaderViewModel)
        val lastPage = bookReaderViewModel.getPageIndex()
        val pageState = rememberPagerState(lastPage) { pages2.size }

        var currentChapterIndex by remember { mutableIntStateOf(bookReaderViewModel.CurrentChapter!!.chapterIndex) }

        val readingCoroutineScope = rememberCoroutineScope()

        val incrementCurrentChapter = {
            readingCoroutineScope.launch {
                pageState.scrollToPage(0)
            }
            currentChapterIndex += 1
        }
        val decrementCurrentChapter = {
            readingCoroutineScope.launch {
                pageState.scrollToPage(0)
            }
            currentChapterIndex -= 1
        }

        // source for Pager code: https://developer.android.com/develop/ui/compose/layouts/pager
        LaunchedEffect(pageState) {
            snapshotFlow { pageState.settledPage }.collect { page ->
                bookReaderViewModel.setPageIndex(page)
            }
        }
        key(currentChapterIndex){
            HorizontalPager(state = pageState) { index ->
                Column {
                    Column {
                        //invoke composable
                        pages2[index].second()
                    }

                    NextAndPreviousChapterButtons(
                        bookReaderViewModel = bookReaderViewModel,
                        currentChapter = currentChapterIndex,
                        incrementCurrentChapter = incrementCurrentChapter,
                        decrementCurrentChapter = decrementCurrentChapter
                    )
                }
            }
        }
    }
    else{
        Text(
            text= stringResource(R.string.no_book_selected_message),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
