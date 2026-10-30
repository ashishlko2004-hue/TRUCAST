package com.example.trucast.ui.screens

import android.util.Patterns
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.trucast.R
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

@Composable
fun LoginPageUI(
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    var showErrors by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var firebaseError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val auth = remember { FirebaseAuth.getInstance() }

    val shakeOffset = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(1f) }

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val isPasswordValid = password.length >= 6

    val handleLogin = {
        scope.launch {
            buttonScale.animateTo(0.92f, animationSpec = tween(100))
            buttonScale.animateTo(
                1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )

            if (!isEmailValid || password.isEmpty()) {
                showErrors = true
                firebaseError = "Please enter a valid email and password."
                showDialog = true
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                shakeOffset.animateTo(
                    0f,
                    keyframes {
                        durationMillis =
                            400; -10f at 50; 10f at 100; -10f at 150; 10f at 200; -5f at 250; 5f at 300
                    })
            } else {
                isLoading = true
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                auth.signInWithEmailAndPassword(email.trim(), password)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            onLoginClick()
                        } else {
                            val exception = task.exception
                            firebaseError = when (exception) {
                                is FirebaseAuthInvalidUserException -> "Account does not exist. Please Sign Up first."
                                is FirebaseAuthInvalidCredentialsException -> "Wrong password or bad email format."
                                else -> exception?.localizedMessage ?: "Login failed"
                            }
                            showDialog = true
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.85f).wrapContentHeight(),
                shape = RoundedCornerShape(28.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1A1A1A),
                                Color(0xFF003D4D)
                            )
                        )
                    ).border(
                        BorderStroke(1.dp, Color(0xFF00BCD4).copy(alpha = 0.5f)),
                        RoundedCornerShape(28.dp)
                    ).padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.size(60.dp)
                                .background(Color(0xFFFF5252).copy(alpha = 0.1f), CircleShape)
                                .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GppBad,
                                contentDescription = null,
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            "Login Failed",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            firebaseError ?: "Error occurred.",
                            color = Color.White.copy(0.7f),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                        ) {
                            Text("Try Again", fontWeight = FontWeight.Bold, color = Color.White)
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
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(start = 12.dp, top = 20.dp).align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
        }

        Column(
            modifier = Modifier.fillMaxSize().offset(x = shakeOffset.value.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Surface(
                modifier = Modifier.size(70.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        "Logo",
                        modifier = Modifier.size(50.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "TRUCAST",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 5.sp
            )
            Text(
                "SOLUTIONS",
                color = Color(0xFF00BCD4),
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                "Welcome Back",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "Sign in to your account",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            LoginValidationField(
                value = email,
                onValueChange = {
                    email = it; if (showErrors) showErrors = false; firebaseError = null
                },
                placeholder = "Email Address",
                icon = Icons.Default.Email,
                isValid = isEmailValid,
                showError = (showErrors || (emailTouched && email.isNotEmpty())) && !isEmailValid,
                onFocusLost = { emailTouched = true },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            LoginValidationField(
                value = password,
                onValueChange = {
                    password = it; if (showErrors) showErrors = false; firebaseError = null
                },
                placeholder = "Password",
                icon = Icons.Default.Lock,
                isValid = isPasswordValid,
                showError = (showErrors || (passwordTouched && password.isNotEmpty())) && !isPasswordValid,
                onFocusLost = { passwordTouched = true },
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); handleLogin() })
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF00BCD4),
                        uncheckedColor = Color.White
                    )
                )
                Text("Remember me", color = Color.White, fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Forgot Password?",
                    color = Color(0xFF00BCD4),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onForgotPasswordClick() })
            }

            Spacer(modifier = Modifier.weight(0.5f))

            Button(
                onClick = { handleLogin() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(0.85f).height(54.dp).scale(buttonScale.value),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF00BCD4),
                                Color(0xFF00838F)
                            )
                        ), RoundedCornerShape(28.dp)
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp
                    )
                    else Text(
                        "LOG IN",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )
                Text("  Social Login  ", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                SocialIconButton(
                    iconRes = R.drawable.google5,
                    label = "Google",
                    borderBrush = Brush.sweepGradient(
                        listOf(
                            Color(0xFFEA4335),
                            Color(0xFFFBBC05),
                            Color(0xFF34A853),
                            Color(0xFF4285F4)
                        )
                    ),
                    bgColor = Color.White.copy(alpha = 0.12f)
                )
                SocialIconButton(
                    iconRes = R.drawable.facebook4,
                    label = "Facebook",
                    borderBrush = Brush.linearGradient(
                        listOf(
                            Color(0xFF1877F2),
                            Color(0xFF00BCD4)
                        )
                    ),
                    bgColor = Color(0xFF1877F2).copy(alpha = 0.15f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                buildAnnotatedString {
                    append("New member? "); withStyle(
                    SpanStyle(
                        color = Color(
                            0xFF00BCD4
                        ), fontWeight = FontWeight.Bold
                    )
                ) { append("Sign Up") }
                },
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSignUpClick() })
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun SocialIconButton(iconRes: Int, label: String, borderBrush: Brush, bgColor: Color) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.88f else 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))

    Surface(
        modifier = Modifier.size(56.dp).scale(scale).pointerInput(Unit) {
            detectTapGestures(onPress = { isPressed = true; haptic.performHapticFeedback(HapticFeedbackType.LongPress); tryAwaitRelease(); isPressed = false })
        },
        shape = CircleShape, color = bgColor, border = BorderStroke(2.dp, borderBrush)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = iconRes), contentDescription = label, modifier = Modifier.size(28.dp), contentScale = ContentScale.Fit)
        }
    }
}

@Composable

fun LoginValidationField(
    value: String, onValueChange: (String) -> Unit, placeholder: String, icon: ImageVector, isValid: Boolean, showError: Boolean = false,
    onFocusLost: () -> Unit = {}, isPassword: Boolean = false, passwordVisible: Boolean = false, onPasswordToggle: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default, keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val borderColor by animateColorAsState(targetValue = if (showError) Color.Red else if (isValid && value.isNotEmpty()) Color.Green else Color.White.copy(0.6f))
    OutlinedTextField(

        value = value, onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(0.85f).height(54.dp).background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp)).onFocusChanged { if (!it.isFocused) onFocusLost() },
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp) },
        leadingIcon = { Icon(icon, null, tint = if (showError) Color.Red else if (isValid && value.isNotEmpty()) Color.Green else Color.White, modifier = Modifier.size(20.dp)) },
        trailingIcon = { if (isPassword) IconButton(onClick = onPasswordToggle) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.White, modifier = Modifier.size(20.dp)) } },
        isError = showError, visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions, keyboardActions = keyboardActions, shape = RoundedCornerShape(12.dp), singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = borderColor, unfocusedBorderColor = borderColor.copy(alpha = 0.5f), errorBorderColor = Color.Red, cursorColor = Color(0xFF00BCD4))
    )
}
