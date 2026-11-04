package com.example.trucast.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.trucast.R
import kotlinx.coroutines.delay
import java.util.Locale

object MusicRepository {
    fun getSongsForChannel(channelName: String): List<Song> {
        return when {
            channelName.contains("Wedding", true) -> listOf(
                Song("Dulhe Ki Behen Brigade", "Wedding Beats", "3:45", R.drawable.bol, R.raw.dulhe_ki_behen_brigade),
                Song("Chal Shadi Kar Lite", "Kurukshetra", "4:12", R.drawable.pop, R.raw.chal_shadi_kar_lite)
            )
            channelName.contains("Workout", true) || channelName.contains("Gym", true) -> listOf(
                Song("Sanson Ki Mala", "Workout Remix", "4:30", R.drawable.u, R.raw.sanson_ki_mala)
            )
            channelName.contains("Party", true) || channelName.contains("Club", true) -> listOf(
                Song("Sajanva", "Mirzapur", "3:08", R.drawable.pop, R.raw.sajanva_mirzapur)
            )
            else -> listOf(
                Song("Sanson Ki Mala", "Mirzapur", "4:30", R.drawable.charts, R.raw.sanson_ki_mala),
                Song("Dulhe Ki Behen Brigade", "Trucast Audio", "3:45", R.drawable.charts, R.raw.dulhe_ki_behen_brigade)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelPlayerUI(
    channelName: String = "Global Top 50",
    userName: String = "Music Lover",
    onBackClick: () -> Unit = {},
    onRequestSongClick: () -> Unit = {}
) {
    val cyanColor = Color(0xFF00BCD4)
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val songList = remember(channelName) { MusicRepository.getSongsForChannel(channelName) }
    var currentSongIndex by remember {
        mutableStateOf(if (MusicPlayerManager.currentChannel == channelName) MusicPlayerManager.currentIndex else 0)
    }
    var isPlaying by remember {
        mutableStateOf(if (MusicPlayerManager.currentChannel == channelName) MusicPlayerManager.isPlaying else false)
    }
    var showPlaylistSheet by remember { mutableStateOf(false) }
    var showLyricsSheet by remember { mutableStateOf(false) }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var sleepTimerActiveMinutes by remember { mutableStateOf<Int?>(null) }

    // Seekbar & Repeat States
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(1L) }
    var isRepeatEnabled by remember { mutableStateOf(MusicPlayerManager.mediaPlayer?.isLooping ?: false) }

    val currentSong = songList[currentSongIndex % songList.size]
    val isLiked = FavoriteManager.isLiked(currentSong.title)

    fun startPlaying(song: Song) {
        MusicPlayerManager.playSong(context, song, channelName, songList, currentSongIndex)
        isPlaying = true
    }

    // Start playing initial song on enter if not already playing this channel
    LaunchedEffect(currentSongIndex, songList) {
        if (MusicPlayerManager.currentChannel != channelName || MusicPlayerManager.mediaPlayer == null) {
            startPlaying(currentSong)
        } else {
            isPlaying = MusicPlayerManager.isPlaying
        }
    }

    // Track playback progress
    LaunchedEffect(isPlaying, MusicPlayerManager.mediaPlayer) {
        while (isPlaying) {
            MusicPlayerManager.mediaPlayer?.let { player ->
                try {
                    currentPosition = player.currentPosition.toLong()
                    duration = player.duration.toLong().coerceAtLeast(1L)
                    isPlaying = player.isPlaying
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            delay(500)
        }
    }

    val togglePlayPause = {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        MusicPlayerManager.togglePlayPause(context)
        isPlaying = MusicPlayerManager.isPlaying
    }

    val playNext = {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        currentSongIndex = (currentSongIndex + 1) % songList.size
        startPlaying(songList[currentSongIndex])
        isPlaying = true
    }

    val playPrev = {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        currentSongIndex = if (currentSongIndex - 1 >= 0) currentSongIndex - 1 else songList.size - 1
        startPlaying(songList[currentSongIndex])
        isPlaying = true
    }

    val seekBackward: () -> Unit = {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        MusicPlayerManager.mediaPlayer?.let { player ->
            val newPos = (player.currentPosition - 10000).coerceAtLeast(0)
            player.seekTo(newPos)
            currentPosition = newPos.toLong()
        }
    }

    val seekForward: () -> Unit = {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        MusicPlayerManager.mediaPlayer?.let { player ->
            val newPos = (player.currentPosition + 10000).coerceAtMost(player.duration)
            player.seekTo(newPos)
            currentPosition = newPos.toLong()
        }
    }

    val toggleRepeat: () -> Unit = {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        isRepeatEnabled = !isRepeatEnabled
        MusicPlayerManager.mediaPlayer?.isLooping = isRepeatEnabled
    }

    fun formatTime(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60)) % 60
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    // --- Animations ---
    val infiniteTransition = rememberInfiniteTransition(label = "player")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color.Black.copy(0.4f), Color.Black.copy(0.92f)))
        ))

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Playing Category", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                    Text(text = channelName, color = cyanColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Image(
                    painter = painterResource(id = R.drawable.vector_image),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Equalizer Waveform Bars
            EqualizerBars(isPlaying = isPlaying)

