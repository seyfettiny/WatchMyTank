package com.syfttny.watchmytank.feature_parameters.ui.history

import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.model.ParameterType

/**
 * Defines the contract (State, Event, Intent) for the Parameter History/Chart screen.
 */
interface ParameterHistoryContract {

    /**
     * Represents the current state of the Parameter History/Chart UI.
     *
     * @param isLoading Indicates if data is being loaded.
     * @param error A message describing any error that occurred during loading.
     * @param chartDataProducers A map holding the data producers for each parameter type's chart.
     *                         The key is the ParameterType, value is the producer for Vico.
     * @param availableChartTypes The list of parameter types for which charts are available/configured.
     */
    data class State(
        val isLoading: Boolean = true,
        val error: String? = null,
        val chartDataProducers: Map<ParameterType, ChartEntryModelProducer> = emptyMap(),
        val availableChartTypes: List<ParameterType> = listOf(
            ParameterType.TEMPERATURE,
            ParameterType.PH,
            ParameterType.AMMONIA,
            ParameterType.NITRITE,
            ParameterType.NITRATE
        ) // Default types to display
    )

    /**
     * Represents one-off events triggered by the ViewModel.
     */
    sealed interface Event {
        data class ShowErrorSnackbar(val message: String) : Event
    }

    /**
     * Represents user actions or intentions on the UI.
     */
    sealed interface Intent {
        // TODO: Add intents for changing time range, etc.
        // object LoadData : Intent // Could be used if manual refresh is needed
    }
} 