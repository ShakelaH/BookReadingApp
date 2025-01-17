package com.example.bookreadingapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookreadingapp.ui.BookReaderViewModel
import com.example.bookreadingapp.ui.NavBarItems
import com.example.bookreadingapp.ui.components.AdaptiveNavigationType
import com.example.bookreadingapp.ui.screens.HomeScreen
import com.example.bookreadingapp.ui.screens.LibraryScreen
import com.example.bookreadingapp.ui.screens.ReadingScreen
import com.example.bookreadingapp.ui.screens.SearchScreen
import com.example.bookreadingapp.ui.screens.TableOfContentsScreen
import kotlinx.coroutines.flow.MutableStateFlow


sealed class NavRoutes (val route: String) {
    data object Home : NavRoutes("home")
    data object Library : NavRoutes("library")
    data object Reading : NavRoutes("reading")
    data object Search : NavRoutes("search")
    data object TableOfContents : NavRoutes("tableofcontents")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookReaderAppBar(
    currentScreenTitle: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_lightmode),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(90.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    currentScreenTitle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
@ExperimentalMaterial3Api
fun BookReadingApp(windowSizeClass: WindowWidthSizeClass, viewModel: BookReaderViewModel, modifier: Modifier) {
    val navController = rememberNavController()
    val currentScreenTitle by viewModel.currentScreenTitle.collectAsState()
    val adaptiveNavigationType = when (windowSizeClass) {
        Compact -> AdaptiveNavigationType.BOTTOM_NAVIGATION
        Medium -> AdaptiveNavigationType.NAVIGATION_RAIL
        else -> AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER
    }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            viewModel.updateScreenTitle(currentRoute, context)
            if (currentRoute != NavRoutes.Reading.route && currentRoute != NavRoutes.TableOfContents.route) {
                BookReaderAppBar(currentScreenTitle = currentScreenTitle)
            }
        },
        bottomBar = {
            if (adaptiveNavigationType == AdaptiveNavigationType.BOTTOM_NAVIGATION && !viewModel.isToggled.collectAsState().value) {
                BottomNavigationBar(navController = navController, viewModel = viewModel)
            }
        }
    ) { innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            viewModel.updateScreenTitle(currentRoute, context)

            if (adaptiveNavigationType == AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER && !viewModel.isToggled.collectAsState().value) {
                PermanentNavigationDrawerComponent(
                    navController = navController,
                    onTitleChange = { title -> viewModel.updateScreenTitle(title, context) }, // Update title when navigation changes
                    viewModel = viewModel
                )
            }
            if (adaptiveNavigationType == AdaptiveNavigationType.NAVIGATION_RAIL && !viewModel.isToggled.collectAsState().value) {
                NavigationRailComponent(navController = navController, viewModel = viewModel)
            }
            if (currentRoute == NavRoutes.Reading.route ||
                currentRoute == NavRoutes.TableOfContents.route) {
                NavBarButtonToggle(isToggled = viewModel.isToggled) {
                    viewModel.isToggled.value = !viewModel.isToggled.value
                }
            }
            else{
                 viewModel.isToggled.value = false
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                NavigationHost(navController = navController, viewModel = viewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(navController: NavHostController, viewModel: BookReaderViewModel) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
    ) {
        composable(route = NavRoutes.Home.route) {
            HomeScreen(
                bookReaderViewModel = viewModel
            )
        }
        composable(route = NavRoutes.Library.route) {
            LibraryScreen(
                bookReaderViewModel = viewModel,
                navigateToTableOfContents = { navigateToTableOfContents(navController) }
            )
        }
        composable(route = NavRoutes.Reading.route) {
            ReadingScreen(bookReaderViewModel = viewModel)
        }
        composable(route = NavRoutes.Search.route) {
            SearchScreen(
                modifier = Modifier.testTag("SearchScreen"),
                bookReaderViewModel = viewModel,
                onSearch = { chapter, paragraph -> onSearch(chapter, paragraph, navController, viewModel)},
                navigateToLibrary = { navigateToLibrary(navController) }
            )
        }
        composable(route = NavRoutes.TableOfContents.route) {
            TableOfContentsScreen(
                bookReaderViewModel = viewModel,
                navigateToReading = { navigateToReading(navController) }
            )
        }
    }
}

private fun onSearch(
    chapterIndex: Int,
    paragraph: Int,
    navController: NavHostController,
    viewModel: BookReaderViewModel
){
    viewModel.updateChapter(chapterIndex)
    viewModel.updateParagraph(paragraph)
    viewModel.setPageIndex(paragraph)

    Log.d("DEBUG_TAG", "Chapter: $chapterIndex, Paragraph: $paragraph")
    navController.navigate(NavRoutes.Reading.route)
}

private fun navigateToLibrary(
    navController: NavHostController
){
    navController.navigate(NavRoutes.Library.route)
}
private fun navigateToTableOfContents(
    navController: NavHostController
){
    navController.navigate(NavRoutes.TableOfContents.route)
}

private fun navigateToReading(
    navController: NavHostController
){
    navController.navigate(NavRoutes.Reading.route)
}

fun getNavItemIndex(title:String) : Int {
    return when (title) {
        "home" -> 0
        "library" -> 1
        "reading" -> 2
        "search" -> 3
        "tableofcontents" -> 4
        else -> 0
    }
}

// Bottom Navigation bar will be used for the "compact" screen (small)
@Composable
fun BottomNavigationBar(navController: NavController, viewModel: BookReaderViewModel) {
    val navTitlesArray = stringArrayResource(R.array.Routes)
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoutes = backStackEntry?.destination?.route
        NavBarItems.BarItems.forEach{ navItem ->
            NavigationBarItem(
                selected = currentRoutes == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(navItem.image),
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navTitlesArray[getNavItemIndex(navItem.route)])
                },
                modifier = Modifier.testTag(navItem.title)
            )
        }
    }
}

// Rail Navigation bar will be used for the "medium" screen
@Composable
fun NavigationRailComponent(navController: NavController, viewModel: BookReaderViewModel) {
    NavigationRail {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoutes = backStackEntry?.destination?.route
        NavBarItems.BarItems.forEach{ navItem ->
            NavigationRailItem(
                selected = currentRoutes == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(navItem.image),
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                },
                modifier = Modifier.testTag(navItem.title)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentNavigationDrawerComponent(navController: NavHostController, onTitleChange: (String) -> Unit, viewModel: BookReaderViewModel) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoutes = backStackEntry?.destination?.route

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet {
                Column {
                    Spacer(Modifier.height(12.dp))
                    NavBarItems.BarItems.forEach { navItem ->
                        NavigationDrawerItem(
                            selected = currentRoutes == navItem.route,
                            onClick = {
                                navController.navigate(navItem.route) {
                                    onTitleChange(navItem.title)
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(navItem.image),
                                    contentDescription = navItem.title
                                )
                            },
                            label = { Text(text = navItem.title) },
                            modifier = Modifier.testTag(navItem.title)
                        )
                    }
                }
            }
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                NavigationHost(navController = navController, viewModel = viewModel)
            }
        }
    )
}

@Composable
fun NavBarButtonToggle(isToggled: MutableStateFlow<Boolean>, onToggle: () -> Unit) {
    val navBarToggled by isToggled.collectAsState()

    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {onToggle()}) {
            Icon(
                imageVector = if (navBarToggled) Icons.Default.Check else Icons.Default.Clear,
                contentDescription = (if (navBarToggled) stringResource(R.string.hide_bar) else stringResource(R.string.show_bar))
            )
        }
    }
}