            Spacer(modifier = Modifier.height(8.dp))

            // Central Animated Disc with Glassmorphism Card
            Surface(
                modifier = Modifier.size(230.dp),
                shape = RoundedCornerShape(115.dp),
                color = Color.White.copy(alpha = 0.05f),
                border = BorderStroke(1.5.dp, cyanColor.copy(alpha = 0.4f)),
                shadowElevation = 12.dp
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            brush = Brush.sweepGradient(listOf(cyanColor, Color.Magenta, cyanColor)),
                            startAngle = if (isPlaying) rotation else 0f,
                            sweepAngle = 280f,
                            useCenter = false,
                            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Image(
                        painter = painterResource(id = currentSong.coverRes),
                        contentDescription = "Disc",
                        modifier = Modifier
                            .size(175.dp)
                            .rotate(if (isPlaying) rotation else 0f)
                            .clip(CircleShape)
                            .border(3.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Song Info with Like (Heart) Button
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentSong.title,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Artist: ${currentSong.artist} | Trucast Audio",
                        color = cyanColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        FavoriteManager.toggleLike(currentSong.title)
                    }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color(0xFFFF5252) else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Seekbar (Slider) & Timestamps
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
                Slider(
                    value = currentPosition.toFloat(),
                    onValueChange = { newVal ->
                        currentPosition = newVal.toLong()
                        MusicPlayerManager.mediaPlayer?.seekTo(newVal.toInt())
                    },
                    valueRange = 0f..duration.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = cyanColor,
                        activeTrackColor = cyanColor,
                        inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.height(24.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = formatTime(currentPosition), color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(text = formatTime(duration), color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Advanced Audio Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = toggleRepeat, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = if (isRepeatEnabled) cyanColor else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = seekBackward, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Replay10, "Rewind 10s", tint = Color.White, modifier = Modifier.size(26.dp))
                }

                IconButton(onClick = playPrev, modifier = Modifier.size(44.dp)) {
                    Icon(Icons.Default.SkipPrevious, "Previous", tint = Color.White, modifier = Modifier.size(32.dp))
                }

                Surface(
                    onClick = togglePlayPause,
                    modifier = Modifier.size(68.dp).scale(if (isPlaying) pulseScale else 1f),
                    shape = CircleShape,
                    color = cyanColor,
                    border = BorderStroke(2.dp, Color.White),
                    shadowElevation = 8.dp
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.Black,
                        modifier = Modifier.size(38.dp).padding(14.dp)
                    )
                }

                IconButton(onClick = playNext, modifier = Modifier.size(44.dp)) {
                    Icon(Icons.Default.SkipNext, "Next", tint = Color.White, modifier = Modifier.size(32.dp))
                }

                IconButton(onClick = seekForward, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Forward10, "Forward 10s", tint = Color.White, modifier = Modifier.size(26.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons (Request Song, Playlist, Lyrics, Sleep Timer) in Frosted Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.05f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PlayerActionCard(
                        title = "Request",
                        icon = Icons.Default.MusicNote,
                        modifier = Modifier.weight(1f),
                        onClick = onRequestSongClick
                    )
                    PlayerActionCard(
                        title = "Playlist",
                        icon = Icons.AutoMirrored.Filled.QueueMusic,
                        modifier = Modifier.weight(1f),
                        onClick = { showPlaylistSheet = true }
                    )
                    PlayerActionCard(
                        title = "Lyrics",
                        icon = Icons.Default.Subtitles,
                        modifier = Modifier.weight(1f),
                        onClick = { showLyricsSheet = true }
                    )
                    PlayerActionCard(
                        title = "Timer",
                        icon = Icons.Default.Timer,
                        modifier = Modifier.weight(1f),
                        onClick = { showSleepTimerDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Playlist Bottom Sheet Modal
        if (showPlaylistSheet) {
            ModalBottomSheet(
                onDismissRequest = { showPlaylistSheet = false },
                containerColor = Color(0xFF1A1A1A)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "$channelName Playlist (${songList.size} Songs)",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(songList) { index, song ->
                            val liked = FavoriteManager.isLiked(song.title)
                            Surface(
                                onClick = {
                                    currentSongIndex = index
                                    isPlaying = true
                                    showPlaylistSheet = false
                                    startPlaying(song)
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = if (index == currentSongIndex) cyanColor.copy(0.15f) else Color.Transparent,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = song.coverRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = song.title,
                                                color = if (index == currentSongIndex) cyanColor else Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                            if (liked) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Icon(Icons.Default.Favorite, null, tint = Color(0xFFFF5252), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                        Text(
                                            text = "${song.artist} • ${song.duration}",
                                            color = Color.White.copy(0.6f),
                                            fontSize = 12.sp
                                        )
                                    }
                                    if (index == currentSongIndex && isPlaying) {
                                        Icon(Icons.Default.VolumeUp, null, tint = cyanColor)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

        // Real-Time Synchronized Lyrics Bottom Sheet Modal
        if (showLyricsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showLyricsSheet = false },
                containerColor = Color(0xFF141414)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Real-Time Synchronized Lyrics",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap any line to jump to that part of the song",
                        color = Color.White.copy(0.6f),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    val lyrics = remember(currentSong.title) { LyricsManager.getLyricsForSong(currentSong.title) }
                    val activeLyricIndex = lyrics.indexOfLast { currentPosition >= it.timeMs }.coerceAtLeast(0)

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        itemsIndexed(lyrics) { idx, lyric ->
                            val isHighlighted = idx == activeLyricIndex
                            Surface(
                                onClick = {
                                    MusicPlayerManager.mediaPlayer?.seekTo(lyric.timeMs)
                                    currentPosition = lyric.timeMs.toLong()
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isHighlighted) cyanColor.copy(0.12f) else Color.Transparent,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = lyric.text,
                                    color = if (isHighlighted) cyanColor else Color.White.copy(0.5f),
                                    fontSize = if (isHighlighted) 17.sp else 14.sp,
                                    fontWeight = if (isHighlighted) FontWeight.ExtraBold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

        // Sleep Timer Dialog
        if (showSleepTimerDialog) {
            Dialog(onDismissRequest = { showSleepTimerDialog = false }) {
                Surface(
                    modifier = Modifier.fillMaxWidth(0.85f).wrapContentHeight(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1A1A1A),
                    border = BorderStroke(1.dp, cyanColor.copy(0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Timer, null, tint = cyanColor, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Sleep Timer", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Stop playback after:", color = Color.White.copy(0.7f), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(20.dp))

                        val minutesList = listOf(5, 15, 30, 45, 60)
                        minutesList.forEach { mins ->
                            OutlinedButton(
                                onClick = {
                                    sleepTimerActiveMinutes = mins
                                    showSleepTimerDialog = false
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, if (sleepTimerActiveMinutes == mins) cyanColor else Color.White.copy(0.3f))
                            ) {
                                Text("$mins Minutes", fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { sleepTimerActiveMinutes = null; showSleepTimerDialog = false }) {
                            Text("Turn Off Timer", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EqualizerBars(isPlaying: Boolean) {
    val transition = rememberInfiniteTransition(label = "eq")
    val h1 by transition.animateFloat(10f, 35f, infiniteRepeatable(tween(300, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h1")
    val h2 by transition.animateFloat(10f, 35f, infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h2")
    val h3 by transition.animateFloat(5f, 25f, infiniteRepeatable(tween(250, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h3")
    val h4 by transition.animateFloat(20f, 40f, infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h4")
    val h5 by transition.animateFloat(12f, 30f, infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h5")

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(40.dp)
    ) {
        val heights = listOf(h1, h2, h3, h4, h5)
        heights.forEach { h ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(if (isPlaying) h.dp else 6.dp)
                    .background(Color(0xFF00BCD4), RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun PlayerActionCard(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(56.dp).clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)
        ) {
            Icon(icon, null, tint = Color(0xFF00BCD4), modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(3.dp))
            Text(text = title, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}
