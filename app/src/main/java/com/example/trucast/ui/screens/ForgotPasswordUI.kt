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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordUI(
    onBackToLogin: () -> Unit,
    onSendCodeClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    
    val shakeOffset = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(1f) }
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val handleSendCode = {
        scope.launch {
            // Button Animation
            buttonScale.animateTo(0.95f, animationSpec = spring())
            buttonScale.animateTo(1f, animationSpec = spring())

            if (!isEmailValid) {
                showErrors = true
                showDialog = true
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                
                // Shake Animation
                shakeOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 400
                        (-10f) at 50
                        10f at 100
                        (-10f) at 150
                        10f at 200
                        (-5f) at 250
                        5f at 300
                    }
                )
            } else {
                isLoading = true
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                delay(1200) // Premium feel loading
                isLoading = false
                onSendCodeClick()
            }
        }
    }

    // --- Premium Glassmorphism Error Dialog ---
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
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
                            modifier = Modifier.size(64.dp).graphicsLayer { scaleX = scale; scaleY = scale }.background(Color(0xFFFF5252).copy(alpha = 0.15f), CircleShape).border(1.dp, Color(0xFFFF5252).copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MailLock, null, tint = Color(0xFFFF5252), modifier = Modifier.size(32.dp))
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Action Required", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (email.isEmpty()) "Please provide your email address to receive the reset code." else "The email format is invalid. Please verify and try again.",
                            color = Color.White.copy(alpha = 0.8f), fontSize = 15.sp, textAlign = TextAlign.Center, lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            shape = RoundedCornerShape(27.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                        ) {
                            Text("TRY AGAIN", color = Color.White, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = shakeOffset.value.dp)
                .padding(horizontal = 30.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackToLogin) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
            
            val brandingoffset = (-15).dp
            Surface(
                modifier = Modifier.size(85.dp).offset(y = brandingoffset),
                shape = RoundedCornerShape(22.dp),
                color = Color.White.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(60.dp), contentScale = ContentScale.Fit)
                }
            }

            Text(text = "TRUCAST", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 6.sp, modifier = Modifier.offset(y = brandingoffset))
            Text(text = "SOLUTIONS", color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold, letterSpacing = 4.sp, fontSize = 14.sp, modifier = Modifier.offset(y = brandingoffset))

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Forgot password", color = Color.Cyan, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Enter email address to receive\nverification code",
                color = Color.White.copy(alpha = 0.8f), fontSize = 15.sp, textAlign = TextAlign.Center, lineHeight = 20.sp, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Premium Field with interaction-based Red Border
            PremiumInputField(
                value = email,
                onValueChange = { email = it; if (showErrors) showErrors = false },
                placeholder = "Email Address",
                icon = Icons.Default.Email,
                isValid = isEmailValid,
                showError = (showErrors || (emailTouched && email.isNotEmpty())) && !isEmailValid,
                onFocusLost = { emailTouched = true },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); handleSendCode() })
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Action Button
            Button(
                onClick = { handleSendCode() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(58.dp).scale(buttonScale.value),
                shape = RoundedCornerShape(29.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF00BCD4).copy(alpha = 0.5f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
                } else {
                    Text(text = "SEND CODE", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, letterSpacing = 2.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun PremiumInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isValid: Boolean,
    showError: Boolean,
    onFocusLost: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            showError -> Color(0xFFFF5252) // Vibrant Red for wrong entry
            isValid && value.isNotEmpty() -> Color(0xFF4CAF50) // Green for Success
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
                .background(Color.Black.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                .onFocusChanged { if (!it.isFocused) onFocusLost() },
            placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f)) },
            leadingIcon = { 
                Icon(
                    icon, null, 
                    tint = if (showError) Color(0xFFFF5252) else if (isValid && value.isNotEmpty()) Color(0xFF4CAF50) else Color.White 
                ) 
            },
            trailingIcon = {
                if (isValid && value.isNotEmpty() && !showError) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                }
            },
            isError = showError,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor.copy(alpha = 0.6f),
                errorBorderColor = Color(0xFFFF5252),
                cursorColor = Color(0xFF00BCD4)
            )
        )

        AnimatedVisibility(
            visible = showError,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = if (value.isEmpty()) "Email is required" else "Invalid email format",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}
