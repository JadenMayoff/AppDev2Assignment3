package com.example.assignment3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    // verticalScroll enables scrolling if the screen is too small to fit the content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎓", style = MaterialTheme.typography.displayLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "JAC Marketplace",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Mission Statement
        Text(
            text = "By Students, For Students.",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Welcome to the exclusive peer-to-peer marketplace for John Abbott College. Whether you need textbooks for the upcoming semester, dorm room furniture, or electronics, trade securely with your fellow classmates.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Rules Section (Using Cards for visual separation)
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Community Guidelines",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Divider(modifier = Modifier.padding(vertical = 12.dp))

                GuidelineRow(icon = "📍", title = "Meet on Campus", desc = "Always conduct transactions in public areas like the Agora or Library.")
                Spacer(modifier = Modifier.height(16.dp))
                GuidelineRow(icon = "💬", title = "Be Respectful", desc = "Negotiate fairly and communicate clearly with other students.")
                Spacer(modifier = Modifier.height(16.dp))
                GuidelineRow(icon = "🚫", title = "No Prohibited Items", desc = "Alcohol, weapons, and illegal materials are strictly forbidden.")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Version 1.0.0 • Developed for A3",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

// Reusable micro-component for the guidelines list
@Composable
fun GuidelineRow(icon: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(text = icon, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(end = 12.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}