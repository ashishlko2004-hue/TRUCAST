package com.example.trucast.ui.screens

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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trucast.R
import java.util.*

@Composable
fun ChannelsUI(
    onSettingsClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileEditClick: () -> Unit = {},
    onChannelClick: (ChannelItem) -> Unit = {}
) {
    val cyanColor = Color(0xFF00BCD4)
    val categories = listOf("Music", "Podcasts", "Workout", "Party", "Study", "Relax")
    var selectedCategory by remember { mutableStateOf("Music") }
    var searchQuery by remember { mutableStateOf("") }

    val recentItems = listOf(
        ChannelItem(1, "Daily Mix 1", R.drawable.maskgroup),
        ChannelItem(2, "Indie Pop", R.drawable.indie),
        ChannelItem(3, "Retro Hits", R.drawable.bol)
    )

    val allChannels = listOf(
        ChannelItem(4, "New Releases", R.drawable.pop),
        ChannelItem(5, "Rock Anthems", R.drawable.u),
        ChannelItem(6, "Chill Vibes", R.drawable.new_img),
        ChannelItem(7, "Top Charts", R.drawable.charts),
        ChannelItem(8, "Electronic Mix", R.drawable.indie1),
        ChannelItem(9, "Global Top 50", R.drawable.rnb)
    )

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
            Brush.verticalGradient(listOf(Color.Black.copy(0.4f), Color.Black.copy(0.9f)))
        ))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(50.dp).clickable { onProfileEditClick() },
                    shape = CircleShape,
                    color = Color.White.copy(0.1f),
                    border = BorderStroke(1.dp, cyanColor)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = greeting, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                    Text(text = "Music Lover!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                }

                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Outlined.Notifications, null, tint = Color.White)
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(26.dp)),
                placeholder = { Text("Find your vibe...", color = Color.Gray, fontSize = 14.sp) },
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

            Spacer(modifier = Modifier.height(25.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(2) }) {
                    Text("Recently Played", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                item(span = { GridItemSpan(2) }) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(recentItems) { item ->
                            RecentCard(item, onChannelClick)
                        }
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(categories) { cat ->
                            CategoryChipUI(cat, selectedCategory == cat) { selectedCategory = cat }
                        }
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Text("New for You", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                items(allChannels) { channel ->
                    MusicGridCard(channel, onChannelClick)
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color(0xFF1A1A1A).copy(0.95f), Color(0xFF003D4D).copy(0.95f))),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(BorderStroke(1.dp, cyanColor.copy(0.3f)), RoundedCornerShape(12.dp))
                .clickable { /* Open Full Player */ }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp).fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image14),
                    contentDescription = null,
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Now Playing...", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text("Artist - Trucast Beats", color = Color.White.copy(0.6f), fontSize = 11.sp, maxLines = 1)
                }
                Icon(Icons.Default.Devices, null, tint = cyanColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(15.dp))
                Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            LinearProgressIndicator(
                progress = { 0.4f },
                modifier = Modifier.fillMaxWidth().height(2.dp).align(Alignment.BottomCenter),
                color = cyanColor,
                trackColor = Color.White.copy(0.1f)
            )
        }
    }
}

@Composable
fun RecentCard(item: ChannelItem, onClick: (ChannelItem) -> Unit) {
    Column(
        modifier = Modifier.width(110.dp).clickable { onClick(item) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            modifier = Modifier.size(110.dp).clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = item.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun CategoryChipUI(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF00BCD4) else Color.White.copy(0.1f),
        border = if (isSelected) null else BorderStroke(1.dp, Color.White.copy(0.15f))
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MusicGridCard(channel: ChannelItem, onClick: (ChannelItem) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(150.dp).clickable { onClick(channel) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box {
            Image(
                painter = painterResource(id = channel.imageRes),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.85f)))
            ))
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)) {
                Text(text = channel.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = "Playlist", color = Color(0xFF00BCD4), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).size(30.dp),
                shape = CircleShape,
                color = Color(0xFF00BCD4).copy(alpha = 0.9f)
            ) {
                Icon(Icons.Default.PlayArrow, null, tint = Color.Black, modifier = Modifier.padding(4.dp))
            }
        }
    }
}
