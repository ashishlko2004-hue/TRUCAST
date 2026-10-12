package com.example.trucast.ui.screens

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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.trucast.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ResetPassword(
    onBackClick: () -> Unit,
    onUpdateSuccess: () -> Unit
) {

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    // Interaction States
    var showErrors by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var currentPasswordTouched by remember { mutableStateOf(false) }
    var newPasswordTouched by remember { mutableStateOf(false) }
    var confirmPasswordTouched by remember { mutableStateOf(false) }

    // Visibility States
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmNewPasswordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Animation States
    val shakeOffset = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(1f) }

    // Professional Password Validation Logic
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$".toRegex()
    val isNewPasswordValid = newPassword.matches(passwordRegex)
    val arePasswordsMatching = newPassword == confirmNewPassword && confirmNewPassword.isNotEmpty()
    val isFormValid = isNewPasswordValid && arePasswordsMatching && currentPassword.isNotEmpty()

    val handleUpdate = {
        scope.launch {
            buttonScale.animateTo(0.95f, animationSpec = spring())
            buttonScale.animateTo(1f, animationSpec = spring())
            
            if (!isFormValid) {
                showErrors = true
                showDialog = true
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                
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
                delay(1200)
                isLoading = false
                onUpdateSuccess()
            }
        }
    }

    // --- Premium Error Dialog ---
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.88f).wrapContentHeight(),
                shape = RoundedCornerShape(28.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .background(brush = Brush.verticalGradient(listOf(Color(0xFF1A1A1A), Color(0xFF00303D))))
                        .border(BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFF00BCD4), Color.Transparent))), RoundedCornerShape(28.dp))
                        .padding(26.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier.size(64.dp).background(Color(0xFFFF5252).copy(alpha = 0.15f), CircleShape).border(1.dp, Color(0xFFFF5252).copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ReportProblem, null, tint = Color(0xFFFF5252), modifier = Modifier.size(36.dp))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Security Check", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                            if (currentPassword.isEmpty()) ResetBulletItem("Current password is required.")
                            if (newPassword.isEmpty()) ResetBulletItem("Please enter a new password.")
                            else if (!isNewPasswordValid) ResetBulletItem("New password is not strong enough.")
                            if (confirmNewPassword != newPassword) ResetBulletItem("Passwords do not match.")
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            shape = RoundedCornerShape(27.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                        ) {
                            Text("TRY AGAIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(start = 12.dp, top = 20.dp).align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = shakeOffset.value.dp)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Surface(
                modifier = Modifier.size(85.dp),
                shape = RoundedCornerShape(22.dp),
                color = Color.White.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, modifier = Modifier.size(60.dp), contentScale = ContentScale.Fit)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "TRUCAST", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 6.sp)
            Text(text = "SOLUTIONS", color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold, letterSpacing = 4.sp, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Reset Password", color = Color(0xFF00BCD4), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Update your security credentials", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)

            Spacer(modifier = Modifier.height(30.dp))

            // Current Password
            ResetInputField(
                value = currentPassword,
                onValueChange = { currentPassword = it; if(showErrors) showErrors = false },
                placeholder = "Current Password",
                icon = Icons.Default.Lock,
                isValid = currentPassword.isNotEmpty(),
                showError = (showErrors || currentPasswordTouched) && currentPassword.isEmpty(),
                onFocusLost = { currentPasswordTouched = true },
                isPassword = true,
                passwordVisible = currentPasswordVisible,
                onPasswordToggle = { currentPasswordVisible = !currentPasswordVisible },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            // New Password
            ResetInputField(
                value = newPassword,
                onValueChange = { newPassword = it; if(showErrors) showErrors = false },
                placeholder = "New Password",
                icon = Icons.Default.VpnKey,
                isValid = isNewPasswordValid,
                showError = (showErrors || (newPasswordTouched && newPassword.isNotEmpty())) && !isNewPasswordValid,
                onFocusLost = { newPasswordTouched = true },
                isPassword = true,
                passwordVisible = newPasswordVisible,
                onPasswordToggle = { newPasswordVisible = !newPasswordVisible },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            // Password Hint Card
            AnimatedVisibility(visible = newPassword.isNotEmpty() && !isNewPasswordValid) {
                Surface(
                    color = Color.Cyan.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth(0.9f)
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lightbulb, null, tint = Color.Cyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Use A-Z, a-z, 0-9 & symbols (@#$!)", color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm New Password
            ResetInputField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it; if(showErrors) showErrors = false },
                placeholder = "Confirm New Password",
                icon = Icons.Default.LockReset,
                isValid = arePasswordsMatching,
                showError = (showErrors || (confirmPasswordTouched && confirmNewPassword.isNotEmpty())) && !arePasswordsMatching,
                onFocusLost = { confirmPasswordTouched = true },
                isPassword = true,
                passwordVisible = confirmNewPasswordVisible,
                onPasswordToggle = { confirmNewPasswordVisible = !confirmNewPasswordVisible },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); handleUpdate() })
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Update Button
            Button(
                onClick = { handleUpdate() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(0.85f).height(56.dp).scale(buttonScale.value),
                shape = RoundedCornerShape(29.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormValid) Color(0xFF00BCD4) else Color(0xFF00BCD4).copy(alpha = 0.6f)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
                } else {
                    Text("UPDATE PASSWORD", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, letterSpacing = 1.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ResetBulletItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(6.dp).background(Color(0xFF00BCD4), CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ResetInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isValid: Boolean,
    showError: Boolean = false,
    onFocusLost: () -> Unit = {},
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var hasBeenFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            showError -> Color(0xFFFF5252)
            isValid && value.isNotEmpty() -> Color(0xFF4CAF50)
            else -> Color.White.copy(alpha = 0.4f)
        },
        animationSpec = tween(400)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(56.dp)
            .background(Color.Black.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .onFocusChanged { 
                if (it.isFocused) hasBeenFocused = true
                if (hasBeenFocused && !it.isFocused) onFocusLost() 
            },
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        leadingIcon = { Icon(icon, null, tint = if (showError) Color(0xFFFF5252) else if (isValid && value.isNotEmpty()) Color(0xFF4CAF50) else Color.White) },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onPasswordToggle) {
                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            } else if (isValid && value.isNotEmpty()) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
            }
        },
        isError = showError,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
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
}
