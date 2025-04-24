package com.syfttny.watchmytank.feature_parameters.ui.history

import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog

/**
 * Defines the contract (State, Event, Intent) for the Parameter History screen,
 * following the MVI pattern.
 */
interface ParameterHistoryContract {

    /**
     * Represents the current state of the Parameter History UI.
     *
     * @param availableTypes List of parameter types the user can filter by.
     * @param selectedType The currently selected filter type.
     * @param historyLogs List of logs matching the selected filter.
     * @param isLoading Indicates if history data is being loaded.
     * @param error A message describing any error that occurred during loading.
     * @param unsyncedCount Track count of logs pending sync
     */
    data class State(
        val availableTypes: List<ParameterType> = ParameterType.values().toList(),
        val selectedType: ParameterType = availableTypes.first(), // Default filter
        val historyLogs: List<WaterParameterLog> = emptyList(),
        val isLoading: Boolean = true, // Start in loading state
        val error: String? = null,
        val unsyncedCount: Int = 0 // Track count of logs pending sync
    )

    /**
     * Represents one-off events triggered by the ViewModel.
     */
    sealed interface Event {
        // Example: Navigate to log details if needed
        // data class NavigateToLogDetail(val logId: Long) : Event
        data class ShowErrorSnackbar(val message: String) : Event
    }

    /**
     * Represents user actions or intentions on the UI.
     */
    sealed interface Intent {
        data class SelectFilterType(val type: ParameterType) : Intent
        // Add other intents like DeleteLog if needed
    }
} 