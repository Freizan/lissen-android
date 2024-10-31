package org.grakovne.lissen.ui.screens.library.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.grakovne.lissen.content.LissenMediaProvider
import org.grakovne.lissen.domain.Book

class LibrarySearchPagingSource(
    private val libraryId: String,
    private val mediaChannel: LissenMediaProvider,
    private val searchToken: String
) : PagingSource<Int, Book>() {

    override fun getRefreshKey(state: PagingState<Int, Book>) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return mediaChannel
            .searchBooks(libraryId, searchToken)
            .fold(
                onSuccess = { LoadResult.Page(it, null, null) },
                onFailure = { LoadResult.Page(emptyList(), null, null) }
            )
    }
}
