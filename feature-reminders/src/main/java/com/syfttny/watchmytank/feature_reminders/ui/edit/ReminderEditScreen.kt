package com.syfttny.watchmytank.feature_reminders.ui.edit

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syfttny.watchmytank.domain.model.ReminderType
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar
import androidx.compose.ui.tooling.preview.Preview
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderEditScreen(
    viewModel: ReminderEditViewModel = hiltViewModel(),
    reminderId: Long?, 
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    
    LaunchedEffect(key1 = reminderId) {
        viewModel.handleIntent(ReminderEditContract.Intent.LoadReminder(reminderId))
    }

    
    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                ReminderEditContract.Event.NavigateBack -> onNavigateBack()
                is ReminderEditContract.Event.ShowError -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Edit Reminder" else "Add Reminder") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                     
                     if (state.isLoading && !state.isEditing) { 
                         CircularProgressIndicator(modifier = Modifier.size(24.dp))
                     } else {
                        IconButton(onClick = { viewModel.handleIntent(ReminderEditContract.Intent.SaveReminder) }) {
                            Icon(Icons.Default.Check, contentDescription = "Save Reminder")
                        }
                     }
                }
            )
        }
    ) { paddingValues ->
        ReminderEditContent(
            state = state,
            onIntent = viewModel::handleIntent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun ReminderEditContent(
    state: ReminderEditContract.State,
    onIntent: (ReminderEditContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showTimePicker by remember { mutableStateOf(false) }

    
    val calendar = Calendar.getInstance()
    val defaultHour = state.triggerHour ?: calendar.get(Calendar.HOUR_OF_DAY)
    val defaultMinute = state.triggerMinute ?: calendar.get(Calendar.MINUTE)

    
    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _: TimePicker, hour: Int, minute: Int ->
                onIntent(ReminderEditContract.Intent.UpdateTime(hour, minute))
                showTimePicker = false 
            },
            defaultHour, 
            defaultMinute, 
            false 
        ).apply {
            setOnDismissListener { showTimePicker = false } 
            show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), 
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        if (state.isLoading && state.isEditing) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
             OutlinedTextField(
                 value = state.name,
                 onValueChange = { onIntent(ReminderEditContract.Intent.UpdateName(it)) },
                 label = { Text("Reminder Name") },
                 modifier = Modifier.fillMaxWidth(),
                 singleLine = true,
                 isError = state.error?.contains("Name", ignoreCase = true) == true 
             )

             ReminderTypeSelector(
                 selectedType = state.type,
                 onTypeSelected = { onIntent(ReminderEditContract.Intent.SelectType(it)) }
             )

             
             if (state.type == ReminderType.DAILY || state.type == ReminderType.EVERY_N_DAYS) {
                 Row(
                     modifier = Modifier.fillMaxWidth(),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.SpaceBetween
                 ) {
                    Text("Trigger Time", style = MaterialTheme.typography.bodyLarge)
                    
                    val timeString = remember(state.triggerHour, state.triggerMinute) {
                         if (state.triggerHour != null && state.triggerMinute != null) {
                             String.format("%02d:%02d", state.triggerHour, state.triggerMinute)
                         } else {
                             "Select Time" 
                         }
                     }
                     Text(
                         text = timeString,
                         style = MaterialTheme.typography.bodyLarge.copy(
                             color = MaterialTheme.colorScheme.primary,
                             textDecoration = TextDecoration.Underline
                         ),
                         modifier = Modifier.clickable { showTimePicker = true }
                     )
                 }
             }

             
             if (state.type == ReminderType.EVERY_N_DAYS) {
                 OutlinedTextField(
                     value = state.frequencyDays,
                     onValueChange = { onIntent(ReminderEditContract.Intent.UpdateFrequencyDays(it)) },
                     label = { Text("Frequency (Days)") },
                     modifier = Modifier.fillMaxWidth(),
                     singleLine = true,
                     isError = state.error?.contains("Frequency", ignoreCase = true) == true
                 )
             }

            
             if (state.type == ReminderType.CRON) {
                 OutlinedTextField(
                     value = state.cronExpression,
                     onValueChange = { onIntent(ReminderEditContract.Intent.UpdateCronExpression(it)) },
                     label = { Text("CRON Expression") },
                     modifier = Modifier.fillMaxWidth(),
                      
                     isError = state.error?.contains("CRON", ignoreCase = true) == true
                 )
                 
             }

            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Enabled", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = state.isEnabled,
                    onCheckedChange = { onIntent(ReminderEditContract.Intent.UpdateEnabled(it)) }
                )
            }

            
             state.error?.let {
                 if (!it.contains("Name", ignoreCase = true) &&
                     !it.contains("Frequency", ignoreCase = true) &&
                     !it.contains("CRON", ignoreCase = true)
                 ) {
                     Text(
                         text = it,
                         color = MaterialTheme.colorScheme.error,
                         style = MaterialTheme.typography.bodySmall,
                         modifier = Modifier.padding(top = 8.dp)
                     )
                 }
             }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderTypeSelector(
    selectedType: ReminderType,
    onTypeSelected: (ReminderType) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = ReminderType.values() 
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text("Reminder Type", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
             OutlinedTextField(
                 value = selectedType.name, 
                 onValueChange = {}, 
                 readOnly = true,
                 label = { Text("Select Type") },
                 trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                 modifier = Modifier.menuAnchor().fillMaxWidth() 
             )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) }, 
                        onClick = {
                            onTypeSelected(type)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Edit Screen Content - New Daily")
@Composable
private fun ReminderEditContentNewDailyPreview() {
    WatchMyTankTheme {
        ReminderEditContent(
            state = ReminderEditContract.State(type = ReminderType.DAILY, isEditing = false),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Screen Content - Edit Every N Days")
@Composable
private fun ReminderEditContentEditNDaysPreview() {
    WatchMyTankTheme {
        ReminderEditContent(
            state = ReminderEditContract.State(
                isEditing = true,
                name = "Weekly Water Change",
                type = ReminderType.EVERY_N_DAYS,
                frequencyDays = "7",
                triggerHour = 10,
                triggerMinute = 30,
                isEnabled = true
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Screen Content - Edit CRON")
@Composable
private fun ReminderEditContentEditCronPreview() {
    WatchMyTankTheme {
        ReminderEditContent(
            state = ReminderEditContract.State(
                isEditing = true,
                name = "Custom Feed",
                type = ReminderType.CRON,
                cronExpression = "0 18 * * MON-FRI",
                isEnabled = true
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Screen Content - Error")
@Composable
private fun ReminderEditContentWithErrorPreview() {
    WatchMyTankTheme {
        ReminderEditContent(
            state = ReminderEditContract.State(error = "Name cannot be empty"),
            onIntent = {}
        )
    }
}

