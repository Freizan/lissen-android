package org.grakovne.lissen.channel.audiobookshelf.client

import org.grakovne.lissen.channel.audiobookshelf.model.LibraryItemIdResponse
import org.grakovne.lissen.channel.audiobookshelf.model.LibraryItemsResponse
import org.grakovne.lissen.channel.audiobookshelf.model.LibraryResponse
import org.grakovne.lissen.channel.audiobookshelf.model.LoginRequest
import org.grakovne.lissen.channel.audiobookshelf.model.LoginResponse
import org.grakovne.lissen.channel.audiobookshelf.model.PersonalizedFeedResponse
import org.grakovne.lissen.channel.audiobookshelf.model.SyncProgressRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AudiobookshelfApiClient {

    @GET("/api/libraries")
    suspend fun fetchLibraries(): Response<LibraryResponse>

    @GET("/api/libraries/{libraryId}/personalized")
    suspend fun fetchPersonalizedFeed(
        @Path("libraryId") libraryId: String
    ): Response<List<PersonalizedFeedResponse>>

    @GET("/api/me/progress/{itemId}")
    suspend fun fetchLibraryItemProgress(
        @Path("itemId") itemId: String
    ): Response<org.grakovne.lissen.channel.audiobookshelf.model.MediaProgressResponse>

    @GET("api/libraries/{libraryId}/items?sort=media.metadata.title&minified=1")
    suspend fun fetchLibraryItems(
        @Path("libraryId") libraryId: String,
        @Query("limit") pageSize: Int,
        @Query("page") pageNumber: Int,
    ): Response<LibraryItemsResponse>

    @GET("/api/items/{itemId}")
    suspend fun fetchLibraryItem(
        @Path("itemId") itemId: String
    ): Response<LibraryItemIdResponse>

    @POST("/api/session/{itemId}/sync")
    suspend fun publishLibraryItemProgress(
        @Path("itemId") itemId: String,
        @Body syncProgressRequest: SyncProgressRequest
    ): Response<Unit>

    @POST("/api/items/{itemId}/play")
    suspend fun startPlayback(
        @Path("itemId") itemId: String,
        @Body syncProgressRequest: org.grakovne.lissen.channel.audiobookshelf.model.StartPlaybackRequest
    ): Response<org.grakovne.lissen.channel.audiobookshelf.model.PlaybackSessionResponse>

    @POST("/api/session/{sessionId}/close")
    suspend fun stopPlayback(
        @Path("sessionId") sessionId: String
    ): Response<Unit>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}