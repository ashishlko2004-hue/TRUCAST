package com.example.trucast.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trucast.R

@Composable
fun VerifyPasswordUI(
    onBackToLogin: () -> Unit,
    onVerifyClick: () -> Unit,
    onResendCode: () -> Unit
) {
    var code1 by remember { mutableStateOf("") }
    var code2 by remember { mutableStateOf("") }
    var code3 by remember { mutableStateOf("") }
    var code4 by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    val focusRequester1 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val isOtpComplete = code1.isNotEmpty() && code2.isNotEmpty() && code3.isNotEmpty() && code4.isNotEmpty()

    LaunchedEffect(Unit) {
        focusRequester1.requestFocus()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToLogin) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            }

            // Branding Section
            val brandingoffset = (-15).dp
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .offset(y = brandingoffset),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "TRUCAST",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp,
                modifier = Modifier.offset(y = brandingoffset),
            )

            Text(
                text = "SOLUTIONS",
                color = Color(0xFF00BCD4),
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp,
                fontSize = 14.sp,
                modifier = Modifier.offset(y = brandingoffset)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Verify", color = Color.Cyan, fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Please enter the code we sent to your email",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                OtpBox(
                    value = code1,
                    onValueChange = { 
                        code1 = it 
                        if (showErrors) showErrors = false
                    },
                    isError = showErrors && code1.isEmpty(),
                    modifier = Modifier.focusRequester(focusRequester1)
                )
                OtpBox(
                    value = code2,
                    onValueChange = { 
                        code2 = it 
                        if (showErrors) showErrors = false
                    },
                    isError = showErrors && code2.isEmpty()
                )
                OtpBox(
                    value = code3,
                    onValueChange = { 
                        code3 = it 
                        if (showErrors) showErrors = false
                    },
                    isError = showErrors && code3.isEmpty()
                )
                OtpBox(
                    value = code4,
                    onValueChange = { 
                        code4 = it 
                        if (showErrors) showErrors = false
                    },
                    isError = showErrors && code4.isEmpty()
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            TextButton(
                onClick = onResendCode,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Didn't receive the code?\n")
                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                            append("Resend Code")
                        }
                    },
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))

            Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                Text(
                    text = "1 of 2",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(5.dp))
                LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF00BCD4),
                    trackColor = Color.White,
                    strokeCap = StrokeCap.Butt,
                    gapSize = 0.dp,
                    drawStopIndicator = {}
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Collective Error Message (White Color)
            if (showErrors && !isOtpComplete) {
                Text(
                    text = "Please enter the complete verification code",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Button(
                onClick = {
                    showErrors = true
                    if (isOtpComplete) {
                        onVerifyClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(0.85f).height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Verify", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun OtpBox(
    value: String, 
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= 1) {
                onValueChange(newValue)
                if (newValue.isNotEmpty()) {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            } else if (newValue.length > 1) {
                val lastChar = newValue.last().toString()
                onValueChange(lastChar)
                focusManager.moveFocus(FocusDirection.Next)
            }
        },
        modifier = modifier
            .size(60.dp)
            .onPreviewKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {
                    if (value.isNotEmpty()) {
                        onValueChange("")
                    } else {
                        focusManager.moveFocus(FocusDirection.Previous)
                    }
                    true
                } else {
                    false
                }
            },
        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            errorBorderColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}
