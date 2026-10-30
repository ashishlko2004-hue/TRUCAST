package com.example.trucast.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.QueueMusic
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trucast.R

@Composable
fun ChannelPlayerUI(
    channelName: String = "Global Top 50",
    userName: String = "Music Lover",
    onBackClick: () -> Unit = {},
    onRequestSongClick: () -> Unit = {}
) {
    val cyanColor = Color(0xFF00BCD4)
    val haptic = LocalHapticFeedback.current
    
    // --- Animations ---
    val infiniteTransition = rememberInfiniteTransition(label = "player")
    
    // Rotating Disc Animation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    // Pulsing Glow for Play Button
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Image with Premium Blur Overlay
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color.Black.copy(0.3f), Color.Black.copy(0.9f)))
        ))

        // 2. Header Content
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

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
                    Text(text = "Playing from", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                    Text(text = channelName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Image(
                    painter = painterResource(id = R.drawable.vector_image),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // 3. Central Animated Disc (The Masterpiece)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(300.dp)
            ) {
                // Neon Outer Ring
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.05f),
                        style = Stroke(width = 15.dp.toPx())
                    )
                    drawArc(
                        brush = Brush.sweepGradient(listOf(cyanColor, Color.Magenta, cyanColor)),
                        startAngle = rotation,
                        sweepAngle = 280f,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                // Rotating Album Art
                Image(
                    painter = painterResource(id = R.drawable.image14),
                    contentDescription = "Disc", 
                    modifier = Modifier
                        .size(240.dp)
                        .rotate(rotation)
                        .clip(CircleShape)
                        .border(4.dp, Color.White.copy(alpha = 0.1f), CircleShape),
                    contentScale = ContentScale.Crop
                )
                // Play Button with Pulse Effect
                Surface(
                    onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
                    modifier = Modifier.size(65.dp).scale(pulseScale),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp).padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 4. Song Info
            Text(
                text = "Tu Maan Meri Jaan",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Artist: King | Trucast Premium",
                color = cyanColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 5. Action Buttons (Glassy Style)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlayerActionCard(
                    title = "Request Song",
                    icon = Icons.Default.MusicNote,
                    modifier = Modifier.weight(1f),
                    onClick = onRequestSongClick
                )
                PlayerActionCard(
                    title = "Playlist",
                    icon = Icons.AutoMirrored.Filled.QueueMusic,
                    modifier = Modifier.weight(1f),
                    onClick = { /* Open Playlist */ }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 6. Premium Bottom Social Bar
            Surface(
                modifier = Modifier.padding(bottom = 30.dp),
                shape = RoundedCornerShape(30.dp),
                color = Color.White.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(25.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniSocial(R.drawable.google5)
                    MiniSocial(R.drawable.facebook4)
                    MiniSocial(R.drawable.insta_icon)
                    MiniSocial(R.drawable.snapchat_icon)
                }
            }
        }
    }
}

@Composable
fun PlayerActionCard(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(60.dp).clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = Color(0xFF00BCD4), modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MiniSocial(resId: Int) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        modifier = Modifier.size(24.dp).clickable { },
        contentScale = ContentScale.Fit
    )
}
