package com.example.assignment3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    onSubmit: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    var titleInput by rememberSaveable { mutableStateOf("") }
    var descInput by rememberSaveable { mutableStateOf("") }
    var priceInput by rememberSaveable { mutableStateOf("") }
    var imageUrlInput by rememberSaveable { mutableStateOf("") } // New field

    val categories = listOf("Books", "Clothing", "Electronics", "School Supplies", "Other")
    var selectedCategory by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    var titleError by rememberSaveable { mutableStateOf(false) }
    var descError by rememberSaveable { mutableStateOf(false) }
    var priceError by rememberSaveable { mutableStateOf(false) }
    var imageUrlError by rememberSaveable { mutableStateOf(false) }
    var categoryError by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Post an Item for Sale",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Item Title") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    maxLines = 2,
                    isError = titleError,
                    supportingText = { if (titleError) Text("Title cannot be empty", color = Color.Red) }
                )

                OutlinedTextField(
                    value = descInput,
                    onValueChange = { descInput = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    maxLines = 4,
                    isError = descError,
                    supportingText = { if (descError) Text("Description cannot be empty", color = Color.Red) }
                )

                OutlinedTextField(
                    value = priceInput,
                    onValueChange = { priceInput = it },
                    label = { Text("Price ($)") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    maxLines = 1,
                    isError = priceError,
                    supportingText = { if (priceError) Text("Enter a valid price greater than 0", color = Color.Red) }
                )

                OutlinedTextField(
                    value = imageUrlInput,
                    onValueChange = { imageUrlInput = it },
                    label = { Text("Image URL") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    maxLines = 1,
                    isError = imageUrlError,
                    supportingText = { if (imageUrlError) Text("Image URL cannot be empty", color = Color.Red) }
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        isError = categoryError,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        supportingText = { if (categoryError) Text("Please select a category", color = Color.Red) }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(text = category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        var isValid = true

                        titleError = titleInput.isBlank().also { if (it) isValid = false }
                        descError = descInput.isBlank().also { if (it) isValid = false }
                        priceError = (priceInput.toDoubleOrNull() == null || priceInput.toDouble() <= 0).also { if (it) isValid = false }
                        imageUrlError = imageUrlInput.isBlank().also { if (it) isValid = false }
                        categoryError = selectedCategory.isBlank().also { if (it) isValid = false }

                        if (isValid) {
                            val newItem = Item(
                                titleInput,
                                descInput,
                                priceInput.toDouble(),
                                selectedCategory,
                                imageUrlInput
                            )
                            onSubmit(newItem) // Pass the item up to the state manager/navigation

                            // Reset fields
                            titleInput = ""
                            descInput = ""
                            priceInput = ""
                            imageUrlInput = ""
                            selectedCategory = ""
                        }
                    }
                ) {
                    Text(text = "Create Listing")
                }
            }
        }
    }
}