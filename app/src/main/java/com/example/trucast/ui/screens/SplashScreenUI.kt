package com.example.trucast.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.trucast.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreenUI(onTimeout: (Boolean) -> Unit) {
    // Boolean pass karenge: true = logged in, false = not logged in
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.firstbackground),
            contentDescription = "Splash Screen",
            contentScale = ContentScale.FillBounds
        )
    }

    LaunchedEffect(Unit) {
        delay(2500) // 2.5 seconds wait
        val currentUser = FirebaseAuth.getInstance().currentUser
        onTimeout(currentUser != null)

    }
}
