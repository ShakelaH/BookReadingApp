package com.example.bookreadingapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreadingapp.NavRoutes
import com.example.bookreadingapp.R
import com.example.bookreadingapp.data.Dao.ParagraphDao
import com.example.bookreadingapp.data.FileRepository
import com.example.bookreadingapp.data.HtmlToBook
import com.example.bookreadingapp.data.LibraryDatabase
import com.example.bookreadingapp.data.Repositories.BookRepository
import com.example.bookreadingapp.data.Repositories.ChaptersRepository
import com.example.bookreadingapp.data.Repositories.HeadingsRepository
import com.example.bookreadingapp.data.Repositories.ImagesRepository
import com.example.bookreadingapp.data.Repositories.ParagraphRepository
import com.example.bookreadingapp.data.Repositories.ReadingRepository
import com.example.bookreadingapp.data.Repositories.TableRepository
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.data.entities.Chapter
import com.example.bookreadingapp.data.entities.Heading
import com.example.bookreadingapp.data.entities.Image
import com.example.bookreadingapp.data.entities.Paragraph
import com.example.bookreadingapp.data.entities.Table
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * ViewModel class for managing the state and data flow of the Book Reader application
 *
 * @param repository An instance of FileRepository for handling file operations.
 */
@SuppressLint("StaticFieldLeak")
class BookReaderViewModel(private val repository: FileRepository, context: Context) : ViewModel() {
    val bookRepository: BookRepository
    val chapterRepository: ChaptersRepository
    val headingRepository: HeadingsRepository
    val readingRepository: ReadingRepository
    val imageRepository: ImagesRepository
    val paragraphRepository: ParagraphRepository
    val tableRepository: TableRepository

    init {
        val bookAppDb = LibraryDatabase.getDatabase(context)
        val booksDao = bookAppDb.BookDao()
        bookRepository = BookRepository(booksDao)
        val tableDao = bookAppDb.TableDao()
        tableRepository = TableRepository(tableDao)
        val chapterDao = bookAppDb.ChapterDao()
        chapterRepository = ChaptersRepository(chapterDao)
        val headingDao = bookAppDb.HeadingDao()
        headingRepository = HeadingsRepository(headingDao)
        val readingDao = bookAppDb.ReadingDao()
        readingRepository = ReadingRepository(readingDao)
        val paragraphDao = bookAppDb.ParagraphDao()
        paragraphRepository = ParagraphRepository(paragraphDao)
        val imageDao = bookAppDb.ImageDao()
        imageRepository = ImagesRepository(imageDao)
    }

    // StateFlow for managing the current screen title
    private val _currentScreenTitle = MutableStateFlow("")
    val currentScreenTitle: StateFlow<String> get() = _currentScreenTitle

    // Mutable state for search text
    private var _searchText by mutableStateOf("")
    val searchText: String get() = _searchText
    // stateFlow for toggling UI components
    var isToggled = MutableStateFlow(false)

    // Current book and chapter being read.
    private var _book : Book? = null
    private var _chapter: Chapter? = null
    val CurrentChapter: Chapter? get() = _chapter  // Pera use this - Shakela
    private var _paragraphIndex : Int? = null // Pera use this - Shakela
    val ParagraphIndex: Int? get() = _paragraphIndex
    private lateinit var _currentPages : List<List<String>>
    private var _currentPageIndex = 0
    val CurrentBook: Book? get() = _book
    // liveData for listing the contents of a directory
    private val _directoryContents = MutableLiveData<List<String>>()
    val directoryContents: LiveData<List<String>> = _directoryContents
    private val _allParagraphsFromBook =  MutableStateFlow<List<ParagraphDao.ParagraphSearchResult>>(emptyList())
    val allParagraphsFromBook: StateFlow<List<ParagraphDao.ParagraphSearchResult>> = _allParagraphsFromBook

    private val paragraphs = MutableStateFlow<List<Paragraph>>(emptyList())
    private val images =  MutableStateFlow<List<Image>>(emptyList())
    private val tables =  MutableStateFlow<List<Table>>(emptyList())
    private val headings =  MutableStateFlow<List<Heading>>(emptyList())

    /**
     * Updates the search text used in the UI
     *
     * @param newText The new search text
     */
    fun updateSearchText(newText: String) {
        _searchText = newText
    }

    fun allParagraphsFromCurrentlyReading() {
        val isbn = _book?.id
        if (isbn != null) {
            viewModelScope.launch {
                paragraphRepository.getAllParagraphsFromBook(isbn, _searchText).collect { searchResults ->
                    _allParagraphsFromBook.value = searchResults
                }
            }
        } else {
            _allParagraphsFromBook.value = emptyList()
        }
    }

    /**
     * Update the current book being read
     *
     * @param currentBook The selected book
     */
    fun updateBook(currentBook: Book){
        _book = currentBook
    }

    fun getChapter() : Chapter? {
        return _chapter
    }

    /**
     * Update the current chapter being read
     *
     * @param currentChapter The selected chapter
     */
    fun updateChapter(currentChapter: Int){
        val isbn = _book!!.id
        viewModelScope.launch {
            chapterRepository.getCurrentChapter(currentChapter, isbn).collect {
                _chapter = it
            }
        }
    }

    fun updateParagraph(currentParagraphIndex: Int) {
        _paragraphIndex = currentParagraphIndex
    }

