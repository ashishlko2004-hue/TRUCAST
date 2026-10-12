package com.example.trucast.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trucast.R

@Composable
fun EditProfile(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    //This is used for Hold photo which photo are come form Gallery
    var imageUri by remember{mutableStateOf<Uri?>(null)}

    // Launcher which is used for pick photo from Gallery
    val launcher = rememberLauncherForActivityResult(
        contract  = ActivityResultContracts.GetContent()
    ) {
        uri:Uri? ->
        imageUri  = uri
    }
    val cyanColor = Color(0xFF00BCD4)

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "Background Image",
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Invisible placeholder to balance the Settings icon if needed, 
                // but since we want the title centered or correctly spaced:
                Spacer(modifier = Modifier.size(48.dp)) 

                Text(
                    text = "Edit Profile",
                    color = cyanColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Profile Picture Circle Section - Reduced size to 100dp
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, cyanColor, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .clickable{launcher.launch("image/*")},
                contentAlignment = Alignment.Center

            )
            {
                if (imageUri != null) {
                    // Show Selected Image
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize() .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {

                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            setToSaturation(0f)
                        })
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Change Profile Picture",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable{launcher.launch("image/*")}
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Full Name Input Field
            EditProfileInputField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Enter your Full Name",
                icon = Icons.Default.Person,
                cyanColor = cyanColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone Number Input Field
            EditProfileInputField(
                label = "Phone",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "Enter your Phone Number",
                icon = Icons.Default.PhoneAndroid,
                cyanColor = cyanColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email ID Input Field
            EditProfileInputField(
                label = "Email ID",
                value = email,
                onValueChange = { email = it },
                placeholder = "Enter your Email Id",
                icon = Icons.Default.Email,
                cyanColor = cyanColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Address Input Field - Reduced height to 80dp
            EditProfileInputField(
                label = "Address",
                value = address,
                onValueChange = { address = it },
                placeholder = "Your Address",
                icon = Icons.Default.LocationOn,
                cyanColor = cyanColor,
                isSingleLine = false,
                modifier = Modifier.height(80.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save Changes Pill Button
            Button(
                onClick = { /* Save action logic here */ },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cyanColor,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Save Changes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun EditProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    cyanColor: Color,
    isSingleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = cyanColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.White,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(22.dp)
                )
            },
            singleLine = isSingleLine,
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                cursorColor = cyanColor,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}
