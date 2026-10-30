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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    
    var showErrors by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val haptic = LocalHapticFeedback.current

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val isPasswordValid = password.length >= 6
    val isFormValid = isEmailValid && isPasswordValid && confirmPassword == password && username.isNotEmpty() && isAgreed

    val handleSignUp = {
        scope.launch {
            if (!isFormValid) {
                showErrors = true
                errorMessage = "Please fill all fields correctly."
                showDialog = true
            } else {
                isLoading = true
                // Trim email to avoid malformed errors
                auth.createUserWithEmailAndPassword(email.trim(), password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val userMap = hashMapOf("username" to username, "email" to email.trim())
                            if (userId != null) {
                                firestore.collection("users").document(userId).set(userMap)
                                    .addOnSuccessListener { isLoading = false; onSignUpSuccess() }
                                    .addOnFailureListener { e -> isLoading = false; errorMessage = e.localizedMessage; showDialog = true }
                            }
                        } else {
                            isLoading = false
                            errorMessage = task.exception?.localizedMessage
                            showDialog = true
                        }
                    }
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(modifier = Modifier.fillMaxWidth(0.88f).wrapContentHeight(), shape = RoundedCornerShape(28.dp), color = Color(0xFF1A1A1A)) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Error, null, tint = Color.Red, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("SignUp Error", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage ?: "Unknown Error", color = Color.White.copy(0.7f), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { showDialog = false }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))) {
                        Text("OK")
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.loginpage), modifier = Modifier.fillMaxSize(), contentDescription = null, contentScale = ContentScale.FillBounds)
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

        Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Create Account", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(30.dp))
            
            SignUpInputField(value = username, onValueChange = { username = it }, placeholder = "Username", icon = Icons.Default.Person)
            Spacer(modifier = Modifier.height(12.dp))
            SignUpInputField(value = email, onValueChange = { email = it }, placeholder = "Email Address", icon = Icons.Default.Email)
            Spacer(modifier = Modifier.height(12.dp))
            SignUpInputField(value = password, onValueChange = { password = it }, placeholder = "Password", icon = Icons.Default.Lock, isPassword = true, passwordVisible = passwordVisible, onPasswordToggle = { passwordVisible = !passwordVisible })
            Spacer(modifier = Modifier.height(12.dp))
            SignUpInputField(value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Confirm Password", icon = Icons.Default.Lock, isPassword = true, passwordVisible = confirmPasswordVisible, onPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible })
            
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isAgreed, onCheckedChange = { isAgreed = it }, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF00BCD4), uncheckedColor = Color.White))
                Text("I agree to terms", color = Color.White, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = { handleSignUp() }, enabled = !isLoading, modifier = Modifier.fillMaxWidth(0.9f).height(56.dp), shape = RoundedCornerShape(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("SIGN UP", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("Already member? Log In", color = Color.White, modifier = Modifier.clickable { onBackToLogin() })
        }
    }
}

@Composable
fun SignUpInputField(value: String, onValueChange: (String) -> Unit, placeholder: String, icon: ImageVector, isPassword: Boolean = false, passwordVisible: Boolean = false, onPasswordToggle: () -> Unit = {}) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(0.9f).height(56.dp),
        placeholder = { Text(placeholder, color = Color.White.copy(0.5f)) },
        leadingIcon = { Icon(icon, null, tint = Color.White) },
        trailingIcon = { if (isPassword) IconButton(onClick = onPasswordToggle) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.White) } },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(12.dp), singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFF00BCD4), unfocusedBorderColor = Color.White.copy(0.5f))
    )
}
