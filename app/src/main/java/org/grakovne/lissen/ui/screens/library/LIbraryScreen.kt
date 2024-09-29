package org.grakovne.lissen.ui.screens.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dagger.hilt.android.EntryPointAccessors
import org.grakovne.lissen.domain.RecentBook
import org.grakovne.lissen.ui.components.ImageLoaderEntryPoint
import org.grakovne.lissen.ui.screens.library.composables.LibraryComposable
import org.grakovne.lissen.ui.screens.library.composables.MiniPlayerComposable
import org.grakovne.lissen.ui.screens.library.composables.RecentBooksComposable
import org.grakovne.lissen.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(
    navController: NavController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()

    val books by viewModel.books.observeAsState(emptyList())
    val recentBooks: List<RecentBook> by viewModel.recentBooks.observeAsState(emptyList())

    val context = LocalContext.current
    val imageLoader = remember {
        EntryPointAccessors.fromApplication(context, ImageLoaderEntryPoint::class.java)
            .getImageLoader()
    }

    val showAppBarTitle by remember {
        derivedStateOf {
            val libraryItemIndex = 2
            !listState.layoutInfo.visibleItemsInfo.any { it.index == libraryItemIndex }
        }
    }

    Scaffold(
        topBar = {
            if (showAppBarTitle) {
                Text(
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    text = "Library",
                    modifier = Modifier
                        .padding(16.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
        },
        bottomBar = {
            MiniPlayerComposable(navController)
        },
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        content = { innerPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(key = "continue_listening_title") {
                    Text(
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        text = "Continue Listening"
                    )
                }

                item(key = "recent_books") {
                    RecentBooksComposable(recentBooks = recentBooks, imageLoader)
                }

                item(key = "library_title") {
                    Text(
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        text = "Library"
                    )
                }

                item(key = "library_list") {
                    LibraryComposable(books = books, imageLoader)
                }
            }
        }
    )
}
