package com.example.trucast.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trucast.R

data class RequestedSong(
    val id: Int,
    val channelName: String,
    val songName: String,
    val channelImage: Int,
    val channelColor: Color
)

@Composable
fun RequestSongList(
    userName: String = "Name",
    onBackClick: () -> Unit = { }
) {
    val cyanColor = Color(0xFF00BCD4)

    val requestedSongs = listOf(
        RequestedSong(1, "Channel 1", "Requested Song Name 1", R.drawable.maskgroup, Color(0xFF8BC34A)),
        RequestedSong(2, "Channel 2", "Requested Song Name 2", R.drawable.indie, Color(0xFFE91E63)),
        RequestedSong(3, "Channel 3", "Requested Song Name 3", R.drawable.rnb, Color(0xFF3F51B5)),
        RequestedSong(4, "Channel 4", "Requested Song Name 4", R.drawable.pop, Color(0xFFE67E22)),
        RequestedSong(5, "Channel 5", "Requested Song Name 5", R.drawable.u, Color(0xFF29B6F6)),
        RequestedSong(6, "Channel 6", "Requested Song Name 6", R.drawable.new_img, Color(0xFF9C27B0)),
        RequestedSong(7, "Channel 9", "Requested Song Name 7", R.drawable.bol, Color(0xFF5D4037)),
        RequestedSong(8, "Channel 8", "Requested Song Name 8", R.drawable.pods, Color(0xFF283593)),
        RequestedSong(9, "channel 10", "Requested Song Name 9", R.drawable.indie1, Color(0xFF5D4037)),
        RequestedSong(10, "Channel 11", "Requested Song Name 10", R.drawable.charts, Color(0xFF00695C))
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        // Standardized Back Arrow Button - Aligned far left (0.dp) to match other screens
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 0.dp, top = 20.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp), // Aligned next to back arrow
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, cyanColor, CircleShape),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            setToSaturation(0f)
                        })
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = userName, color = Color.White, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // vector_image icon at top right (Matching ChannelPlayerUI)
                Image(
                    painter = painterResource(id = R.drawable.vector_image),
                    contentDescription = "Vector Logo",
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            // Banner Title

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(cyanColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Requested songs List",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // List of Songs
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(requestedSongs) { song ->
                    RequestedSongListItem(song)
                }
            }

            // Footer Message
            Text(
                text = "Your request for [Song Name] by [Artist Name] has been submitted. We'll notify you when it's about to play.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun RequestedSongListItem(song: RequestedSong) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 55.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(id = song.channelImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Song Name
            Text(
                text = song.songName,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Music Note Circular Button
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
