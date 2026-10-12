package com.example.trucast.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trucast.R

@Composable
fun ChannelPlayerUI(
    channelName: String = " Channel name",
    userName: String = " Name",
    onBackClick: () -> Unit = {},
    onRequestSongClick: () -> Unit = {}
) {
    val cyanColor = Color(0xFF00BCD4)
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        // Standardized Back Arrow Button - Moved further left (0dp)
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
            // Spacer to match top bar vertical alignment
            Spacer(modifier = Modifier.height(20.dp))

            // 1. Top Bar (Profile + Name & vector_image Icon)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp), // Space for back button
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
                            setToSaturation(0f) // Makes it Black and White
                        })
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = userName, color = Color.White, fontSize = 14.sp)
                }
                
                // vector_image icon at top right
                Image(
                    painter = painterResource(id = R.drawable.vector_image),
                    contentDescription = "Vector Logo",
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // 2. Channel Name
            Text(
                text = channelName,
                color = cyanColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(50.dp))

            // 3. Central Player Circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(280.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.4f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    drawArc(
                        color = Color.White,
                        startAngle = 270f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.image14),
                    contentDescription = "Artist",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .clip(CircleShape)
                        .border(
                            4.dp,
                            Brush.linearGradient(
                                listOf(
                                    cyanColor,
                                    Color.Magenta
                                )
                            ), CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )

                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(10.dp)
                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (-7.5).dp)
                        .background(Color.White, CircleShape))
                    
                    Box(modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = (-7.5).dp)
                        .background(Color.White, CircleShape))
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // 4. Action Buttons (Request Song & My song List)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PlayerActionButton(
                    text = "Request Song",
                    icon = Icons.Default.MusicNote,
                    cyanColor = cyanColor,
                    onClick = onRequestSongClick
                )
                PlayerActionButton(
                    text = "My song List",
                    icon = Icons.AutoMirrored.Filled.QueueMusic,
                    cyanColor = cyanColor,
                    onClick = { /* Handle My Song List */ }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 5. Social Media Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialIcon(
                    containerColor = Color(0xFFFF0000),
                    content = {
                        Box(
                            modifier = Modifier
                                .size(24.dp, 16.dp)
                                .background(Color.White, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color(0xFFFF0000),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                )

                SocialIcon(
                    content = {
                        Image(painter = painterResource(id = R.drawable.insta_icon),
                            contentDescription = null,
                            modifier = Modifier.size(44.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                    }
                )

                SocialIcon(
                    icon = Icons.Default.Facebook,
                    containerColor = Color(0xFF1877F2)
                )

                SocialIcon(
                    content = {
                        Image(painter = painterResource(id = R.drawable.snapchat_icon),
                            contentDescription = null,
                            modifier = Modifier.size(44.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                    }
                )

                SocialIcon(
                    icon = Icons.Default.Close,
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            }
        }
    }
}

@Composable
fun PlayerActionButton(
    text: String,
    icon: ImageVector,
    cyanColor: Color,
    onClick: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .height(40.dp)
                .width(130.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = text, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SocialIcon(
    icon: ImageVector? = null,
    containerColor: Color = Color.Transparent,
    contentColor: Color = Color.White,
    brush: Brush? = null,
    content: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .then(
                if (brush != null) Modifier.background(brush)
                else Modifier.background(containerColor)
            )
            .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (content != null) {
            content()
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
