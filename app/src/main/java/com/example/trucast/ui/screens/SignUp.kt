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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.trucast.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUp(onBackToLogin: () -> Unit,
           onSignUpSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isAgreed by remember { mutableStateOf(false) }
    
    // Interaction & Validation States
    var showErrors by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var usernameTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var confirmPasswordTouched by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Animation States
    val shakeOffset = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(1f) }

    // Validation Criteria
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$".toRegex()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.matches(passwordRegex)
    val isConfirmPasswordValid = confirmPassword == password && confirmPassword.isNotEmpty()
    val isUsernameValid = username.trim().isNotEmpty()

    val isFormValid = isEmailValid && isPasswordValid && isConfirmPasswordValid && isUsernameValid && isAgreed

    val handleSignUp = {
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
                onSignUpSuccess()
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
                        Text(text = "Registration Issues", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                            if (!isUsernameValid) SignUpBulletPoint("Username is required.")
                            if (!isEmailValid) SignUpBulletPoint("Enter a valid Email Address.")
                            if (!isPasswordValid) SignUpBulletPoint("Use a strong password structure.")
                            if (confirmPassword != password) SignUpBulletPoint("Passwords do not match.")
                            if (!isAgreed) SignUpBulletPoint("Please agree to the Policy.")
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            shape = RoundedCornerShape(27.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                        ) {
                            Text("TRY AGAIN", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
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
            contentDescription = "Sign Up Background",
            contentScale = ContentScale.FillBounds
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f)))

        IconButton(
            onClick = onBackToLogin,
            modifier = Modifier.padding(start = 12.dp, top = 20.dp).align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
        }

        // Fixed Column (No Scroll for Professional look on single page)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = shakeOffset.value.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Branding Area - Compact to fit one page
            Surface(
                modifier = Modifier.size(75.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, modifier = Modifier.size(55.dp), contentScale = ContentScale.Fit)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = "TRUCAST", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 5.sp)   
            Text(text = "SOLUTIONS", color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold, letterSpacing = 3.sp, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(25.dp))

            Text("Create Account", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            
            Spacer(modifier = Modifier.height(15.dp))

            // Professional Input Fields with real-time feedback
            SignUpInputField(
                value = username,
                onValueChange = { username = it; if(showErrors) showErrors = false },
                placeholder = "Username",
                icon = Icons.Default.Person,
                isValid = isUsernameValid,
                showError = (showErrors || usernameTouched) && !isUsernameValid,
                onFocusLost = { usernameTouched = true },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(10.dp))

            SignUpInputField(
                value = email,
                onValueChange = { email = it; if(showErrors) showErrors = false },
                placeholder = "Email Address",
                icon = Icons.Default.Email,
                isValid = isEmailValid,
                showError = (showErrors || emailTouched) && !isEmailValid,
                onFocusLost = { emailTouched = true },
                keyboardType = KeyboardType.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(10.dp))

            SignUpInputField(
                value = password,
                onValueChange = { password = it; if(showErrors) showErrors = false },
                placeholder = "Password",
                icon = Icons.Default.Lock,
                isValid = isPasswordValid,
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                showError = (showErrors || passwordTouched) && !isPasswordValid,
                onFocusLost = { passwordTouched = true },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            // Attractive Password Hint Card (Matches LoginPageUI)
            AnimatedVisibility(visible = password.isNotEmpty() && !isPasswordValid) {
                Surface(
                    color = Color.Cyan.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth(0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lightbulb, null, tint = Color.Cyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Include: A-Z, a-z, 0-9 & symbols (@#$!)",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            SignUpInputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; if(showErrors) showErrors = false },
                placeholder = "Confirm Password",
                icon = Icons.Default.Lock, // Updated Icon
                isValid = isConfirmPasswordValid,
                isPassword = true,
                passwordVisible = confirmPasswordVisible,
                onPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                showError = (showErrors || confirmPasswordTouched) && !isConfirmPasswordValid,
                onFocusLost = { confirmPasswordTouched = true },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); handleSignUp() })
            )

            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(0.9f), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isAgreed,
                    onCheckedChange = { isAgreed = it; if(showErrors) showErrors = false },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF00BCD4), uncheckedColor = Color.White)
                )
                Text(
                    text = buildAnnotatedString {
                        append("I Agree with ")
                        withStyle(SpanStyle(color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold)) { append("Privacy") }
                        append(" & ")
                        withStyle(SpanStyle(color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold)) { append("Policy") }
                    },
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Main Action Button
            Button(
                onClick = { handleSignUp() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(0.85f).height(56.dp).scale(buttonScale.value),
                shape = RoundedCornerShape(29.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xFF00BCD4) else Color(0xFF00BCD4).copy(alpha = 0.6f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
                } else {
                    Text("SIGN UP", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, letterSpacing = 2.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    append("Already a member? ")
                    withStyle(SpanStyle(color = Color(0xFF00BCD4), fontWeight = FontWeight.ExtraBold)) { append("Log In") }
                },
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onBackToLogin() }
            )
        }
    }
}

@Composable
fun SignUpBulletPoint(text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(6.dp).background(Color(0xFF00BCD4), CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SignUpInputField(
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
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var hasBeenFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            showError -> Color(0xFFFF5252) // Real-time Red
            isValid && value.isNotEmpty() -> Color(0xFF4CAF50) // Green success
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
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp) },
        leadingIcon = { 
            Icon(
                icon, null, 
                tint = if (showError) Color(0xFFFF5252) else if (isValid && value.isNotEmpty()) Color(0xFF4CAF50) else Color.White 
            ) 
        },
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
