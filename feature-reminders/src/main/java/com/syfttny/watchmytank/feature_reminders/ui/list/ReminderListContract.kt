package com.syfttny.watchmytank.feature_reminders.ui.list

import com.syfttny.watchmytank.domain.model.Reminder

/**
 * Defines the contract between the UI and the ViewModel for the Reminder List screen.
 */
object ReminderListContract {

    /**
     * Represents the current state of the Reminder List UI.
     */
    data class State(
        val isLoading: Boolean = true,
        val reminders: List<Reminder> = emptyList(),
        val error: String? = null 
    )

    /**
     * Represents user actions or intents initiated from the UI.
     */
    sealed interface Intent {
        object LoadReminders : Intent
        data class DeleteReminder(val reminderId: Long) : Intent
        data class EditReminder(val reminderId: Long) : Intent 
        object AddReminder : Intent 
    }

    /**
     * Represents one-time events sent from the ViewModel to the UI
     * (e.g., navigation triggers, showing snackbars).
     */
    sealed interface Event {
        data class NavigateToEditScreen(val reminderId: Long?) : Event 
        data class ShowErrorSnackbar(val message: String) : Event
        data class ShowUndoDeleteSnackbar(val reminderId: Long) : Event 
    }
} 