package com.example.assignment3

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.navigation.NavType
import androidx.navigation.navArgument

// A simple sealed class to define our routes as required
sealed class Screen(val route: String) {
    object Create : Screen("create")

    // The preview screen takes a parameter in its route
    object Preview : Screen("preview/{itemJson}") {
        // Helper function to convert the Item object into a JSON string for the route URL
        fun createRoute(item: Item): String {
            // Note: Make sure to add @Serializable to your Item data class!
            val jsonString = Json.encodeToString(item)
            return "preview/$jsonString"
        }
    }

    object Feed : Screen("feed")
    object About : Screen("about")
}