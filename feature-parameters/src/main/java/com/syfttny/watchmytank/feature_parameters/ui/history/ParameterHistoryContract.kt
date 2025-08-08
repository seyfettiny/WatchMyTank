package com.syfttny.watchmytank.feature_parameters.ui.history

import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.model.ParameterType

interface ParameterHistoryContract {
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
        ) 
    )

    sealed interface Event {
        data class ShowErrorSnackbar(val message: String) : Event
    }

    sealed interface Intent {
        // TODO: Add intents for changing time range, etc.
        // object LoadData : Intent // Could be used if manual refresh is needed
    }
} 