package com.ditadigitali.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ditadigitali.navigation.Screen
import com.ditadigitali.ui.screens.ErrorScreen
import com.ditadigitali.ui.screens.SettingsScreen
import com.ditadigitali.ui.screens.WebViewScreen
import com.ditadigitali.utils.NetworkUtil

@Composable
fun Navigator() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val lastError = remember { mutableStateOf("") }
    NavHost(navController, startDestination = Screen.WebView.route) {
        composable(Screen.WebView.route) {
            WebViewScreen(
                onError = { msg -> navController.navigate(Screen.Error.createRoute(msg)) }
            )
        }
        composable(
            route = Screen.Error.route,
            arguments = listOf(navArgument("message") { type = NavType.StringType })
        ) { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message") ?: "Errore"
            lastError.value = message
            ErrorScreen(
                message = message,
                onRetry = { navController.navigate(Screen.WebView.route) },
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onSave = { newIp ->
                    NetworkUtil.saveServerIp(context, newIp)
                    navController.navigate(Screen.WebView.route)
                },
                onCancel = { navController.navigate(Screen.Error.createRoute(lastError.value)) }
            )
        }
    }
}