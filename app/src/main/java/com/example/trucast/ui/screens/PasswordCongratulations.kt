package com.example.trucast.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.trucast.R
import kotlinx.coroutines.delay

@Composable
fun PasswordCongratulationsUI(onContinueClick: () -> Unit) {
    // Automatically redirect to login screen after 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        onContinueClick()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // The background image already contains all the text and design,
        // so we don't need any separate Text or Column composables.
        Image(
            painter = painterResource(id = R.drawable.forgotcongratulations),
            contentDescription = "Congratulations",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}
