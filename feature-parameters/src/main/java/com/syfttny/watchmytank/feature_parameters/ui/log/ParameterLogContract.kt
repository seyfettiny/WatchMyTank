package com.syfttny.watchmytank.feature_parameters.ui.log

import com.syfttny.watchmytank.domain.model.ParameterType
import java.time.LocalDateTime

/**
 * Defines the contract (State, Event, Intent) for the Parameter Log screen,
 * following the MVI pattern.
 */
interface ParameterLogContract {

    /**
     * Represents the current state of the Parameter Log UI.
     *
     * @param availableTypes List of parameter types the user can select from.
     * @param selectedType The currently chosen parameter type.
     * @param currentValue The current value entered by the user (as String for TextField).
     * @param currentNotes The current notes entered by the user.
     * @param selectedTimestamp The timestamp for the log entry (defaults to now, might be editable).
     * @param isLoading Indicates if a save operation is in progress.
     * @param error A message describing any error that occurred.
     */
    data class State(
        val availableTypes: List<ParameterType> = ParameterType.values().toList(),
        val selectedType: ParameterType = availableTypes.first(), 
        val currentValue: String = "",
        val currentNotes: String = "",
        val selectedTimestamp: LocalDateTime = LocalDateTime.now(), 
        val isLoading: Boolean = false,
        val error: String? = null
    )

    /**
     * Represents one-off events triggered by the ViewModel, usually for navigation or showing Snackbars.
     */
    sealed interface Event {
        data class ShowSnackbar(val message: String) : Event
        object NavigateBack : Event 
    }

    /**
     * Represents user actions or intentions on the UI.
     */
    sealed interface Intent {
        data class SelectParameterType(val type: ParameterType) : Intent
        data class UpdateValue(val value: String) : Intent
        data class UpdateNotes(val notes: String) : Intent
        
        object SaveLog : Intent
    }
} 