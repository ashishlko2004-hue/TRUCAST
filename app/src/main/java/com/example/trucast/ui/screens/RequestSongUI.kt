package com.example.trucast.ui.screens

import android.util.Patterns
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.trucast.R

@Composable
fun RequestSongUI(
    userName: String = "Music Lover",
    onBackClick: () -> Unit = {},
    onSubmitRequest: (String, String) -> Unit = { _, _ -> }
) {
    val cyanColor = Color(0xFF00BCD4)
    var songName by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    
    // Validation & Interaction States
    var showErrors by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var songNameTouched by remember { mutableStateOf(false) }
    var contactInfoTouched by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(contactInfo).matches()
    val isSongNameValid = songName.trim().isNotEmpty()
    val isFormValid = isEmailValid && isSongNameValid

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)))

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 0.dp, top = 20.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp).clip(CircleShape).border(1.dp, cyanColor, CircleShape),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = userName, color = Color.White, fontSize = 14.sp)
                }
                Image(
                    painter = painterResource(id = R.drawable.vector_image),
                    contentDescription = "Vector Logo",
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Request Song", color = cyanColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(220.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image14),
                    contentDescription = "Song Illustration",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Song Name Input
            RequestInputField(
                value = songName,
                onValueChange = { songName = it; if(showErrors) showErrors = false },
                placeholder = "Enter the Song Name",
                icon = Icons.Default.MusicNote,
                isValid = isSongNameValid,
                showError = (showErrors || (songNameTouched && songName.isEmpty())) && !isSongNameValid,
                onFocusLost = { songNameTouched = true },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Input
            RequestInputField(
                value = contactInfo,
                onValueChange = { contactInfo = it; if(showErrors) showErrors = false },
                placeholder = "Enter Email address",
                icon = Icons.Default.Email,
                isValid = isEmailValid,
                showError = (showErrors || (contactInfoTouched && contactInfo.isNotEmpty())) && !isEmailValid,
                onFocusLost = { contactInfoTouched = true },
                keyboardType = KeyboardType.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (showErrors && !isFormValid) {
                Text(
                    text = "Please fill in all details correctly",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            // Submit Request Button
            Button(
                onClick = { 
                    showErrors = true
                    if (isFormValid) {
                        SongRequestManager.addRequest(songName, contactInfo)
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cyanColor,
                    disabledContainerColor = cyanColor.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(text = "Submit Request", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Success Notification Dialog
        if (showSuccessDialog) {
            Dialog(
                onDismissRequest = { showSuccessDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(0.85f).wrapContentHeight(),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .background(brush = Brush.verticalGradient(listOf(Color(0xFF1A1A1A), Color(0xFF002B36))))
                            .border(BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFF00BCD4), Color.Transparent))), RoundedCornerShape(28.dp))
                            .padding(26.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                            val scale by infiniteTransition.animateFloat(
                                initialValue = 1f, targetValue = 1.15f,
                                animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "scale"
                            )

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .graphicsLayer { scaleX = scale; scaleY = scale }
                                    .background(Color(0xFF00BCD4).copy(alpha = 0.15f), CircleShape)
                                    .border(1.dp, Color(0xFF00BCD4).copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF00BCD4), modifier = Modifier.size(32.dp))
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Request Sent & Notified!",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Your song request for \"$songName\" has been successfully saved in the Request Song List. Notification received!",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { showSuccessDialog = false; onBackClick() },
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f))
                                ) {
                                    Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = {
                                        showSuccessDialog = false
                                        onSubmitRequest(songName, contactInfo)
                                    },
                                    modifier = Modifier.weight(1.2f).height(50.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                                ) {
                                    Text("VIEW LIST", color = Color.Black, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RequestInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isValid: Boolean,
    showError: Boolean = false,
    onFocusLost: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            showError -> Color(0xFFFF5252)
            isValid && value.isNotEmpty() -> Color(0xFF4CAF50)
            else -> Color.White.copy(alpha = 0.4f)
        },
        animationSpec = tween(400)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .onFocusChanged { if (!it.isFocused) onFocusLost() },
            placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp) },
            leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = if (showError) Color(0xFFFF5252) else if (isValid && value.isNotEmpty()) Color(0xFF4CAF50) else Color.White) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            isError = showError,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                errorBorderColor = Color(0xFFFF5252),
                cursorColor = Color(0xFF00BCD4),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}
