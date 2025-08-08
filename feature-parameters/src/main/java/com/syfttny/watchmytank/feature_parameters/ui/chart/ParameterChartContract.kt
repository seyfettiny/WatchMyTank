package com.syfttny.watchmytank.feature_parameters.ui.chart

import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.syfttny.watchmytank.domain.model.ParameterType

interface ParameterChartContract {
    data class State(
        val availableTypes: List<ParameterType> = ParameterType.values().toList(),
        val selectedType: ParameterType = availableTypes.first(),
        val chartModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
        val isLoading: Boolean = true,
        val error: String? = null,
        val isEmpty: Boolean = false
    )

    sealed interface Event {
        data class ShowErrorSnackbar(val message: String) : Event
        
    }

    sealed interface Intent {
        data class SelectParameterType(val type: ParameterType) : Intent
        
    }
} 