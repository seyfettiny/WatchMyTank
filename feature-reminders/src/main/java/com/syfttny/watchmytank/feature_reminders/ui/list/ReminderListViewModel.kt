package com.syfttny.watchmytank.feature_reminders.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syfttny.watchmytank.domain.use_case.DeleteReminderUseCase
import com.syfttny.watchmytank.domain.use_case.GetAllRemindersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderListViewModel @Inject constructor(
    private val getAllRemindersUseCase: GetAllRemindersUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
    // Inject other use cases like GetReminderByIdUseCase, SaveReminderUseCase later if needed for undo
) : ViewModel() {

    private val _state = MutableStateFlow(ReminderListContract.State())
    val state: StateFlow<ReminderListContract.State> = _state.asStateFlow()

    private val _eventChannel = Channel<ReminderListContract.Event>()
    val events = _eventChannel.receiveAsFlow() // UI collects this for one-off events

    init {
        handleIntent(ReminderListContract.Intent.LoadReminders)
    }

    fun handleIntent(intent: ReminderListContract.Intent) {
        when (intent) {
            is ReminderListContract.Intent.LoadReminders -> loadReminders()
            is ReminderListContract.Intent.DeleteReminder -> deleteReminder(intent.reminderId)
            is ReminderListContract.Intent.EditReminder -> navigateToEdit(intent.reminderId)
            is ReminderListContract.Intent.AddReminder -> navigateToEdit(null) // null ID signifies creation
        }
    }

    private fun loadReminders() {
        _state.update { it.copy(isLoading = true) } // Show loading indicator

        getAllRemindersUseCase()
            .onEach { reminders ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        reminders = reminders,
                        error = null // Clear previous error on success
                    )
                }
            }
            .catch { throwable ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.localizedMessage ?: "An unexpected error occurred"
                    )
                }
                _eventChannel.send(ReminderListContract.Event.ShowErrorSnackbar(throwable.localizedMessage ?: "Failed to load reminders"))
            }
            .launchIn(viewModelScope) // Collect the flow within the ViewModel's scope
    }

    private fun deleteReminder(reminderId: Long) {
        viewModelScope.launch {
            try {
                deleteReminderUseCase(reminderId)
                // Optionally: Send an event to show an 'Undo' snackbar
                 _eventChannel.send(ReminderListContract.Event.ShowUndoDeleteSnackbar(reminderId))
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage ?: "Failed to delete reminder") }
                 _eventChannel.send(ReminderListContract.Event.ShowErrorSnackbar(e.localizedMessage ?: "Failed to delete reminder"))
            }
        }
    }

    private fun navigateToEdit(reminderId: Long?) {
        viewModelScope.launch {
            _eventChannel.send(ReminderListContract.Event.NavigateToEditScreen(reminderId))
        }
    }
} 