package com.example.trucast.ui.screens

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trucast.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun ChannelsUI(
    onSettingsClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileEditClick: () -> Unit = {},
    onChannelClick: (ChannelItem) -> Unit = {},
    onNowPlayingClick: (String) -> Unit = {}
) {
    val cyanColor = Color(0xFF00BCD4)
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    LaunchedEffect(userId) {
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        val name = doc.getString("fullName")
                        if (!name.isNullOrEmpty()) {
                            UserProfileManager.fullName = name
                        }
                        val imgStr = doc.getString("profileImageUri")
                        if (!imgStr.isNullOrEmpty()) {
                            UserProfileManager.profileImageUri = Uri.parse(imgStr)
                        }
                    }
                }
        }
    }

    val categories = listOf("All", "Wedding", "Workout", "Party", "Chill", "Romantic", "Study")
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val recentItems = listOf(
        ChannelItem(1, "Wedding", R.drawable.bol),
        ChannelItem(2, "Gym Pump Workout", R.drawable.u),
        ChannelItem(3, "Club Party Beats", R.drawable.pop)
    )

    val allChannels = listOf(
        ChannelItem(4, "Wedding", R.drawable.bol),
        ChannelItem(5, "Gym Pump Workout", R.drawable.u),
        ChannelItem(6, "Club Party Beats", R.drawable.pop),
        ChannelItem(7, "Lofi Chill Vibes", R.drawable.indie),
        ChannelItem(8, "Romantic Melodies", R.drawable.maskgroup),
        ChannelItem(9, "Deep Study Focus", R.drawable.new_img),
        ChannelItem(10, "Top Charts 2025", R.drawable.charts),
        ChannelItem(11, "Global Top 50", R.drawable.rnb)
    )

    val filteredChannels = if (selectedCategory == "All") {
        allChannels
    } else {
        allChannels.filter { it.name.contains(selectedCategory, true) }
    }

    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Gorgeous Frosted Glass User Greeting Card Header
            Surface(
                modifier = Modifier.fillMaxWidth().height(74.dp),
                shape = RoundedCornerShape(22.dp),
                color = Color.White.copy(alpha = 0.07f),
                border = BorderStroke(1.5.dp, cyanColor.copy(alpha = 0.5f)),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(50.dp).clickable { onProfileEditClick() },
                        shape = CircleShape,
                        color = Color.White.copy(0.1f),
                        border = BorderStroke(2.dp, cyanColor),
                        shadowElevation = 4.dp
                    ) {
                        if (UserProfileManager.profileImageUri != null) {
                            AsyncImage(
                                model = UserProfileManager.profileImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = null,
                                modifier = Modifier.padding(2.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = greeting, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        Text(text = UserProfileManager.fullName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                    }

                    val isOwner = auth.currentUser?.email?.trim()?.lowercase() == "ashishlko2004@gmail.com"
                    if (isOwner) {
                        Box {
                            IconButton(onClick = onNotificationClick) {
                                Icon(Icons.Outlined.Notifications, null, tint = cyanColor)
                            }
                            if (SongRequestManager.requests.isNotEmpty()) {
                                Badge(
                                    modifier = Modifier.align(Alignment.TopEnd).offset(x = (-4).dp, y = 4.dp),
                                    containerColor = Color(0xFFFF5252),
                                    contentColor = Color.White
                                ) {
                                    Text("${SongRequestManager.requests.size}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, null, tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(26.dp)),
                placeholder = { Text("Find songs, artists, vibes...", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = cyanColor) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = cyanColor,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = cyanColor
                ),
                shape = RoundedCornerShape(26.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Main Content Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Featured Banner Card
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF003D4D), Color(0xFF1A1A1A))))
                            .border(1.5.dp, cyanColor.copy(0.5f), RoundedCornerShape(24.dp))
                            .clickable { onChannelClick(ChannelItem(11, "Global Top 50", R.drawable.rnb)) }
                            .padding(20.dp)
                    ) {
                        Column(modifier = Modifier.align(Alignment.CenterStart).fillMaxWidth(0.65f)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = cyanColor.copy(0.25f)
                            ) {
                                Text("FEATURED VIBE", color = cyanColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("TRUCAST Summer 2025", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Top streaming tracks & party anthems", color = Color.White.copy(0.7f), fontSize = 11.sp, maxLines = 1)
                        }
                        Surface(
                            modifier = Modifier.align(Alignment.CenterEnd).size(50.dp),
                            shape = CircleShape,
                            color = cyanColor,
                            shadowElevation = 6.dp
                        ) {
                            Icon(Icons.Default.PlayArrow, null, tint = Color.Black, modifier = Modifier.padding(13.dp))
                        }
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Text("Recently Played", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                item(span = { GridItemSpan(2) }) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        items(recentItems) { item ->
                            RecentCard(item, onChannelClick)
                        }
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(5.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(categories) { cat ->
                            CategoryChipUI(cat, selectedCategory == cat) { selectedCategory = cat }
                        }
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Text("Explore Channels", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                items(filteredChannels) { channel ->
                    MusicGridCard(channel, onChannelClick)
                }
            }
        }

        // Persistent Now Playing Mini-Player Bar
        if (MusicPlayerManager.exoPlayer != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
                    .height(66.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color(0xFF1E1E1E), Color(0xFF003D4D))),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(BorderStroke(1.dp, cyanColor.copy(0.4f)), RoundedCornerShape(16.dp))
                    .clickable { onNowPlayingClick(MusicPlayerManager.currentChannel) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp).fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = MusicPlayerManager.currentCoverRes),
                        contentDescription = null,
                        modifier = Modifier.size(46.dp).clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(MusicPlayerManager.currentSongTitle, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(MusicPlayerManager.currentArtist, color = cyanColor, fontSize = 11.sp, maxLines = 1)
                    }
                    IconButton(onClick = { MusicPlayerManager.togglePlayPause(context) }) {
                        Icon(
                            imageVector = if (MusicPlayerManager.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentCard(item: ChannelItem, onClick: (ChannelItem) -> Unit) {
    Column(
        modifier = Modifier.width(115.dp).clickable { onClick(item) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            modifier = Modifier
                .size(115.dp)
                .shadow(6.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun CategoryChipUI(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        color = if (isSelected) Color(0xFF00BCD4) else Color.White.copy(alpha = 0.08f),
        border = if (isSelected) BorderStroke(1.5.dp, Color.White) else BorderStroke(1.dp, Color(0xFF00BCD4).copy(0.4f)),
        shadowElevation = if (isSelected) 6.dp else 0.dp
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.White,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MusicGridCard(channel: ChannelItem, onClick: (ChannelItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(8.dp, RoundedCornerShape(22.dp))
            .clickable { onClick(channel) },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = channel.imageRes),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.85f)))
            ))
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                Text(text = channel.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Curated Playlist", color = Color(0xFF00BCD4), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp).size(34.dp),
                shape = CircleShape,
                color = Color(0xFF00BCD4).copy(alpha = 0.95f),
                shadowElevation = 4.dp
            ) {
                Icon(Icons.Default.PlayArrow, null, tint = Color.Black, modifier = Modifier.padding(6.dp))
            }
        }
    }
}
