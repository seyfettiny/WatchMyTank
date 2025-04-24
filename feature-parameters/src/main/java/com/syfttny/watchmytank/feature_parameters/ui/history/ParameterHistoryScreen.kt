package com.syfttny.watchmytank.feature_parameters.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import androidx.compose.ui.tooling.preview.Preview
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme
import java.time.LocalDateTime
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import com.syfttny.watchmytank.core.ui.components.InfoStateView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParameterHistoryScreen(
    viewModel: ParameterHistoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToLogParameter: () -> Unit, // Callback to navigate to the logging screen
    onNavigateToChart: (ParameterType) -> Unit // Pass selected type to chart
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-off events
    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ParameterHistoryContract.Event.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
                // Handle other events like navigation if added
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Parameter History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Show sync status icon if items are pending
                    if (state.unsyncedCount > 0) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = "${state.unsyncedCount} logs pending sync",
                            modifier = Modifier.padding(end = 8.dp), // Add some spacing
                            tint = MaterialTheme.colorScheme.onSurfaceVariant // Use a muted color
                        )
                    }
                    IconButton(onClick = { onNavigateToChart(state.selectedType) }) { // Pass type
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "View Chart for ${state.selectedType.displayName}")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToLogParameter) {
                Icon(Icons.Filled.Add, contentDescription = "Log New Parameter")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            // Filter Selection (Using ScrollableTabRow as an example)
            ParameterTypeFilterTabs(
                availableTypes = state.availableTypes,
                selectedType = state.selectedType,
                onTypeSelected = { viewModel.handleIntent(ParameterHistoryContract.Intent.SelectFilterType(it)) }
            )

            Divider() // Separator

            // History List Content
            ParameterHistoryListContent(
                isLoading = state.isLoading,
                historyLogs = state.historyLogs,
                error = state.error,
                modifier = Modifier.weight(1f) // Takes remaining space
            )
        }
    }
}

@Composable
private fun ParameterTypeFilterTabs(
    availableTypes: List<ParameterType>,
    selectedType: ParameterType,
    onTypeSelected: (ParameterType) -> Unit
) {
    val selectedIndex = availableTypes.indexOf(selectedType)
    if (selectedIndex == -1) return // Should not happen if state is consistent

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 8.dp // Padding at the start/end
    ) {
        availableTypes.forEachIndexed { index, type ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTypeSelected(type) },
                text = { Text(type.displayName) }
            )
        }
    }
}

@Composable
private fun ParameterHistoryListContent(
    isLoading: Boolean,
    historyLogs: List<WaterParameterLog>,
    error: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Content alignment for loading/empty/error
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                InfoStateView(
                    message = "Error loading history: \n$error",
                    icon = Icons.Default.Warning
                    // Optionally add a retry button here if appropriate
                    // actionLabel = "Retry",
                    // onActionClick = { /* TODO: Trigger reload intent */ }
                )
            }
            historyLogs.isEmpty() -> {
                InfoStateView(
                    message = "No history recorded for this parameter yet.",
                    icon = Icons.Default.Info // Or null
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), // Take up Box space
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(historyLogs, key = { it.id }) { log ->
                        ParameterLogListItem(log = log)
                    }
                }
            }
        }
    }
}

@Composable
private fun ParameterLogListItem(log: WaterParameterLog) {
    // Simple Card layout for each log item
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Format timestamp nicely
                val formatter = remember { DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT) }
                Text(
                    text = log.timestamp.format(formatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${log.parameterType.displayName}: ${log.value} ${log.parameterType.unit}",
                    style = MaterialTheme.typography.bodyLarge
                )
                log.notes?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Notes: $it",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            // Add delete/edit icons here if needed later
        }
    }
}

@Preview(showBackground = true, name = "History Screen Content - Empty")
@Composable
private fun ParameterHistoryListContentEmptyPreview() {
    WatchMyTankTheme {
        ParameterHistoryListContent(
            isLoading = false,
            historyLogs = emptyList(),
            error = null
        )
    }
}

@Preview(showBackground = true, name = "History Screen Content - Loading")
@Composable
private fun ParameterHistoryListContentLoadingPreview() {
    WatchMyTankTheme {
        ParameterHistoryListContent(
            isLoading = true,
            historyLogs = emptyList(),
            error = null
        )
    }
}

@Preview(showBackground = true, name = "History Screen Content - Error")
@Composable
private fun ParameterHistoryListContentErrorPreview() {
    WatchMyTankTheme {
        ParameterHistoryListContent(
            isLoading = false,
            historyLogs = emptyList(),
            error = "Failed to load data."
        )
    }
}

@Preview(showBackground = true, name = "History Screen Content - Data")
@Composable
private fun ParameterHistoryListContentDataPreview() {
    val sampleLogs = listOf(
        WaterParameterLog(1, LocalDateTime.now().minusDays(1), ParameterType.PH, 7.2, "Looking good"),
        WaterParameterLog(2, LocalDateTime.now().minusHours(5), ParameterType.PH, 7.1),
        WaterParameterLog(3, LocalDateTime.now(), ParameterType.TEMPERATURE, 25.5, "Stable")
    )
    WatchMyTankTheme {
        ParameterHistoryListContent(
            isLoading = false,
            historyLogs = sampleLogs.filter { it.parameterType == ParameterType.PH }, // Simulate filtering
            error = null
        )
    }
}

@Preview(showBackground = true, name = "History List Item")
@Composable
private fun ParameterLogListItemPreview() {
    WatchMyTankTheme {
        ParameterLogListItem(
            log = WaterParameterLog(
                id = 1,
                timestamp = LocalDateTime.now(),
                parameterType = ParameterType.NITRATE,
                value = 10.0,
                notes = "Slightly elevated"
            )
        )
    }
}

@Preview
@Composable
private fun ParameterTypeFilterTabsPreview() {
    WatchMyTankTheme {
        ParameterTypeFilterTabs(
            availableTypes = ParameterType.values().toList(),
            selectedType = ParameterType.AMMONIA,
            onTypeSelected = {}
        )
    }
}

// TODO: Add Composable Previews for ParameterHistoryScreen/Content/ListItem 