package org.grakovne.lissen.channel.sources.cache

import android.net.Uri
import org.grakovne.lissen.channel.ChannelCode
import org.grakovne.lissen.channel.common.ApiError
import org.grakovne.lissen.channel.common.ApiResult
import org.grakovne.lissen.channel.common.MediaChannel
import org.grakovne.lissen.domain.Book
import org.grakovne.lissen.domain.DetailedBook
import org.grakovne.lissen.domain.Library
import org.grakovne.lissen.domain.PagedItems
import org.grakovne.lissen.domain.PlaybackProgress
import org.grakovne.lissen.domain.PlaybackSession
import org.grakovne.lissen.domain.RecentBook
import org.grakovne.lissen.domain.UserAccount
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForceCacheChannel @Inject constructor() : MediaChannel {
    override fun getChannelCode(): ChannelCode = ChannelCode.CACHE_FORCE

    override fun provideFileUri(libraryItemId: String, chapterId: String): Uri = Uri.EMPTY

    override fun provideBookCover(bookId: String): Uri = Uri.EMPTY

    override suspend fun syncProgress(itemId: String, progress: PlaybackProgress): ApiResult<Unit> =
        ApiResult.Success(Unit)

    override suspend fun fetchBookCover(bookId: String): ApiResult<InputStream> {
        return ApiResult.Error(ApiError.InternalError)
    }

    override suspend fun fetchBooks(
        libraryId: String,
        pageSize: Int,
        pageNumber: Int
    ): ApiResult<PagedItems<Book>> = ApiResult.Success(PagedItems(emptyList(), 0, 0))

    override suspend fun fetchLibraries(): ApiResult<List<Library>> = ApiResult.Success(emptyList())

    override suspend fun startPlayback(
        itemId: String,
        supportedMimeTypes: List<String>,
        deviceId: String
    ): ApiResult<PlaybackSession> =
        ApiResult.Error(ApiError.InternalError)

    override suspend fun fetchRecentListenedBooks(libraryId: String): ApiResult<List<RecentBook>> =
        ApiResult.Success(emptyList())

    override suspend fun fetchBook(bookId: String): ApiResult<DetailedBook> =
        ApiResult.Error(ApiError.InternalError)

    override suspend fun authorize(
        host: String,
        username: String,
        password: String
    ): ApiResult<UserAccount> = ApiResult.Error(ApiError.InternalError)
}