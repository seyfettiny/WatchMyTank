package com.syfttny.watchmytank.feature_reminders.ui.edit

import com.syfttny.watchmytank.domain.model.ReminderType

/**
 * Defines the contract between the UI (Screen) and the ViewModel
 * for the Reminder Edit screen, following MVI pattern.
 */
interface ReminderEditContract {

    /**
     * Represents the current state of the Reminder Edit screen.
     */
    data class State(
        val isLoading: Boolean = false,
        val isEditing: Boolean = false, // True if editing an existing reminder
        val reminderId: Long? = null,
        val name: String = "",
        val type: ReminderType = ReminderType.DAILY, // Default type
        val frequencyDays: String = "", // Store as String for TextField
        val cronExpression: String = "",
        val isEnabled: Boolean = true, // <-- Add isEnabled flag
        val triggerHour: Int? = null, // Added: Hour for editing
        val triggerMinute: Int? = null, // Added: Minute for editing
        val error: String? = null // General error message
    )

    /**
     * Represents user actions or intents initiated from the UI.
     */
    sealed interface Intent {
        data class LoadReminder(val reminderId: Long?) : Intent
        data class UpdateName(val name: String) : Intent
        data class SelectType(val type: ReminderType) : Intent
        data class UpdateFrequencyDays(val days: String) : Intent
        data class UpdateCronExpression(val expression: String) : Intent
        data class UpdateEnabled(val enabled: Boolean) : Intent // <-- Add Intent for isEnabled
        data class UpdateTime(val hour: Int, val minute: Int): Intent // Added: Intent for time update
        object SaveReminder : Intent
    }

    /**
     * Represents one-off events triggered by the ViewModel, usually for navigation or showing transient messages.
     */
    sealed interface Event {
        object NavigateBack : Event
        data class ShowError(val message: String) : Event
    }
} 