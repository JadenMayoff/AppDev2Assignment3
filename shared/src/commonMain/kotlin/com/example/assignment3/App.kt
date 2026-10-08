package com.example.assignment3

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.json.Json

@Composable
fun App() {
    // 1. STATE HOISTING: We keep the master list at the very top of the app.
    // We are reusing your custom listSaver from Assignment 2 to survive screen rotations!
    val itemList = rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                // Flattening the object to save it during rotation
                stateList.flatMap { listOf(it.title, it.description, it.price.toString(), it.category, it.imageUrl) }
            },
            restore = { savedList ->
                val restoredList = mutableStateListOf<Item>()
                for (i in savedList.indices step 5) {
                    restoredList.add(
                        Item(
                            title = savedList[i],
                            description = savedList[i + 1],
                            price = savedList[i + 2].toDouble(),
                            category = savedList[i + 3],
                            imageUrl = savedList[i + 4]
                        )
                    )
                }
                restoredList
            }
        )
    ) {
        mutableStateListOf<Item>()
    }

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // 2. RESPONSIVE DESIGN: BoxWithConstraints lets us measure the available screen width.
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        if (maxWidth < 600.dp) {
            // MOBILE LAYOUT (Android): Content on top, NavigationBar on the bottom
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    MarketplaceNavHost(navController, itemList)
                }
                MarketplaceBottomBar(navController, currentRoute)
            }
        } else {
            // DESKTOP/WEB LAYOUT: NavigationRail on the left, Content on the right
            Row(modifier = Modifier.fillMaxSize()) {
                MarketplaceNavRail(navController, currentRoute)
                Box(modifier = Modifier.weight(1f)) {
                    MarketplaceNavHost(navController, itemList)
                }
            }
        }
    }
}

// 3. NAVIGATION HOST: Maps our routes to the actual UI screens
@Composable
fun MarketplaceNavHost(navController: NavHostController, itemList: MutableList<Item>) {
    NavHost(navController = navController, startDestination = Screen.Create.route) {

        // SCREEN 1: Create Listing
        composable(Screen.Create.route) {
            CreateListingScreen(
                onSubmit = { newItem ->
                    itemList.add(newItem) // Add to our hoisted state
                    val jsonItem = Screen.Preview.createRoute(newItem) // Serialize to JSON
                    navController.navigate(jsonItem) // Navigate to preview and pass item
                }
            )
        }

        // SCREEN 2: Preview (Receives the JSON parameter)
        composable(Screen.Preview.route) { backStackEntry ->
            // Extract the passed JSON string from the URL and convert it back to an Item
            val itemJson = backStackEntry.arguments?.getString("itemJson")
            val item = itemJson?.let { Json.decodeFromString<Item>(it) }

            if (item != null) {
                // TODO: Build PreviewScreen
                Text("Preview Screen for: ${item.title}")
            }
        }

        // SCREEN 3: Interactive Feed
        composable(Screen.Feed.route) {
            // TODO: Build FeedScreen
            Text("Feed Screen - Items count: ${itemList.size}")
        }

        // SCREEN 4: Information Screen
        composable(Screen.About.route) {
            // TODO: Build AboutScreen
            Text("About JAC Marketplace")
        }
    }
}

// --- RESPONSIVE NAVIGATION COMPONENTS ---

@Composable
fun MarketplaceBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Create") },
            label = { Text("Post") },
            selected = currentRoute == Screen.Create.route,
            onClick = { navController.navigate(Screen.Create.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Feed") },
            label = { Text("Feed") },
            selected = currentRoute == Screen.Feed.route,
            onClick = { navController.navigate(Screen.Feed.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
            label = { Text("About") },
            selected = currentRoute == Screen.About.route,
            onClick = { navController.navigate(Screen.About.route) }
        )
    }
}

@Composable
fun MarketplaceNavRail(navController: NavHostController, currentRoute: String?) {
    NavigationRail {
        NavigationRailItem(
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Create") },
            label = { Text("Post") },
            selected = currentRoute == Screen.Create.route,
            onClick = { navController.navigate(Screen.Create.route) }
        )
        NavigationRailItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Feed") },
            label = { Text("Feed") },
            selected = currentRoute == Screen.Feed.route,
            onClick = { navController.navigate(Screen.Feed.route) }
        )
        NavigationRailItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
            label = { Text("About") },
            selected = currentRoute == Screen.About.route,
            onClick = { navController.navigate(Screen.About.route) }
        )
    }
}