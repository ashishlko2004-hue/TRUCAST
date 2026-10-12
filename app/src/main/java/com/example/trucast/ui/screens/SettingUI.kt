package com.example.trucast.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trucast.R

@Composable
fun SettingUI(
    onBackClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onResetPasswordClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onAboutUsClick: () -> Unit = {},
    onTermsAndConditionsClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {
    val cyanColor = Color(0xFF00BCD4)
    var notificationsEnabled by remember { mutableStateOf(true) }

    // State for Full Name editable text
    var fullNameText by remember { mutableStateOf("") }

    // State to store selected image Uri
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery Picker Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds
        )

        // Standardized Back Arrow Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 16.dp, top = 20.dp)
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
            // Header Top Bar - Adjusted to accommodate floating back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End // Shift title and icon to end
            ) {
                Text(
                    text = "Settings",
                    color = cyanColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f).padding(start = 40.dp), // Space for back button
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "Waveform",
                    tint = cyanColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // User Profile Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .border(2.dp, cyanColor, CircleShape)
                        .padding(3.dp)
                        .clip(CircleShape)
                        .clickable { galleryLauncher.launch("image/*") } // Triggers gallery picker
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                setToSaturation(0f) // Keep it Black and White
                            })
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                setToSaturation(0f) // Keep it Black and White
                            })
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Editable Name Box with dynamic placeholder text
                BasicTextField(
                    value = fullNameText,
                    onValueChange = { fullNameText = it },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        Box {
                            if (fullNameText.isEmpty()) {
                                Text(
                                    text = "Full Name",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // First Settings Group Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                SettingItemRow(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    trailingContent = {
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = cyanColor,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.DarkGray
                            )
                        )
                    }
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                SettingItemRow(
                    icon = Icons.Default.Security,
                    title = "Privacy Policy",
                    onClick = onPrivacyPolicyClick
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                SettingItemRow(
                    icon = Icons.Default.Lock,
                    title = "Reset Password",
                    onClick = onResetPasswordClick
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Second Settings Group Card containing Logout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingItemRow(
                    icon = Icons.Default.Description,
                    title = "Terms and Conditions",
                    onClick = onTermsAndConditionsClick
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                SettingItemRow(
                    icon = Icons.Default.PersonRemove,
                    title = "Delete Account",
                    onClick = onDeleteAccountClick
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                SettingItemRow(
                    icon = Icons.Default.Info,
                    title = "About us",
                    onClick = onAboutUsClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(38.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(19.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cyanColor,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SettingItemRow(
    icon: ImageVector,
    title: String,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (trailingContent != null) {
            trailingContent()
        } else if (onClick != null) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
