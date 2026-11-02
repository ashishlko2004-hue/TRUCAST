package com.example.trucast.ui.screens

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object UserProfileManager {
    var profileImageUri by mutableStateOf<Uri?>(null)
    var fullName by mutableStateOf("Music Lover")
}
