package com.example.assignment3

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// A simple sealed class to define our routes as required
sealed class Screen(val route: String) {
    object Create : Screen("create")

    // The preview route expects an encoded parameter
    object Preview : Screen("preview/{itemBase64}") {
        @OptIn(ExperimentalEncodingApi::class)
        fun createRoute(item: Item): String {
            val jsonString = Json.encodeToString(item)
            // Encode safely and strip padding ('=') to prevent route corruption crashes
            val base64String = Base64.UrlSafe.encode(jsonString.encodeToByteArray()).replace("=", "")
            return "preview/$base64String"
        }
    }

    object Feed : Screen("feed")
    object About : Screen("about")
}