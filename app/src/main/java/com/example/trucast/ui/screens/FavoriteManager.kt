package com.example.trucast.ui.screens

import androidx.compose.runtime.mutableStateListOf

object FavoriteManager {
    val likedSongs = mutableStateListOf<String>()

    fun toggleLike(songTitle: String) {
        if (likedSongs.contains(songTitle)) {
            likedSongs.remove(songTitle)
        } else {
            likedSongs.add(songTitle)
        }
    }

    fun isLiked(songTitle: String): Boolean {
        return likedSongs.contains(songTitle)
    }
}
