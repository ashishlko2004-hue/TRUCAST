package com.example.trucast.ui.screens

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trucast.R
import com.example.trucast.ui.theme.TRUCASTTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreenUI(onTimeout: () -> Unit) {
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
        delay(3000)
        onTimeout()
    }
}
@Preview
@Composable
 fun SplashScreenUIPreview() {
    TRUCASTTheme() {
        SplashScreenUI(onTimeout = {})
    }
}
