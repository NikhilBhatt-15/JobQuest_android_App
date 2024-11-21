package com.example.job_test


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.job_test.data.repository.AuthRepository
import com.example.job_test.data.repository.UserRepository
import com.example.job_test.ui.screens.BottomNavItem
import com.example.job_test.ui.screens.CreateProfileScreen

import com.example.job_test.ui.screens.LoginScreen
import com.example.job_test.ui.screens.MainScreen
import com.example.job_test.ui.screens.ProfileScreen
import com.example.job_test.ui.screens.RegisterScreen
import com.example.job_test.ui.screens.SplashScreen
import com.example.job_test.ui.theme.Job_testTheme
import com.example.job_test.ui.viewmodel.CreateProfileViewModel
import com.example.job_test.ui.viewmodel.HomeViewModel
import com.example.job_test.ui.viewmodel.LoginViewModel
import com.example.job_test.ui.viewmodel.ProfileViewModel
import com.example.job_test.ui.viewmodel.RegisterViewModel
import com.example.job_test.ui.viewmodel.SplashViewModel
import com.example.job_test.utility.PreferenceManager
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get the registration token
            val token = task.result
            Log.d("FCM", "FCM registration token: $token.")
            // Send the token to your server or save it locally
        }

        setContent {
            Job_testTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(applicationContext,navController)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}



@Composable
fun App(context: Context,navController: NavHostController){

    val bottomNavItems = listOf(
        BottomNavItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,

        ),
        BottomNavItem(
            title = "jobs",
            selectedIcon = Icons.Filled.Email,
            unSelectedIcon = Icons.Outlined.Email,
            hasNews = true,
        ),
        BottomNavItem(
            title = "Saved Jobs",
            selectedIcon = Icons.Filled.Favorite,
            unSelectedIcon = Icons.Outlined.Favorite
        ),
        BottomNavItem(
            title = "Profile",
            selectedIcon = Icons.Filled.AccountCircle,
            unSelectedIcon = Icons.Outlined.AccountCircle
        )
    )
    val HomeViewModel = HomeViewModel(UserRepository(context))
    val token = PreferenceManager.getToken(context)
    NavHost(navController = navController, startDestination = "splash"){
        composable("splash"){
            SplashScreen(viewModel = SplashViewModel(AuthRepository(context)),
                context = context,
                onLogin = {
                    navController.navigate("login"){
                        popUpTo("splash"){
                            inclusive = true
                        }
                    }
                },
                onProfile = {
                    navController.navigate("main"){
                        popUpTo("splash"){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("register"){ RegisterScreen(
            RegisterViewModel(AuthRepository(context)),
            context = context,
            onRegister = {email,password ->
                Log.d("Register","Email: $email")
                Log.d("Register","Password: $password")
                navController.navigate("login")
            },
            onGoToLogin = {
                navController.popBackStack()
            }
        )}
        composable("login"){ LoginScreen(
            LoginViewModel(AuthRepository(context)),
            context = context,
            onLogin = {

//                i have to make sure getProfile is called and completed before navigating to main screen

                    navController.navigate("createProfile"){
                        popUpTo("login"){
                            inclusive = true
                        }
                    }


            },
            onGoToRegister = {
                navController.navigate("register")
            }
        )}
        composable("createProfile"){
            CreateProfileScreen(viewModel = CreateProfileViewModel(UserRepository(context)),
                context = context,
                onProfileCreated = {
                    navController.navigate("main"){
                        popUpTo("createProfile"){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("main"){
            MainScreen(context, bottomNavItems = bottomNavItems, onLogout = {
                PreferenceManager.clearToken(context)
                navController.navigate("login"){
                    popUpTo("main"){
                        inclusive = true
                    }
                }
            })
        }
    }
}


// my name is khan

// and i am not terroist







