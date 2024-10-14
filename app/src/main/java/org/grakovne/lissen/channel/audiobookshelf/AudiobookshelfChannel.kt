package org.grakovne.lissen.channel.audiobookshelf

import android.net.Uri
import org.grakovne.lissen.channel.audiobookshelf.api.AudioBookshelfDataRepository
import org.grakovne.lissen.channel.audiobookshelf.api.AudioBookshelfMediaRepository
import org.grakovne.lissen.channel.audiobookshelf.api.AudioBookshelfSyncService
import org.grakovne.lissen.channel.audiobookshelf.converter.LibraryItemIdResponseConverter
import org.grakovne.lissen.channel.audiobookshelf.converter.LibraryItemResponseConverter
import org.grakovne.lissen.channel.audiobookshelf.converter.LibraryResponseConverter
import org.grakovne.lissen.channel.audiobookshelf.converter.PlaybackSessionResponseConverter
import org.grakovne.lissen.channel.audiobookshelf.converter.RecentBookResponseConverter
import org.grakovne.lissen.channel.audiobookshelf.model.DeviceInfo
import org.grakovne.lissen.channel.audiobookshelf.model.StartPlaybackRequest
import org.grakovne.lissen.channel.common.ApiResult
import org.grakovne.lissen.domain.Book
import org.grakovne.lissen.domain.Library
import org.grakovne.lissen.domain.PlaybackProgress
import org.grakovne.lissen.domain.PlaybackSession
import org.grakovne.lissen.domain.RecentBook
import org.grakovne.lissen.domain.UserAccount
import org.grakovne.lissen.persistence.preferences.LissenSharedPreferences
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudiobookshelfChannel @Inject constructor(
    private val dataRepository: AudioBookshelfDataRepository,
    private val mediaRepository: AudioBookshelfMediaRepository,
    private val recentBookResponseConverter: RecentBookResponseConverter,
    private val libraryItemResponseConverter: LibraryItemResponseConverter,
    private val libraryResponseConverter: LibraryResponseConverter,
    private val libraryItemIdResponseConverter: LibraryItemIdResponseConverter,
    private val sessionResponseConverter: PlaybackSessionResponseConverter,
    private val preferences: LissenSharedPreferences,
    private val syncService: AudioBookshelfSyncService
) {

    fun provideFileUri(
        libraryItemId: String,
        chapterId: String
    ): Uri = Uri.parse(preferences.getHost())
        .buildUpon()
        .appendPath("api")
        .appendPath("items")
        .appendPath(libraryItemId)
        .appendPath("file")
        .appendPath(chapterId)
        .appendQueryParameter("token", preferences.getToken())
        .build()

    fun provideChapterCoverUri(
        libraryItemId: String
    ): Uri = Uri.parse(preferences.getHost())
        .buildUpon()
        .appendPath("api")
        .appendPath("items")
        .appendPath(libraryItemId)
        .appendPath("cover")
        .appendQueryParameter("token", preferences.getToken())
        .build()

    suspend fun syncProgress(
        itemId: String,
        progress: PlaybackProgress
    ): ApiResult<Unit> = syncService.syncProgress(itemId, progress)

    suspend fun fetchBookCover(
        itemId: String
    ): ApiResult<InputStream> = mediaRepository.fetchBookCover(itemId)

    suspend fun fetchLibraryItems(
        libraryId: String
    ): ApiResult<List<Book>> = dataRepository
        .fetchLibraryItems(libraryId)
        .map { libraryItemResponseConverter.apply(it) }

    suspend fun fetchLibraries(): ApiResult<List<Library>> = dataRepository
        .fetchLibraries()
        .map { libraryResponseConverter.apply(it) }

    suspend fun startPlayback(
        itemId: String,
        supportedMimeTypes: List<String>,
        deviceId: String
    ): ApiResult<PlaybackSession> {
        val request = StartPlaybackRequest(
            supportedMimeTypes = supportedMimeTypes,
            deviceInfo = DeviceInfo(
                clientName = "Lissen Android",
                deviceId = deviceId
            ),
            forceTranscode = false,
            forceDirectPlay = false,
            mediaPlayer = "exo3"
        )

        return dataRepository
            .startPlayback(
                itemId = itemId,
                request = request
            )
            .map { sessionResponseConverter.apply(it) }
    }

    suspend fun stopPlayback(sessionId: String) = dataRepository
        .stopPlayback(sessionId)

    suspend fun getRecentItems(): ApiResult<List<RecentBook>> =
        dataRepository
            .getRecentItems()
            .map { recentBookResponseConverter.apply(it) }

    suspend fun getLibraryItem(itemId: String) = dataRepository
        .getLibraryItem(itemId)
        .map { item ->
            dataRepository
                .getItemIdProgress(item.id)
                .fold(
                    onSuccess = { libraryItemIdResponseConverter.apply(item, it) },
                    onFailure = { libraryItemIdResponseConverter.apply(item, null) }
                )
        }

    suspend fun authorize(
        host: String,
        username: String,
        password: String
    ): ApiResult<UserAccount> = dataRepository.authorize(host, username, password)


}