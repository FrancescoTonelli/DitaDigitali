package com.ditadigitali.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object WebView     : Screen("webview")
    data object Error       : Screen("error/{message}") {
        fun createRoute(msg: String) = "error/${Uri.encode(msg)}"
    }
    data object Settings    : Screen("settings")
}
