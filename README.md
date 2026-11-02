# 🎵 TRUCAST - Modern Music Streaming & Audio Platform

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9+-purple?style=for-the-badge&logo=kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-UI-blue?style=for-the-badge&logo=jetpackcompose" />
  <img src="https://img.shields.io/badge/Retrofit-REST%20API-red?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Firebase-Auth%20%26%20Firestore-orange?style=for-the-badge&logo=firebase" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-green?style=for-the-badge" />
</p>

**TRUCAST** is a feature-rich, production-ready music streaming and audio playback application inspired by Spotify and Apple Music. Built using modern Android development best practices, it combines a gorgeous glassmorphism UI with robust backend integration, real-time REST API networking, and secure cloud storage.

---

## 🌟 Key Features

### 🎧 Immersive Music Experience
- **Dynamic Categories & Playlists**: Dedicated channels for *Wedding, Workout, Party, Chill, and Romantic* vibes.
- **Real-Time Audio Playback**: Powered by `MediaPlayer` with seamless background persistence and a persistent **Now Playing Mini-Player Bar**.
- **Vinyl Disc Animation & Equalizer**: Glowing animated spinning disc with real-time dancing equalizer waveform bars.
- **Advanced Audio Controls**: Skip next/prev, 10s fast-forward/rewind, and loop/repeat mode.

### 💖 Interactive & Smart Utilities
- **Favorites / Liked Songs**: Heart toggle for favorited tracks with status persistence across playlists.
- **Synchronized Lyrics**: Real-time karaoke-style scrolling lyrics with tap-to-seek timestamp jumping.
- **Sleep Timer**: Custom sleep timer modal to automatically stop playback after 5 to 60 minutes.
- **Song Request System**: Users can submit song requests with validation, saving them to a persistent Request List with **Admin Notification Badge Counters**.

### 👤 Profile & Cloud Sync
- **Persistent Cloud Profiles**: User profile details (Full Name, Phone, Email, Address, and custom Profile Picture) are securely synced and stored in **Cloud Firestore**.
- **GPS Location Tracking**: Integrated runtime location permission and Geocoder to automatically detect and fill user addresses.
- **Professional Settings Hub**: Spotify-like settings interface with audio streaming quality toggles, high contrast mode, and secure logout confirmation dialogs.

---

## 🛠️ Tech Stack & Architecture

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3, Animations, Lazy Grids, Bottom Sheets)
- **Architecture**: **MVVM (Model-View-ViewModel)** with Unidirectional Data Flow (UDF)
- **Networking**: **Retrofit** & **Gson Converter** for REST API integration
- **Asynchronous Programming**: **Kotlin Coroutines** & **Flow** (`suspend` functions, Dispatchers)
- **Backend & Database**: **Firebase Authentication** (Email/Password) & **Cloud Firestore**
- **Media & Image Loading**: Android `MediaPlayer` & [Coil](https://github.com/coil-kt/coil)

---

## 📂 Project Architecture

```text
com.example.trucast/
│
├── data/
│   ├── api/          # Retrofit Client & Music API Service
│   └── model/        # Data Transfer Objects (DTOs) & Models
│
└── ui/
    ├── navigation/   # Jetpack Navigation Compose NavHost & Routes
    └── screens/      # Jetpack Compose UI Screens & State Managers
        ├── ChannelsUI.kt
        ├── ChannelPlayerUI.kt
        ├── EditProfile.kt
        ├── SettingUI.kt
        ├── RequestSongUI.kt
        ├── RequestSongList.kt
        ├── MusicPlayerManager.kt
        ├── FavoriteManager.kt
        ├── LyricsManager.kt
        └── SongRequestManager.kt
```

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/ashishlko2004-hue/TRUCAST.git
   ```
2. Open the project in **Android Studio** (Koala or newer).
3. Add your `google-services.json` file into the `app/` directory (Firebase configuration).
4. Sync Gradle and run the app on an Android Emulator or physical device (minSdk 24, compileSdk 35).

---

## 👨‍💻 Author

**Ashish**  
*Android Developer*  
- GitHub: [@ashishlko2004-hue](https://github.com/ashishlko2004-hue)
