@file:OptIn(UnstableApi::class)

package com.example.trucast.ui.screens

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
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

    var exoPlayer: ExoPlayer? = null
    var songList: List<Song> = listOf()
    var currentIndex by mutableStateOf(0)

    val currentPosition: Long
        get() = exoPlayer?.currentPosition ?: 0L

    val duration: Long
        get() = exoPlayer?.duration?.coerceAtLeast(1L) ?: 1L

    fun playSong(context: Context, song: Song, channel: String, list: List<Song>, index: Int) {
        currentChannel = channel
        currentSongTitle = song.title
        currentArtist = song.artist
        currentCoverRes = song.coverRes
        songList = list
        currentIndex = index

        try {
            exoPlayer?.stop()
            exoPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        exoPlayer = null

        try {
            val player = ExoPlayer.Builder(context).build()
            // Official Google Media3 URI helper for raw resource MP3s
            val uri = RawResourceDataSource.buildRawResourceUri(song.audioResId)
            val mediaItem = MediaItem.fromUri(uri)

            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            exoPlayer = player
            isPlaying = true

            player.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingState: Boolean) {
                    isPlaying = isPlayingState
                }
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        if (songList.isNotEmpty()) {
                            val nextIndex = (currentIndex + 1) % songList.size
                            playSong(context, songList[nextIndex], currentChannel, songList, nextIndex)
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            isPlaying = false
        }
    }

    fun playOnlineSong(context: Context, song: Song, channel: String, list: List<Song>, index: Int) {
        playSong(context, song, channel, list, index)
    }

    fun togglePlayPause(context: Context) {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                isPlaying = false
            } else {
                player.play()
                isPlaying = true
            }
        } ?: run {
            if (songList.isNotEmpty()) {
                playSong(context, songList[currentIndex], currentChannel, songList, currentIndex)
            }
        }
    }
}
