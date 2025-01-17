package com.example.bookreadingapp.ui.screens

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookreadingapp.R
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.ui.BookReaderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LibraryBook(
    title: String,
    image: Int,
    isbn: Int,
    modifier: Modifier = Modifier,
    downloadUrl: String? = null,
    bookReaderViewModel: BookReaderViewModel,
    libraryCoroutineScope: CoroutineScope? = null,
    downloadingBooksArray: ArrayList<String>? = null,
    downloadingBooksArrayUpdate: ((String, Boolean) -> Unit)? = null,
    directoryContents: List<String>? = null,
    navigateToTableOfContents: (() -> Unit)? = null,
){
    //Checks that all the fields required for downloading aren't null
    val isDownloadable : Boolean = downloadUrl != null &&
            libraryCoroutineScope != null &&
            downloadingBooksArray != null &&
            downloadingBooksArrayUpdate != null &&
            directoryContents != null &&
            !downloadingBooksArray.contains(title)

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(dimensionResource(R.dimen.library_book_image_width))
            .padding(bottom = 20.dp)
    ){
        Box(
            modifier = modifier
        ){
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = modifier
                    .padding(dimensionResource(R.dimen.library_book_image_padding))
                    .size(
                        width = dimensionResource(R.dimen.library_book_image_width),
                        height = dimensionResource(R.dimen.library_book_image_height)
                    )
                    .testTag("BookCover")
                    .clickable(onClick = {
                        if (!isDownloadable){
                            bookReaderViewModel.updateBook( Book(isbn, image.toString(), title) )
                            navigateToTableOfContents?.invoke()
                        }
                    })
            )
            //If downloadable, then include the download button.
            //The nullable fields below are asserted not null due to the nature of
            //the isDownloadable boolean initialization
            if (isDownloadable){
                Button(
                    onClick = {
                        libraryCoroutineScope!!.launch(Dispatchers.IO) {
                            downloadingBooksArrayUpdate?.invoke(title, true)
                            bookReaderViewModel.setupDownload(downloadUrl!!, title, isbn)
                            downloadingBooksArrayUpdate?.invoke(title, false)
                        }
                    },
                    modifier = modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(R.string.download, "\""+title+"\""),
                        textAlign = TextAlign.Center)
                }
            }
        }
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
    }
}

/**
 * Returns an array of books that represents the books which are downloaded
 * @param libraryBookArray ArrayList<String> starting list of books to filter from
 * @param directoryContents List<String> Signet book files downloaded on the device
 */
fun filterOutDownloadedBooks(
    libraryBookArray: ArrayList<String>,
    directoryContents: List<String>
): ArrayList<String>
{
    val filteredLibraryBookArray = ArrayList<String>()
    libraryBookArray.forEach { libraryBook ->
        directoryContents.forEach { downloadedBook ->
            if (downloadedBook.contains(libraryBook) && downloadedBook.contains("html")){
                filteredLibraryBookArray.add(libraryBook)
            }
        }
    }

    return filteredLibraryBookArray
}

/**
 * Returns an array of isbn which matches indexes for an array of chosen books
 * @param originalISBNArray IntArray isbn numbers of downloadable signet books
 * @param libraryBookArray ArrayList<String> Starting list of books to filter from
 * @param chosenBookArray List<String> Signet book files downloaded on the device
 */
fun computeISBNsForBooks(originalISBNArray: IntArray, libraryBookArray: ArrayList<String>, chosenBookArray: ArrayList<String>)
: ArrayList<Int>
{
    var index = 0
    val displayBookISBNs = ArrayList<Int>()

    libraryBookArray.forEach{
        if (chosenBookArray.contains(it)){
            displayBookISBNs.add(originalISBNArray[index])
        }
        index++
    }

    return displayBookISBNs
}

