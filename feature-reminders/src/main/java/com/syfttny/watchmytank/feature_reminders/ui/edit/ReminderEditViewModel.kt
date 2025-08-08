package com.syfttny.watchmytank.feature_reminders.ui.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.model.ReminderType
import com.syfttny.watchmytank.domain.use_case.AddReminderUseCase
import com.syfttny.watchmytank.domain.use_case.GetReminderUseCase
import com.syfttny.watchmytank.domain.use_case.UpdateReminderUseCase
import com.syfttny.watchmytank.domain.scheduler.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReminderEditViewModel @Inject constructor(
    
    private val getReminderUseCase: GetReminderUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(ReminderEditContract.State())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ReminderEditContract.Event>()
    val events = _eventChannel.receiveAsFlow()

    fun handleIntent(intent: ReminderEditContract.Intent) {
        when (intent) {
            is ReminderEditContract.Intent.LoadReminder -> loadReminder(intent.reminderId)
            is ReminderEditContract.Intent.UpdateName -> _state.update { it.copy(name = intent.name, error = null) }
            is ReminderEditContract.Intent.SelectType -> _state.update { it.copy(type = intent.type, error = null) }
            is ReminderEditContract.Intent.UpdateFrequencyDays -> {
                
                val digitsOnly = intent.days.filter { it.isDigit() }
                _state.update { it.copy(frequencyDays = digitsOnly, error = null) }
            }
            is ReminderEditContract.Intent.UpdateCronExpression -> _state.update { it.copy(cronExpression = intent.expression, error = null) }
            ReminderEditContract.Intent.SaveReminder -> saveReminder()
            is ReminderEditContract.Intent.UpdateEnabled -> {
                val isEnabled = intent.enabled
                _state.update { it.copy(isEnabled = isEnabled) }
                
                val reminderId = _state.value.reminderId
                if (!isEnabled && reminderId != null && reminderId > 0) {
                    viewModelScope.launch { 
                        reminderScheduler.cancel(reminderId)
                    }
                }
                
            }
            is ReminderEditContract.Intent.UpdateTime -> {
                _state.update { it.copy(triggerHour = intent.hour, triggerMinute = intent.minute, error = null) }
            }
        }
    }

    private fun loadReminder(reminderId: Long?) {
        if (reminderId == null || reminderId <= 0) {
            
            _state.update { ReminderEditContract.State(isEditing = false) }
            return
        }

        _state.update { it.copy(isLoading = true, isEditing = true, reminderId = reminderId) }
        viewModelScope.launch {
            // TODO: Implement loading logic using GetReminderUseCase
            try {
                
                if (reminderId == null || reminderId <= 0) {
                     _state.update { it.copy(isLoading = false, error = "Invalid Reminder ID for loading") }
                    _eventChannel.send(ReminderEditContract.Event.ShowError("Invalid Reminder ID"))
                    return@launch 
                }

                val reminder = getReminderUseCase(reminderId)
                if (reminder != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            reminderId = reminder.id,
                            isEditing = true,
                            name = reminder.name,
                            type = reminder.type,
                            frequencyDays = reminder.frequencyDays?.toString() ?: "",
                            cronExpression = reminder.cronExpression ?: "",
                            isEnabled = reminder.isEnabled,
                            triggerHour = reminder.triggerHour ?: 9,
                            triggerMinute = reminder.triggerMinute ?: 0,
                            error = null
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Reminder not found") }
                    _eventChannel.send(ReminderEditContract.Event.ShowError("Reminder not found"))
                    
                    
                }
            } catch (e: Exception) {
                Log.e("ReminderEditViewModel", "Error loading reminder", e) 
                _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load reminder") }
                _eventChannel.send(ReminderEditContract.Event.ShowError(e.message ?: "Failed to load reminder"))
            }
        }
    }

    private fun saveReminder() {
        val currentState = _state.value

        
        if (currentState.name.isBlank()) {
            _state.update { it.copy(error = "Name cannot be empty") }
            return
        }
        val frequencyDaysInt = currentState.frequencyDays.toIntOrNull()
        if (currentState.type == ReminderType.EVERY_N_DAYS && (frequencyDaysInt == null || frequencyDaysInt <= 0)) {
            _state.update { it.copy(error = "Frequency must be a positive number") }
            return
        }
        if (currentState.type == ReminderType.CRON && currentState.cronExpression.isBlank()) {
            // TODO: (CRON Validation) Implement robust CRON expression validation beyond just isBlank().
            _state.update { it.copy(error = "CRON expression cannot be empty") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            // TODO: Use AddReminderUseCase or UpdateReminderUseCase based on isEditing
            try {
                var savedReminder: Reminder? = null 

                
                val reminderToSave = Reminder(
                    id = currentState.reminderId ?: 0,
                    name = currentState.name.trim(),
                    type = currentState.type,
                    frequencyDays = if (currentState.type == ReminderType.EVERY_N_DAYS) currentState.frequencyDays.toIntOrNull() else null,
                    cronExpression = if (currentState.type == ReminderType.CRON) currentState.cronExpression.trim() else null,
                    // TODO: Preserve original creationTime on updates. Load it in loadReminder and pass it here.
                    creationTime = LocalDateTime.now(), // TODO: Should preserve original creationTime on updates
                    isEnabled = currentState.isEnabled,
                    triggerHour = currentState.triggerHour,
                    triggerMinute = currentState.triggerMinute,
                    // Temporary assignment, will be recalculated before scheduling / or by scheduler
                    nextTriggerTime = LocalDateTime.now().plusHours(1)
                )

                if (currentState.reminderId != null && currentState.reminderId > 0) {
                    
                    updateReminderUseCase(reminderToSave) 
                    
                    savedReminder = reminderToSave
                } else {
                    
                    
                    val newId = addReminderUseCase(reminderToSave.copy(id = 0))
                    
                    
                    if (newId > 0) { 
                        savedReminder = reminderToSave.copy(id = newId) 
                    } else {
                        Log.e("ReminderEditViewModel", "Failed to get valid ID after adding reminder.")
                        
                    }
                }

                
                if (savedReminder != null) {
                    
                    
                    reminderScheduler.schedule(savedReminder)
                } else {
                    Log.e("ReminderEditViewModel", "Scheduling skipped because savedReminder object is null.")
                    
                }

                _state.update { it.copy(isLoading = false) }
                _eventChannel.send(ReminderEditContract.Event.NavigateBack)
            } catch (e: Exception) {
                Log.e("ReminderEditViewModel", "Error saving reminder", e)
                _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to save reminder") }
                _eventChannel.send(ReminderEditContract.Event.ShowError(e.message ?: "Failed to save reminder"))
            }
        }
    }
} 