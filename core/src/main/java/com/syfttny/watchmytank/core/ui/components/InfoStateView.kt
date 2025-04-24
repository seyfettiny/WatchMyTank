package com.syfttny.watchmytank.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme

/**
 * A composable to display informative messages, typically for empty or error states.
 *
 * @param message The primary message to display.
 * @param modifier Optional Modifier.
 * @param icon Optional icon to display above the message.
 * @param actionLabel Optional text for an action button below the message.
 * @param onActionClick Optional lambda to be invoked when the action button is clicked.
 */
@Composable
fun InfoStateView(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = Icons.Default.Info, // Default to Info icon
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize() // Take available space
            .padding(32.dp), // Add significant padding
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center content vertically
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null, // Message text provides context
                modifier = Modifier.size(64.dp), // Make icon larger
                tint = MaterialTheme.colorScheme.secondary // Use a less prominent color
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant // Slightly muted text
        )
        if (actionLabel != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onActionClick) {
                Text(actionLabel)
            }
        }
    }
}

// Previews
@Preview(showBackground = true, name = "InfoStateView - Basic")
@Composable
private fun InfoStateViewPreview() {
    WatchMyTankTheme {
        InfoStateView(message = "No data available.")
    }
}

@Preview(showBackground = true, name = "InfoStateView - With Action")
@Composable
private fun InfoStateViewWithActionPreview() {
    WatchMyTankTheme {
        InfoStateView(
            message = "Failed to load data. Check connection?",
            icon = Icons.Default.Warning, // Use Warning icon for errors
            actionLabel = "Retry",
            onActionClick = {}
        )
    }
}

@Preview(showBackground = true, name = "InfoStateView - No Icon")
@Composable
private fun InfoStateViewNoIconPreview() {
    WatchMyTankTheme {
        InfoStateView(message = "Just a simple message.", icon = null)
    }
} 