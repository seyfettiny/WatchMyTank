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
import androidx.compose.material.icons.filled.Add
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
    Column(
        modifier = modifier
            .fillMaxSize(),
            // Remove center arrangement if list should start from top
            // .padding(16.dp), // Padding might be better applied around LazyColumn
        horizontalAlignment = Alignment.CenterHorizontally,
        // verticalArrangement = Arrangement.Center // Remove this
    ) {
        if (state.isLoading) {
             // Keep loading indicator centered
             Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                 CircularProgressIndicator()
             }
        } else if (state.reminders.isEmpty()) {
            // Keep empty message centered
             Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                 Text("No reminders yet. Tap '+' to add one.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                 modifier = Modifier.fillMaxSize(),
                 contentPadding = PaddingValues(16.dp), // Apply padding here
                 verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
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

        // Display error if present (could also be handled via snackbar only)
        /* // Removing this as error is primarily shown via Snackbar now
        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        */
    }
} 