/**
 * Returns an array of isbn which matches indexes for an array of chosen books
 * @param originalImagesArray IntArray image covers of signet books
 * @param libraryBookArray ArrayList<String> Starting list of books to filter from
 * @param chosenBookArray List<String> Signet book files downloaded on the device
 */
fun computeImagesForBooks(originalImagesArray: IntArray, libraryBookArray: ArrayList<String>, chosenBookArray: ArrayList<String>)
        : ArrayList<Int>
{
    var index = 0
    val displayBookImages = ArrayList<Int>()

    libraryBookArray.forEach{
        if (chosenBookArray.contains(it)){
            displayBookImages.add(originalImagesArray[index])
        }
        index++
    }

    return displayBookImages
}

@Composable
fun ContinueReadingShelf(
    libraryBookArray : ArrayList<String>,
    bookReaderViewModel: BookReaderViewModel,
    imagesArray: IntArray,
    navigateToTableOfContents: () -> Unit,
    modifier : Modifier = Modifier
){
    val continueReadingISBNs = computeISBNsForBooks(
        originalISBNArray = LocalContext.current.resources.getIntArray(R.array.BookISBNs),
        libraryBookArray = LocalContext.current.resources.getStringArray(R.array.BookTitles).toCollection(ArrayList()),
        chosenBookArray = libraryBookArray
    )
    val continueReadingImages = computeImagesForBooks(
        originalImagesArray = imagesArray,
        libraryBookArray = LocalContext.current.resources.getStringArray(R.array.BookTitles).toCollection(ArrayList()),
        chosenBookArray = libraryBookArray
    )
    Text(
        text=stringResource(R.string.library_continue_reading_header),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineSmall,
    )
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(
                start = dimensionResource(R.dimen.library_continue_reading_padding_start),
                end = dimensionResource(R.dimen.library_continue_reading_padding_end),
                bottom = dimensionResource(R.dimen.library_continue_reading_padding_bottom)
            )
    ){
        for ( i in 0 .. libraryBookArray.size-1){
            Row ( modifier = modifier){
                LibraryBook(
                    title = libraryBookArray[i],
                    image = continueReadingImages[i],
                    isbn = continueReadingISBNs[i],
                    bookReaderViewModel = bookReaderViewModel,
                    navigateToTableOfContents = navigateToTableOfContents,
                )
            }
        }
    }
}

@Composable
fun AllLibraryBooks(
    libraryBookArray: ArrayList<String>,
    imagesArray: IntArray,
    unDownloadedBookArray: ArrayList<String>,
    bookReaderViewModel: BookReaderViewModel,
    libraryCoroutineScope: CoroutineScope,
    downloadingBooksArray: ArrayList<String>,
    downloadingBooksArrayUpdate: (String, Boolean) -> Unit,
    directoryContents: List<String>,
    modifier: Modifier = Modifier
){
    //calculations to obtain width of screen
    val a = Resources.getSystem().getDisplayMetrics().widthPixels
    val dp : Int = (a/Resources.getSystem().getDisplayMetrics().density).toInt()
    val stepSize = dp/200

    //Retrieve book urls & isbn
    val context = LocalContext.current
    val bookUrls = context.resources.getStringArray(R.array.BookUrls)
    val isbnArray = context.resources.getIntArray(R.array.BookISBNs)

    var index = 0
    val displayBooks = ArrayList<String>()
    val displayBookISBNs = ArrayList<Int>()
    val displayBookUrls = ArrayList<String>()
    val displayImagesArray = ArrayList<Int>()

    libraryBookArray.forEach{
        if (unDownloadedBookArray.contains(it)){
            displayBooks.add(it)
            displayBookISBNs.add(isbnArray[index])
            displayBookUrls.add(bookUrls[index])
            displayImagesArray.add(imagesArray[index])
        }
        index++
    }

    Text(
        text=stringResource(R.string.library_catalog_header),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineSmall,
    )
    Column (
        modifier = modifier
    ){
        for ( i in 0 .. displayBooks.size-1 step stepSize){
            Row ( modifier = modifier){
                for (j in 0 .. stepSize-1)
                {
                    if (i+j < displayBooks.size){
                        LibraryBook(
                            title = displayBooks[i+j],
                            image = displayImagesArray[i+j],
                            isbn = displayBookISBNs[i+j],
                            downloadUrl = displayBookUrls[i+j],
                            bookReaderViewModel =bookReaderViewModel,
                            libraryCoroutineScope =libraryCoroutineScope,
                            downloadingBooksArray =downloadingBooksArray,
                            downloadingBooksArrayUpdate =downloadingBooksArrayUpdate,
                            directoryContents = directoryContents)
                    }
                }
            }
        }
    }
}

