package com.example.assignment3

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun App() {
    // STATE HOISTING: The master list lives here so all screens can access it.
    val itemList = rememberSaveable(
        saver = listSaver(
            save = { stateList ->
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
    ) { mutableStateListOf<Item>() }

    // INITIALIZE NAVIGATION CONTROLLER
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // RESPONSIVE LAYOUT CHECK
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        if (maxWidth < 600.dp) {
            // Mobile Layout: Navigation at the bottom
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) { MarketplaceNavHost(navController, itemList) }
                MarketplaceBottomBar(navController, currentRoute)
            }
        } else {
            // Desktop Layout: Navigation on the left rail
            Row(modifier = Modifier.fillMaxSize()) {
                MarketplaceNavRail(navController, currentRoute)
                Box(modifier = Modifier.weight(1f)) { MarketplaceNavHost(navController, itemList) }
            }
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun MarketplaceNavHost(navController: NavHostController, itemList: MutableList<Item>) {
    NavHost(navController = navController, startDestination = Screen.Create.route) {

        composable(Screen.Create.route) {
            CreateListingScreen(
                onSubmit = { newItem ->
                    itemList.add(newItem) // 1. Add to the state list
                    val encodedItemRoute = Screen.Preview.createRoute(newItem) // 2. Encode to string
                    navController.navigate(encodedItemRoute) // 3. Navigate
                }
            )
        }

        composable(Screen.Preview.route) {
            // Bypass the KMP SavedState parsing issues completely by just
            // grabbing the item we added to the hoisted state a millisecond ago.
            val item = itemList.lastOrNull()

            if (item != null) {
                ListingPreviewScreen(
                    item = item,
                    onNavigateToFeed = {
                        navController.navigate(Screen.Feed.route) {
                            popUpTo(Screen.Create.route) // Prevents the user from clicking back endlessly
                        }
                    }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error loading preview.", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        composable(Screen.Feed.route) {
            FeedScreen(itemList = itemList)
        }

        // Add the new Information screen route
        composable(Screen.About.route) {
            AboutScreen()
        }
    }
}

@Composable
fun MarketplaceBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Create") },
            label = { Text("Post") },
            selected = currentRoute == Screen.Create.route,
            onClick = { navController.navigate(Screen.Create.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "Feed") },
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
            icon = { Icon(Icons.Default.Add, contentDescription = "Create") },
            label = { Text("Post") },
            selected = currentRoute == Screen.Create.route,
            onClick = { navController.navigate(Screen.Create.route) }
        )
        NavigationRailItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "Feed") },
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