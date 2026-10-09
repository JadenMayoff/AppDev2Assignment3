package com.example.assignment3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun FeedScreen(itemList: MutableList<Item>) {
    var selectedItemForDialog by remember { mutableStateOf<Item?>(null) }
    var itemToDelete by remember { mutableStateOf<Item?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Marketplace Feed",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (itemList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No items for sale yet. Go post one!", color = MaterialTheme.colorScheme.secondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Centers the cards horizontally on wide screens
            ) {
                items(itemList) { item ->
                    // Constrain the max width of the card so it doesn't span a huge desktop screen
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(0.7f) // Takes up 70% of screen width on desktop, scales nicely
                            .clickable { selectedItemForDialog = item },
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {

                            // Controlled height image banner that won't blow up on desktop
                            KamelImage(
                                resource = asyncPainterResource(data = item.imageUrl),
                                contentDescription = "Item Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp) // Clean, fixed max height for the image banner
                                    .background(Color.LightGray),
                                contentScale = ContentScale.Crop,
                                onLoading = {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                },
                                onFailure = {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text("📷", style = MaterialTheme.typography.displayLarge)
                                    }
                                }
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$${item.price} • ${item.category}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                IconButton(
                                    onClick = { itemToDelete = item },
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text("🗑️", style = MaterialTheme.typography.titleLarge)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- DIALOG POPUPS ---

    selectedItemForDialog?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedItemForDialog = null },
            title = { Text(text = item.title) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    KamelImage(
                        resource = asyncPainterResource(data = item.imageUrl),
                        contentDescription = "Item Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        onLoading = { CircularProgressIndicator(modifier = Modifier.padding(32.dp)) },
                        onFailure = { Text("Failed to load image", color = MaterialTheme.colorScheme.error) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Price: $${item.price}", fontWeight = FontWeight.Bold)
                        Text("Category: ${item.category}", color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Description: ${item.description}")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { selectedItemForDialog = null }) { Text("Close") }
            }
        )
    }

    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text(text = "Delete Listing?") },
            text = { Text(text = "Are you sure you want to permanently remove '${item.title}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        itemList.remove(item)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) { Text("Cancel") }
            }
        )
    }
}