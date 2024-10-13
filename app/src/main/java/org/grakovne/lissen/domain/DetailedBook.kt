package org.grakovne.lissen.domain

import java.io.Serializable

data class DetailedBook(
    val id: String,
    val title: String,
    val author: String,
    val files: List<BookFile>,
    val chapters: List<BookChapter>,
    val progress: MediaProgress?
) : Serializable

data class BookFile(
    val id: String,
    val name: String,
    val duration: Double,
) : Serializable

data class MediaProgress(
    val currentTime: Double,
    val isFinished: Boolean,
    val lastUpdate: Long
) : Serializable

data class BookChapter(
    val start: Double,
    val end: Double,
    val title: String,
    val id: String
): Serializable
