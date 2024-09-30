package org.grakovne.lissen.ui.screens.library.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.request.ImageRequest
import org.grakovne.lissen.R
import org.grakovne.lissen.domain.RecentBook
import org.grakovne.lissen.ui.screens.AsyncShimmeringImage
import org.grakovne.lissen.ui.screens.library.composables.placeholder.RecentBookPlaceholderComposable

@Composable
fun RecentBooksComposable(
    recentBooks: List<RecentBook>,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember { configuration.screenWidthDp.dp }

    val itemsVisible = 2.3f
    val spacing = 16.dp
    val totalSpacing = spacing * (itemsVisible + 1)
    val itemWidth = (screenWidth - totalSpacing) / itemsVisible

    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(recentBooks, key = { it.id }) { book ->
            RecentBookItemComposable(book = book, width = itemWidth, imageLoader = imageLoader)
        }
    }
}

@Composable
fun RecentBookItemComposable(
    book: RecentBook,
    width: Dp,
    imageLoader: ImageLoader
) {
    Column(
        modifier = Modifier
            .width(width)
            .clickable { /* TODO: Handle click */ }
    ) {
        val context = LocalContext.current

        val imageRequest = remember(book.id) {
            ImageRequest
                .Builder(context)
                .data(book.id)
                .crossfade(300)
                .build()
        }

        AsyncShimmeringImage(
            imageRequest = imageRequest,
            imageLoader = imageLoader,
            contentDescription = "${book.title} cover",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
            error = painterResource(R.drawable.fallback_cover)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RecentBooksPlaceholderComposable(
    itemCount: Int = 5
) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember { configuration.screenWidthDp.dp }

    val itemsVisible = 2.3f
    val spacing = 16.dp
    val totalSpacing = spacing * (itemsVisible + 1)
    val itemWidth = (screenWidth - totalSpacing) / itemsVisible

    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(itemCount) { RecentBookPlaceholderComposable(width = itemWidth) }
    }
}
