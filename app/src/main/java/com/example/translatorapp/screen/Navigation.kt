package com.example.translatorapp.screen


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.translatorapp.R


@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) { SplashScreen(navController) }
        composable(Screen.MainScreen.route) { MainScreen(navController) }
        composable(Screen.ChatScreen.route) { ChatScreen(navController) }
        composable(Screen.CameraScreen.route) { CameraScreen(navController) }
        composable(Screen.HistoryScreen.route) { HistoryScreen(navController) }
        composable(Screen.FavouriteScreen.route) { FavouriteScreen(navController) }


    }
}

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object MainScreen : Screen("MainScreen", "", icon = R.drawable.mdi_translate)
    object SplashScreen : Screen("SplashScreen", "SplashScreen", icon = R.drawable.mic)
    object ChatScreen : Screen("ChatScreen", "Chat", icon = R.drawable.mic)
    object CameraScreen : Screen("CameraScreen", "Camera", icon = R.drawable.camera)
    object HistoryScreen : Screen("HistoryScreen", "History", icon = R.drawable.history)
    object FavouriteScreen : Screen("FavouriteScreen", "Favourite", icon = R.drawable.star)
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        Screen.ChatScreen,
        Screen.CameraScreen,
        Screen.MainScreen,
        Screen.HistoryScreen,
        Screen.FavouriteScreen
    )

    val navStack by navController.currentBackStackEntryAsState()
    val current = navStack?.destination?.route

    NavigationBar(
        containerColor = Color(0xFFFFFBFE)
    ) {
        items.forEachIndexed { index, screen ->
            val isSelected = current == screen.route
            val isCenterItem = index == 2

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if (isCenterItem) {
                        Box(
                            modifier = Modifier
                                .size(49.dp)
                                .offset(y = (15).dp)
                                .background(color = Color(0xFF0A57FF), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFF0A57FF) else Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.title,
                        color = if (isSelected) Color(0xFF0A57FF) else Color.Black, fontSize = 10.sp
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFFFFFBFE),

                )
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavEntry() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomNav = currentRoute != Screen.SplashScreen.route

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigation(navController = navController)
            }
        }
    ) {
        Navigation(navController = navController)
    }
}