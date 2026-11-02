package com.example.trucast.ui.screens

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trucast.R

data class Song(
    val title: String,
    val artist: String,
    val duration: String,
    val coverRes: Int,
    val audioResId: Int,
    val audioUrl: String? = null
)

object MusicPlayerManager {
    var currentChannel by mutableStateOf("Wedding")
    var currentSongTitle by mutableStateOf("Dulhe Ki Behen Brigade")
    var currentArtist by mutableStateOf("Wedding Vocals")
    var currentCoverRes by mutableStateOf(R.drawable.bol)
    var isPlaying by mutableStateOf(false)
    var mediaPlayer: MediaPlayer? = null
    var songList: List<Song> = listOf()
    var currentIndex by mutableStateOf(0)

    fun playSong(context: Context, song: Song, channel: String, list: List<Song>, index: Int) {
        currentChannel = channel
        currentSongTitle = song.title
        currentArtist = song.artist
        currentCoverRes = song.coverRes
        songList = list
        currentIndex = index

        mediaPlayer?.stop()
        mediaPlayer?.release()
        try {
            // Play the user's actual real vocal MP3 files from res/raw
            mediaPlayer = MediaPlayer.create(context, song.audioResId).apply {
                start()
                MusicPlayerManager.isPlaying = true
                setOnCompletionListener {
                    if (songList.isNotEmpty()) {
                        val nextIndex = (currentIndex + 1) % songList.size
                        playSong(context, songList[nextIndex], currentChannel, songList, nextIndex)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MusicPlayerManager.isPlaying = false
        }
    }

    fun playOnlineSong(context: Context, song: Song, channel: String, list: List<Song>, index: Int) {
        playSong(context, song, channel, list, index)
    }

    fun togglePlayPause(context: Context) {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                MusicPlayerManager.isPlaying = false
            } else {
                player.start()
                MusicPlayerManager.isPlaying = true
            }
        } ?: run {
            if (songList.isNotEmpty()) {
                playSong(context, songList[currentIndex], currentChannel, songList, currentIndex)
            }
        }
    }
}
