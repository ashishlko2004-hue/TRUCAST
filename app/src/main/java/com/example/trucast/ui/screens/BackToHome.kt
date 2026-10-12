package com.example.trucast.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trucast.R

@Composable
fun BackToHome(
    onBackToHome: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds
        )

        // Standardized Back Arrow Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 16.dp, top = 20.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            // Branding Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "TRUCAST",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp,
            )

            Text(
                text = "SOLUTIONS",
                color = Color(0xFF00BCD4),
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp,
                fontSize = 18.sp,
            )

            // Very small spacer to keep it right below SOLUTIONS
            Spacer(modifier = Modifier.height(20.dp))

            // backtohome image - Small size and positioned right below text
            Image(
                painter = painterResource(id = R.drawable.backtohome),
                contentDescription = "Back to Home",
                modifier = Modifier
                    .size(140.dp)
                    .clickable { onBackToHome() },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = " Password reset", 
                color = Color.Cyan,  
                fontSize = 20.sp  
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = " Your Password has been reset successfully ",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(80.dp))
            Button(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = " Back to Home ",
                    color  =Color.Black,
                    fontSize = 18.sp,

                )
            }
        }
    }
}