@Composable
fun BookDeleteButton(bookReaderViewModel: BookReaderViewModel, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),  // Replace dimensionResource with hardcoded value if undefined
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            bookReaderViewModel.confirmDeletionAll()
            bookReaderViewModel.updateDirectoryContents()
            bookReaderViewModel.bookRepository.deleteAllBooks()
        }) {
            Text(stringResource(R.string.delete_all_directories))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun LibraryScreen(
    modifier : Modifier = Modifier,
    bookReaderViewModel : BookReaderViewModel = viewModel(),
    navigateToTableOfContents: () -> Unit
) {
    val context = LocalContext.current
    val libraryBookArray = context.resources.getStringArray(R.array.BookTitles).toCollection(ArrayList())
    val directoryContents by bookReaderViewModel.directoryContents.observeAsState(emptyList())
    bookReaderViewModel.updateDirectoryContents()
    val filteredLibraryBookArray = filterOutDownloadedBooks(libraryBookArray, directoryContents)
    val unDownloadedBookArray = (libraryBookArray - filteredLibraryBookArray.toSet()) as ArrayList<String>

    val libraryCoroutineScope = rememberCoroutineScope()
    var downloadingBooksArray: ArrayList<String> by rememberSaveable { mutableStateOf(ArrayList()) }
    val downloadingBooksArrayUpdate = {  book: String, addBook: Boolean ->
        val newarray: ArrayList<String> = ArrayList()
        downloadingBooksArray.forEach{
            newarray.add(it)
        }
        if (addBook){ newarray.add(book) }
        else{ newarray.remove(book) }
        downloadingBooksArray = newarray
    }

    val imagesArray = intArrayOf(
        R.drawable.pg236_cover_medium,
        R.drawable.pg135_cover_medium,
        R.drawable.pg108_cover_medium,
        R.drawable.pg164_cover_medium,
        R.drawable.pg28657_cover_medium,
        R.drawable.pg834_cover_medium
    )

    Column (
        modifier = modifier
            .padding(vertical=dimensionResource(R.dimen.library_screen_column_padding)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        ContinueReadingShelf(
            libraryBookArray=filteredLibraryBookArray,
            bookReaderViewModel=bookReaderViewModel,
            imagesArray = imagesArray,
            navigateToTableOfContents=navigateToTableOfContents,
            modifier = modifier)

        AllLibraryBooks(
            libraryBookArray = libraryBookArray,
            imagesArray= imagesArray,
            unDownloadedBookArray = unDownloadedBookArray,
            bookReaderViewModel = bookReaderViewModel,
            libraryCoroutineScope = libraryCoroutineScope,
            downloadingBooksArray = downloadingBooksArray,
            downloadingBooksArrayUpdate = downloadingBooksArrayUpdate,
            directoryContents = directoryContents,
            modifier = modifier)

        BookDeleteButton(bookReaderViewModel=bookReaderViewModel,
            modifier = modifier)
    }
}

//@Preview(showBackground = true, widthDp=412, heightDp = 900)
//@Composable
//fun LibraryPreview() {
//    LibraryScreen()
//}
//@Preview(showBackground = true, widthDp=812, heightDp = 900)
//@Composable
//fun LibraryPreviewWide() {
//    LibraryScreen()
//}