package com.example.trucast.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trucast.ui.screens.BackToHome
import com.example.trucast.ui.screens.ChannelPlayerUI
import com.example.trucast.ui.screens.ChannelsUI
import com.example.trucast.ui.screens.CreateNewPasswordUI
import com.example.trucast.ui.screens.EditProfile
import com.example.trucast.ui.screens.ForgotPasswordUI
import com.example.trucast.ui.screens.LoginPageUI
import com.example.trucast.ui.screens.MainScreenUI
import com.example.trucast.ui.screens.PasswordCongratulationsUI
import com.example.trucast.ui.screens.RequestSongList
import com.example.trucast.ui.screens.RequestSongUI
import com.example.trucast.ui.screens.ResetPassword
import com.example.trucast.ui.screens.SettingUI
import com.example.trucast.ui.screens.SignUp
import com.example.trucast.ui.screens.SplashScreenUI
import com.example.trucast.ui.screens.VerifyPasswordUI

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "Forgot Password"
    const val VERIFY_PASSWORD = "verify_password"
    const val CREATE_NEW_PASSWORD = "create_new_password"
    const val PASSWORD_CONGRATULATIONS = "password_congratulations"
    const val SIGNUP = "sign_up"
    const val RESET_PASSWORD = "reset_password"
    const val BACK_TO_HOME = "back_to_home"
    const val EDIT_PROFILE = "edit_profile"
    const val SETTING  ="Setting"
    const val CHANNELS = "channels"
    const val CHANNEL_PLAYER = "channel_player"
    const val REQUEST_SONG = "request_song"
    const val REQUEST_SONG_LIST = "request_song_list"
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
        composable(Routes.SPLASH) {
            SplashScreenUI(onTimeout = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Routes.HOME) {
            MainScreenUI(onGetStartedClick = {
                navController.navigate(Routes.LOGIN)
            })
        }

        composable(Routes.LOGIN) {
            LoginPageUI(
                onForgotPasswordClick = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                },
                onSignUpClick = {
                    navController.navigate(Routes.SIGNUP)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.navigate(Routes.CHANNELS)
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignUp(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate(Routes.RESET_PASSWORD)
                }
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordUI(
                onBackToLogin = { navController.popBackStack() },
                onSendCodeClick = {
                    navController.navigate(Routes.VERIFY_PASSWORD)
                }
            )
        }

        composable(Routes.VERIFY_PASSWORD) {
            VerifyPasswordUI(
                onBackToLogin = { navController.popBackStack() },
                onVerifyClick = {
                    navController.navigate(Routes.CREATE_NEW_PASSWORD)
                },
                onResendCode = {
                    println ("Resend code callback triggered inside AppNavHost")
                }
            )
        }
        composable(Routes.CREATE_NEW_PASSWORD) {
            CreateNewPasswordUI(
                onBackToLogin = { navController.popBackStack() },
                onSubmit = { _, _ ->
                    navController.navigate(Routes.PASSWORD_CONGRATULATIONS)
                }
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

        composable(Routes.RESET_PASSWORD) {
            ResetPassword(
                onBackClick = {
                    navController.popBackStack()
                },
                onUpdateSuccess = {
                    navController.navigate(Routes.BACK_TO_HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.BACK_TO_HOME) {
            BackToHome(
                onBackToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.BACK_TO_HOME) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.CHANNELS) {
            ChannelsUI(
                onSettingsClick = {
                    navController.navigate(Routes.EDIT_PROFILE)
                },
                onChannelClick = { channel ->
                    navController.navigate("${Routes.CHANNEL_PLAYER}/${channel.name}")
                }
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfile(
                onBackClick = {
                    navController.popBackStack()
                },
                onSettingsClick = {
                    navController.navigate( Routes.SETTING)
                }
            )
        }
        composable(Routes.SETTING){

            SettingUI(
                onBackClick = {
                    navController.popBackStack()
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
                userName = "User Name",
                onBackClick = {
                    navController.popBackStack()
                },
                onRequestSongClick = {
                    navController.navigate(Routes.REQUEST_SONG)
                }
            )
        }
        composable(Routes.REQUEST_SONG) {
            RequestSongUI(
                onBackClick = {
                    navController.popBackStack()
                },
                onSubmitRequest = { _, _ ->
                    navController.navigate(Routes.REQUEST_SONG_LIST)
                }
            )
        }
        composable(Routes.REQUEST_SONG_LIST) {
            RequestSongList(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
