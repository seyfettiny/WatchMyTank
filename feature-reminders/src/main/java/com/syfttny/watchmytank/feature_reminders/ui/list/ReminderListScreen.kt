package com.syfttny.watchmytank.feature_reminders.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.feature_reminders.ui.list.components.ReminderListItem
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.tooling.preview.Preview
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme
import com.syfttny.watchmytank.domain.model.ReminderType
import java.time.LocalDateTime
import com.syfttny.watchmytank.core.ui.components.InfoStateView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderListScreen(
    viewModel: ReminderListViewModel = hiltViewModel(),
    onNavigateToEdit: (reminderId: Long?) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-off events
    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ReminderListContract.Event.NavigateToEditScreen -> {
                    onNavigateToEdit(event.reminderId)
                }
                is ReminderListContract.Event.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
                is ReminderListContract.Event.ShowUndoDeleteSnackbar -> {
                    // TODO: Implement Undo logic if desired
                    snackbarHostState.showSnackbar(message = "Reminder deleted") // Simple confirmation for now
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Reminders") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.handleIntent(ReminderListContract.Intent.AddReminder) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Reminder")
            }
        }
    ) { paddingValues ->
        ReminderListContent(
            state = state,
            onIntent = viewModel::handleIntent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun ReminderListContent(
    state: ReminderListContract.State,
    onIntent: (ReminderListContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.reminders.isEmpty() -> {
                InfoStateView(
                    message = "No reminders yet. \nTap '+' to add one.",
                    icon = Icons.Filled.Warning
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.reminders, key = { it.id }) { reminder ->
                        ReminderListItem(
                            reminder = reminder,
                            onEditClick = { onIntent(ReminderListContract.Intent.EditReminder(reminder.id)) },
                            onDeleteClick = { onIntent(ReminderListContract.Intent.DeleteReminder(reminder.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "List Screen Content - Empty")
@Composable
private fun ReminderListContentEmptyPreview() {
    WatchMyTankTheme {
        ReminderListContent(
            state = ReminderListContract.State(isLoading = false, reminders = emptyList()),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "List Screen Content - Loading")
@Composable
private fun ReminderListContentLoadingPreview() {
    WatchMyTankTheme {
        ReminderListContent(
            state = ReminderListContract.State(isLoading = true, reminders = emptyList()),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "List Screen Content - Data")
@Composable
private fun ReminderListContentDataPreview() {
    val now = LocalDateTime.now()
    val sampleReminders = listOf(
        Reminder(
            id = 1,
            name = "Morning Feed",
            type = ReminderType.DAILY,
            creationTime = now.minusDays(1),
            nextTriggerTime = now.plusHours(1),
            triggerHour = 8,
            triggerMinute = 0,
            isEnabled = true,
            lastTriggeredTime = now.minusDays(1).withHour(8).withMinute(0)
        ),
        Reminder(
            id = 2,
            name = "Water Change",
            type = ReminderType.EVERY_N_DAYS,
            frequencyDays = 7,
            creationTime = now.minusDays(3),
            nextTriggerTime = now.plusDays(4),
            triggerHour = 10,
            triggerMinute = 0,
            isEnabled = true,
            lastTriggeredTime = now.minusDays(3).withHour(10).withMinute(0)
        ),
        Reminder(
            id = 3,
            name = "Night Feed",
            type = ReminderType.DAILY,
            creationTime = now.minusDays(1),
            nextTriggerTime = now.plusHours(12),
            triggerHour = 20,
            triggerMinute = 0,
            isEnabled = false,
            lastTriggeredTime = now.minusDays(1).withHour(20).withMinute(0)
        ),
    )
    WatchMyTankTheme {
        ReminderListContent(
            state = ReminderListContract.State(isLoading = false, reminders = sampleReminders),
            onIntent = {}
        )
    }
} 