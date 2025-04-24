package com.syfttny.watchmytank.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme // Assuming theme is here

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToReminders: () -> Unit,
    onNavigateToParameters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("WatchMyTank Dashboard") })
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DashboardButton(
                text = "Reminders",
                icon = Icons.Default.DateRange,
                onClick = onNavigateToReminders
            )
            DashboardButton(
                text = "Parameters",
                icon = Icons.Default.Add,
                onClick = onNavigateToParameters
            )
            // Add more buttons/cards for future features (e.g., Settings, Tank Profile)
        }
    }
}

@Composable
private fun DashboardButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(60.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    WatchMyTankTheme {
        DashboardScreen(
            onNavigateToReminders = {},
            onNavigateToParameters = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardButtonPreview() {
    WatchMyTankTheme {
        DashboardButton(
            text = "Preview Button",
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {}
        )
    }
}

// TODO: Add Preview for DashboardScreen 