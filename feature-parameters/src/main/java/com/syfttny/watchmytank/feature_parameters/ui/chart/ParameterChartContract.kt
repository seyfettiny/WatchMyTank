package com.syfttny.watchmytank.feature_parameters.ui.chart

import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.syfttny.watchmytank.domain.model.ParameterType

/**
 * Defines the contract (State, Event, Intent) for the Parameter Chart screen.
 */
interface ParameterChartContract {

    /**
     * Represents the current state of the Parameter Chart UI.
     *
     * @param availableTypes List of parameter types the user can select to chart.
     * @param selectedType The currently selected parameter type.
     * @param chartModelProducer Produces the data model for the Vico chart.
     * @param isLoading Indicates if chart data is being loaded.
     * @param error A message describing any error that occurred.
     * @param isEmpty Indicates if there is no data to display for the selected type.
     */
    data class State(
        val availableTypes: List<ParameterType> = ParameterType.values().toList(),
        val selectedType: ParameterType = availableTypes.first(),
        val chartModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
        val isLoading: Boolean = true,
        val error: String? = null,
        val isEmpty: Boolean = false
    )

    /**
     * Represents one-off events triggered by the ViewModel.
     */
    sealed interface Event {
        data class ShowErrorSnackbar(val message: String) : Event
        // Add other events if needed (e.g., for interactions with the chart)
    }

    /**
     * Represents user actions or intentions on the UI.
     */
    sealed interface Intent {
        data class SelectParameterType(val type: ParameterType) : Intent
        // Add intents for zoom/pan/date range later if needed
    }
} 