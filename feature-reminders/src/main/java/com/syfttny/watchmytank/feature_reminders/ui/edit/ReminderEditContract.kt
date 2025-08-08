package com.syfttny.watchmytank.feature_reminders.ui.edit

import com.syfttny.watchmytank.domain.model.ReminderType

interface ReminderEditContract {

    data class State(
        val isLoading: Boolean = false,
        val isEditing: Boolean = false, 
        val reminderId: Long? = null,
        val name: String = "",
        val type: ReminderType = ReminderType.DAILY, 
        val frequencyDays: String = "", 
        val cronExpression: String = "",
        val isEnabled: Boolean = true, 
        val triggerHour: Int? = null, 
        val triggerMinute: Int? = null, 
        val error: String? = null 
    )

    sealed interface Intent {
        data class LoadReminder(val reminderId: Long?) : Intent
        data class UpdateName(val name: String) : Intent
        data class SelectType(val type: ReminderType) : Intent
        data class UpdateFrequencyDays(val days: String) : Intent
        data class UpdateCronExpression(val expression: String) : Intent
        data class UpdateEnabled(val enabled: Boolean) : Intent 
        data class UpdateTime(val hour: Int, val minute: Int): Intent 
        object SaveReminder : Intent
    }

    sealed interface Event {
        object NavigateBack : Event
        data class ShowError(val message: String) : Event
    }
} 