    fun setPages(pages: List<List<String>>){
        _currentPages = pages
    }

    fun setPageIndex(index: Int){
        _currentPageIndex = index
    }
    fun getPageIndex(): Int {
        return _currentPageIndex
    }

    /**
     * Updates the screen title based on the current navigation route
     *
     * @param currentRoute The current route
     */
    fun updateScreenTitle(currentRoute: String?, context: Context) {
        _currentScreenTitle.value = when (currentRoute) {
            NavRoutes.Home.route -> context.resources.getTextArray(R.array.Routes)[0].toString()
            NavRoutes.Library.route -> context.resources.getTextArray(R.array.Routes)[1].toString()
            NavRoutes.Search.route -> context.resources.getTextArray(R.array.Routes)[3].toString()
            else -> ""
        }
    }

    // Source: Carlton Davis [Link](https://gitlab.com/crdavis/networkandfileio/-/blob/master/app/src/main/java/com/example/networkandfileio/ui/DownloadViewModel.kt?ref_type=heads)
    // Function to set up file download
    // Source for Join(): [Link](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-job/join.html)
    /**
     * Sets up a file download and extracts its contents if successful
     *
     * @param url The URL of the file to download
     */
    fun setupDownload(url: String, bookTitle: String, isbn: Int) {
        val fileName = url.substringAfterLast("/")
        val file = repository.createFile("DownloadedFiles", fileName)
        runBlocking {
            viewModelScope.launch(Dispatchers.IO) {
                if (repository.downloadFile(url, file)) {
                    updateDirectoryContents("DownloadedFiles")
                } else {
                    Log.e("DownloadViewModel", "Failed to download file")
                }
            }.join()
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.unzipBook(bookTitle, file)) {
                updateDirectoryContents(bookTitle)
                sendBooksToDB(title = bookTitle,
                    isbn = isbn)
            } else {
                Log.e("DownloadViewModel", "Failed to unzip")
            }
        }
    }


    private suspend fun sendBooksToDB(
        title: String,
        isbn: Int
    ){
        repository.listDirectoryContents(title).forEach { downloadedBook ->
            if (downloadedBook.contains(title) && downloadedBook.contains("html")){
                HtmlToBook().convert(downloadedBook, isbn, this)
            }
        }
    }


    /**
     * Updates the LiveData with the contents of a directory
     *
     * @param directoryName The name of the directory
     */
    private fun updateDirectoryContents(directoryName: String) {
        val contents = repository.listDirectoryContents(directoryName)
        _directoryContents.postValue(contents)
//        _directoryContents.postValue((_directoryContents.value ?: emptyList()) + contents)
    }

    fun updateDirectoryContents(){
        var newContent = ArrayList<String>()
        repository.listDownloadDirectoryContents().forEach { directory ->
            newContent = (newContent + repository.listDirectoryContents(directory)) as ArrayList<String>
        }
        _directoryContents.postValue(newContent)
    }

    fun getBookDirectoryContents(title: String) : List<String>{
        var newContent = ArrayList<String>()
        repository.listDirectoryContents(title).forEach { directory ->
            newContent = (newContent + repository.listDirectoryContents(directory)) as ArrayList<String>
        }
        return newContent
    }

    fun getDirectoryImages(title: String) : List<String>{
        return repository.listDirectoryContents(title+"/images")
    }

    /**
     * Deletes the contents of a directory and updates the UI
     * @param directoryName The name of the directory
     */
    fun confirmDeletion(directoryName: String) {
        repository.deleteDirectoryContents(directoryName)
        updateDirectoryContents(directoryName)
    }

    fun insertBookInDB(book: Book) {
        bookRepository.insertBook(book)
    }

    /**
     * Deletes the contents of all directories and updates the UI
     */
    fun confirmDeletionAll() {
        repository.listDownloadDirectoryContents().forEach{
            confirmDeletion(it)
        }
    }

    fun getAllBooksFromDb(): Flow<List<Book>> {
        val listOfAllBooks = bookRepository.allBooks
        return listOfAllBooks
    }

    @SuppressLint("SuspiciousIndentation")
    fun getContentsByChapterID(ID: Int): Map<Int,Any>{
        val contentMap: MutableMap<Int, Any> = mutableMapOf<Int, Any>()


        viewModelScope.launch {
            headingRepository.getAllHeadingsByChapter(ID).collect{ hCollector->
                headings.value = hCollector
            }
        }
        viewModelScope.launch {
            paragraphRepository.getAllParagraphsByChapter(ID).collect { pCollector ->
                paragraphs.value = pCollector
            }
        }
        viewModelScope.launch {
            imageRepository.getAllImagesOfSpecificChapter(ID).collect { iCollector ->
                images.value = iCollector
            }
        }
        viewModelScope.launch {
            tableRepository.getAllTablesOfSpecificChapter(ID).collect{ tCollector->
                tables.value = tCollector
            }
        }




        contentMap += paragraphs.value.associateBy { it.paragraphIndex }
        contentMap += headings.value.associateBy { it.headingIndex }
        contentMap += images.value.associateBy { it.imageIndex }
        contentMap += tables.value.associateBy { it.tableIndex }

        //source : https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/to-sorted-map.html
        val sortedContentMap = contentMap.toSortedMap(comparator = compareBy<Int>{it})

        return sortedContentMap
    }

}