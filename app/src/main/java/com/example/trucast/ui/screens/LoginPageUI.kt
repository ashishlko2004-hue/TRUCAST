package com.example.trucast.ui.screens

import android.content.Context
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.trucast.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPageUI(
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("TrucastPrefs", Context.MODE_PRIVATE) }

    var email by remember { mutableStateOf(sharedPreferences.getString("saved_email", "") ?: "") }
    var password by remember { mutableStateOf(sharedPreferences.getString("saved_password", "") ?: "") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(sharedPreferences.getBoolean("remember_me", false)) }

    val focusManager = LocalFocusManager.current
    var isLoggingIn by remember { mutableStateOf(false) }

    // Dialog state for errors
    var showDialog by remember { mutableStateOf(false) }
    var firebaseError by remember { mutableStateOf<String?>(null) }

    // Auto-Login logic if user is already authenticated
    LaunchedEffect(Unit) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            onLoginClick()
        }
    }

    fun handleLogin() {
        if (email.isBlank() || password.isBlank()) {
            firebaseError = "Please enter both Email and Password."
            showDialog = true
            return
        }

        isLoggingIn = true
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                isLoggingIn = false
                if (task.isSuccessful) {
                    // Save credentials if Remember Me is checked
                    val editor = sharedPreferences.edit()
                    if (rememberMe) {
                        editor.putString("saved_email", email.trim())
                        editor.putString("saved_password", password)
                        editor.putBoolean("remember_me", true)
                    } else {
                        editor.clear()
                    }
                    editor.apply()

                    onLoginClick()
                } else {
                    firebaseError = task.exception?.localizedMessage ?: "Authentication failed."
                    showDialog = true
                }
            }
    }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Welcome Back",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign in to continue to Trucast",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email", color = Color.White.copy(0.7f)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color.White.copy(0.7f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00BCD4),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF00BCD4)
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password", color = Color.White.copy(0.7f)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color.White.copy(0.7f)
                    )
                },
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Toggle Password Visibility",
                            tint = Color.White.copy(0.7f)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00BCD4),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF00BCD4)
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); handleLogin() })
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF00BCD4),
                            uncheckedColor = Color.White.copy(0.7f),
                            checkmarkColor = Color.Black
                        )
                    )
                    Text(
                        text = "Remember me",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "Forgot password?",
                    color = Color(0xFF00BCD4),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onForgotPasswordClick() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    handleLogin()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4),
                    contentColor = Color.Black
                ),
                enabled = !isLoggingIn
            ) {
                if (isLoggingIn) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "Log In",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign Up",
                    color = Color(0xFF00BCD4),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}
