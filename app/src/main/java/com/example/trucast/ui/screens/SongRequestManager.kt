package com.example.trucast.ui.screens

import androidx.compose.runtime.mutableStateListOf

data class RequestedSong(
    val id: String,
    val songName: String,
    val contactInfo: String,
    val timestamp: Long = System.currentTimeMillis()
)

object SongRequestManager {
    val requests = mutableStateListOf<RequestedSong>(
        RequestedSong("1", "Kabira (En Concert)", "user1@gmail.com"),
        RequestedSong("2", "Din Shagna Da", "user2@gmail.com")
    )

    fun addRequest(songName: String, contactInfo: String) {
        requests.add(0, RequestedSong(System.currentTimeMillis().toString(), songName, contactInfo))
    }
}
