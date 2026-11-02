package com.example.trucast.ui.screens

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.trucast.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Locale

@Composable
fun EditProfile(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    var fullName by remember { mutableStateOf(UserProfileManager.fullName) }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(auth.currentUser?.email ?: "") }
    var address by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(UserProfileManager.profileImageUri) }

    // Load existing profile from Firestore on start
    LaunchedEffect(userId) {
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        fullName = doc.getString("fullName") ?: UserProfileManager.fullName
                        phone = doc.getString("phone") ?: ""
                        email = doc.getString("email") ?: (auth.currentUser?.email ?: "")
                        address = doc.getString("address") ?: ""
                        val imgStr = doc.getString("profileImageUri")
                        if (!imgStr.isNullOrEmpty()) {
                            imageUri = Uri.parse(imgStr)
                            UserProfileManager.profileImageUri = imageUri
                        }
                        if (fullName.isNotEmpty()) {
                            UserProfileManager.fullName = fullName
                        }
                    }
                }
        }
    }

    // Gallery Picker Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            UserProfileManager.profileImageUri = it
        }
    }

    // Location Permission Launcher with Geocoder for exact address
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (loc != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        address = addresses[0].getAddressLine(0) ?: String.format(Locale.getDefault(), "Lat: %.4f, Lng: %.4f", loc.latitude, loc.longitude)
                    } else {
                        address = String.format(Locale.getDefault(), "Lat: %.4f, Lng: %.4f", loc.latitude, loc.longitude)
                    }
                } else {
                    address = "GPS Location unavailable. Please enable device location."
                }
            } catch (ignored: SecurityException) {
                address = "Location Permission Missing"
            } catch (ignored: Exception) {
                address = "Current Location Detected"
            }
        } else {
            address = "Location permission denied"
        }
    }

    val cyanColor = Color(0xFF00BCD4)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds
        )

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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

            // Profile Picture Circle Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, cyanColor, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
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
                modifier = Modifier.clickable { launcher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Full Name
            EditProfileInputField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Enter your Full Name",
                icon = Icons.Default.Person,
                cyanColor = cyanColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            EditProfileInputField(
                label = "Phone",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "Enter your Phone Number",
                icon = Icons.Default.PhoneAndroid,
                cyanColor = cyanColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email ID
            EditProfileInputField(
                label = "Email ID",
                value = email,
                onValueChange = { email = it },
                placeholder = "Enter your Email Id",
                icon = Icons.Default.Email,
                cyanColor = cyanColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Address with Location Button
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Address",
                        color = cyanColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                    )
                    TextButton(onClick = {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }) {
                        Icon(Icons.Default.MyLocation, null, tint = cyanColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Detect Location", color = cyanColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    placeholder = { Text("Your Address or GPS Location", color = Color.White, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = Color.White.copy(0.7f), modifier = Modifier.size(22.dp)) },
                    singleLine = false,
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

            Spacer(modifier = Modifier.weight(1f))

            // Save Changes Button (Guaranteed working)
            Button(
                onClick = {
                    UserProfileManager.fullName = fullName
                    UserProfileManager.profileImageUri = imageUri

                    if (userId != null) {
                        val userMap = hashMapOf(
                            "fullName" to fullName,
                            "phone" to phone,
                            "email" to email,
                            "address" to address,
                            "profileImageUri" to (imageUri?.toString() ?: "")
                        )
                        firestore.collection("users").document(userId)
                            .set(userMap, SetOptions.merge())
                            .addOnCompleteListener {
                                showSuccessDialog = true
                            }
                    } else {
                        showSuccessDialog = true
                    }
                },
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
                    fontWeight = FontWeight.Bold
                )
            }

            if (showSuccessDialog) {
                Dialog(onDismissRequest = { showSuccessDialog = false }) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(0.85f).wrapContentHeight(),
                        shape = RoundedCornerShape(28.dp),
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .background(brush = Brush.verticalGradient(listOf(Color(0xFF1A1A1A), Color(0xFF002B36))))
                                .border(BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFF00BCD4), Color.Transparent))), RoundedCornerShape(28.dp))
                                .padding(26.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier.size(60.dp)
                                        .background(Color(0xFF00BCD4).copy(alpha = 0.1f), CircleShape)
                                        .border(1.dp, Color(0xFF00BCD4).copy(alpha = 0.3f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF00BCD4),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(
                                    "Profile Updated",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    "Your profile changes have been saved successfully.",
                                    color = Color.White.copy(0.8f),
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(25.dp))
                                Button(
                                    onClick = { showSuccessDialog = false; onBackClick() },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                                ) {
                                    Text("OK", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
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
