package com.example.trucast.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trucast.R


data class ChannelItem(
    val id: Int,
    val name: String,
    val imageRes: Int
)

@Composable
fun ChannelsUI(
    onSettingsClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileEditClick: () -> Unit = {},
    onChannelClick: (ChannelItem) -> Unit = {}
) {
    val cyanColor = Color(0xFF00BCD4)

    // Images aur names ki list
    val channels = listOf(
        ChannelItem(1, "Channel 1", R.drawable.maskgroup),
        ChannelItem(2, "Channel 2", R.drawable.indie),
        ChannelItem(3, "Channel 3", R.drawable.rnb),
        ChannelItem(4, "Channel 4", R.drawable.pop),
        ChannelItem(5, "Channel 5", R.drawable.u),
        ChannelItem(6, "Channel 6", R.drawable.new_img),
        ChannelItem(8, "Channel 8", R.drawable.charts),
        ChannelItem(9, "Channel 9", R.drawable.pods),
        ChannelItem(10, "channel 10", R.drawable.bol),
        ChannelItem(11, "Channel 11", R.drawable.indie1)
    )

    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(1.dp, cyanColor, CircleShape)
                        .padding(2.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Welcome!", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp).clickable { onProfileEditClick() }
                        )
                    }
                    Text(text = "Name", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }

                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, null, tint = Color.White)
                }
                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Default.Notifications, null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            Text(
                text = "Listen to your\nFavorite channels",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().height(54.dp).background(Color.Black, RoundedCornerShape(27.dp)),
                placeholder = { Text("Channels", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(27.dp),
                singleLine = true

            )
            Spacer(modifier = Modifier.height(30.dp))

            // Grid Section: Fixed and Cleaned
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(channels) { channel ->
                    ChannelGridCard(channel, onChannelClick)
                }
            }
        }
    }
}

@Composable
fun ChannelGridCard(channel: ChannelItem, onClick: (ChannelItem) -> Unit) {
    // Card ab clickable hai aur channel name pass karega
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick(channel) }

    ) {
        Image(
            painter = painterResource(id = channel.imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
