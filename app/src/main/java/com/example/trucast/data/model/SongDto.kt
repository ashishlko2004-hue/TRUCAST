package com.example.trucast.data.model

data class ItunesSearchResponse(
    val resultCount: Int,
    val results: List<SongDto>
)

data class SongDto(
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?
)
