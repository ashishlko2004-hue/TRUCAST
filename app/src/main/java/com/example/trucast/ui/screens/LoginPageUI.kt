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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.compose.ui.window.DialogProperties

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
    
    // Validation States
    var showErrors by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val shakeOffset = remember { Animatable(0f) }

    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$".toRegex()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.matches(passwordRegex)
    val isFormValid = isEmailValid && isPasswordValid

    LaunchedEffect(showErrors) {
        if (showErrors && !isFormValid) {
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
                    modifier = Modifier
                        .background(brush = Brush.verticalGradient(listOf(Color(0xFF1A1A1A), Color(0xFF003D4D))))
                        .border(BorderStroke(1.dp, Color(0xFF00BCD4).copy(alpha = 0.5f)), RoundedCornerShape(24.dp))
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier.size(64.dp).background(Color(0xFFFF5252).copy(alpha = 0.1f), CircleShape).border(1.dp, Color(0xFFFF5252).copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.GppBad, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(36.dp))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Login Failed", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        val errorMsg = when {
                            email.isEmpty() && password.isEmpty() -> "Email and Password are required."
                            email.isEmpty() || !isEmailValid -> "Please enter a valid Email."
                            password.isEmpty() || !isPasswordValid -> "Please enter a strong Password."
                            else -> "Something went wrong."
                        }
                        Text(text = errorMsg, color = Color.White.copy(alpha = 0.8f), fontSize = 15.sp, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                        ) {
                            Text(text = "Try Again", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.loginpage), modifier = Modifier.fillMaxSize(), contentDescription = null, contentScale = ContentScale.FillBounds)
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)))

        IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 12.dp, top = 20.dp).align(Alignment.TopStart)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
        }

        Column(
            modifier = Modifier.fillMaxSize().offset(x = shakeOffset.value.dp).padding(horizontal = 24.dp).verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Surface(modifier = Modifier.size(80.dp), shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.15f)) {
                Box(contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(60.dp), contentScale = ContentScale.Fit)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "TRUCAST", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 6.sp)
            Text(text = "SOLUTIONS", color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold, letterSpacing = 4.sp, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(30.dp))
            Text("Log in to Your Account", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            LoginValidationField(
                value = email,
                onValueChange = { email = it; if(showErrors) showErrors = false },
                placeholder = "Email Address",
                icon = Icons.Default.Email,
                isValid = isEmailValid,
                showError = (showErrors || (emailTouched && email.isNotEmpty())) && !isEmailValid,
                onFocusLost = { emailTouched = true },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password Field
            LoginValidationField(
                value = password,
                onValueChange = { password = it; if(showErrors) showErrors = false },
                placeholder = "Password",
                icon = Icons.Default.Lock,
                isValid = isPasswordValid,
                showError = (showErrors || (passwordTouched && password.isNotEmpty())) && !isPasswordValid,
                onFocusLost = { passwordTouched = true },
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { 
                    focusManager.clearFocus()
                    if (!isFormValid) { showErrors = true; showDialog = true } else { onLoginClick() }
                })
            )

            // Password tip card - Compact
            AnimatedVisibility(visible = (password.isNotEmpty() && !isPasswordValid)) {
                Box(modifier = Modifier.fillMaxWidth(0.85f).padding(top = 8.dp).background(Color.Cyan.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(6.dp)) {
                    Text(text = "Tip: Include A-Z, a-z, 0-9 & @#$!", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, modifier = Modifier.align(Alignment.Center))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it }, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4), uncheckedColor = Color.White))
                Text("Remember me", color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Forgot?", color = Color(0xFF00BCD4), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onForgotPasswordClick() })
            }

            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = { if (!isFormValid) { showErrors = true; showDialog = true } else { onLoginClick() } },
                modifier = Modifier.fillMaxWidth(0.80f).height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("LOG IN", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = buildAnnotatedString { append("Don't have an account? "); withStyle(SpanStyle(color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold)) { append("Sign Up") } }, color = Color.White, fontSize = 14.sp, modifier = Modifier.clickable { onSignUpClick() })
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun LoginValidationField(
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
    val borderColor by animateColorAsState(
        targetValue = when {
            showError -> Color(0xFFFF5252) // Red for wrong entry
            isValid && value.isNotEmpty() -> Color(0xFF4CAF50) // Green for correct entry
            else -> Color.White.copy(alpha = 0.6f)
        },
        animationSpec = tween(400)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(0.85f).height(56.dp)
            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .onFocusChanged { if (!it.isFocused) onFocusLost() },
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp) },
        leadingIcon = { Icon(icon, null, tint = if (showError) Color(0xFFFF5252) else if (isValid && value.isNotEmpty()) Color(0xFF4CAF50) else Color.White, modifier = Modifier.size(20.dp)) },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onPasswordToggle) {
                    Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            } else if (isValid && value.isNotEmpty()) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
            }
        },
        isError = showError,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
            focusedBorderColor = borderColor, unfocusedBorderColor = borderColor.copy(alpha = 0.5f),
            errorBorderColor = Color(0xFFFF5252), cursorColor = Color(0xFF00BCD4)
        )
    )
}
