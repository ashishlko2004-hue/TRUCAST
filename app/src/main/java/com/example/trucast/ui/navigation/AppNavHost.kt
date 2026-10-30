package com.example.trucast.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trucast.ui.screens.*

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot_password"
    const val PASSWORD_CONGRATULATIONS = "password_congratulations"
    const val SIGNUP = "sign_up"
    const val EDIT_PROFILE = "edit_profile"
    const val SETTING = "setting"
    const val CHANNELS = "channels"
    const val CHANNEL_PLAYER = "channel_player"
    const val REQUEST_SONG = "request_song"
    const val REQUEST_SONG_LIST = "request_song_list"
}

object PasswordResetState {
    var email: String = ""
    var otpCode: String = "1234"
    var resetCode: String = ""
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {
        // 1. Splash Screen - Handle Auto-Login
        composable(Routes.SPLASH) {
            SplashScreenUI(onTimeout = { isLoggedIn ->
                if (isLoggedIn) {
                    navController.navigate(Routes.CHANNELS) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                } else {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            })
        }
        // 2. Welcome Landing
        composable(Routes.HOME) {
            MainScreenUI(onGetStartedClick = {
                navController.navigate(Routes.LOGIN)
            })
        }
        // 3. Authentication
        composable(Routes.LOGIN) {
            LoginPageUI(
                onForgotPasswordClick = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onSignUpClick = { navController.navigate(Routes.SIGNUP) },
                onBackClick = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate(Routes.CHANNELS) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignUp(
                onBackToLogin = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate(Routes.CHANNELS) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // 4. Password Recovery Flow
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordUI(
                onBackToLogin = { navController.popBackStack() },
                onSendCodeClick = { navController.navigate(Routes.PASSWORD_CONGRATULATIONS) }
            )
        }

        composable(Routes.PASSWORD_CONGRATULATIONS) {
            PasswordCongratulationsUI(
                onContinueClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // 5. Main Content (Music Hub)
        composable(Routes.CHANNELS) {
            ChannelsUI(
                onSettingsClick = { navController.navigate(Routes.SETTING) },
                onProfileEditClick = { navController.navigate(Routes.EDIT_PROFILE) },
                onChannelClick = { channel ->
                    val encodedName = Uri.encode(channel.name)
                    navController.navigate("${Routes.CHANNEL_PLAYER}/$encodedName")
                }
            )
        }

        composable(
            route = "${Routes.CHANNEL_PLAYER}/{channelName}",
            arguments = listOf(navArgument("channelName") { type = NavType.StringType })
        ) { backStackEntry ->
            val channelName = backStackEntry.arguments?.getString("channelName") ?: "Channel"
            ChannelPlayerUI(
                channelName = channelName,
                userName = "Music Lover",
                onBackClick = { navController.popBackStack() },
                onRequestSongClick = { navController.navigate(Routes.REQUEST_SONG) }
            )
        }


        composable(Routes.EDIT_PROFILE) {
            EditProfile(
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { navController.navigate(Routes.SETTING) }
            )
        }

        composable(Routes.SETTING) {
            SettingUI(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.REQUEST_SONG) {
            RequestSongUI(
                onBackClick = { navController.popBackStack() },
                onSubmitRequest = { _, _ -> navController.navigate(Routes.REQUEST_SONG_LIST) }
            )
        }

        composable(Routes.REQUEST_SONG_LIST) {
            RequestSongList(onBackClick = { navController.popBackStack() })
        }
    }
}
