package com.syfttny.watchmytank.feature_parameters.ui.history

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.use_case.GetParameterLogSetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ParameterHistoryViewModel @Inject constructor(
    getParameterLogSetsUseCase: GetParameterLogSetsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentTankId: String = savedStateHandle.get<String>("tankId") ?: "DUMMY_TANK_ID"

    private val _eventChannel = Channel<ParameterHistoryContract.Event>()
    val events: Flow<ParameterHistoryContract.Event> = _eventChannel.receiveAsFlow()

    // Flow for raw data and loading/error state
    private val _logsDataFlow: StateFlow<LogsDataResult> = flow {
        if (currentTankId == "DUMMY_TANK_ID") {
            emit(LogsDataResult.Error("Tank ID not provided."))
            return@flow
        }
        // Emit loading first
        emit(LogsDataResult.Loading)
        // Collect logs from the use case
        getParameterLogSetsUseCase(currentTankId)
            .catch { e -> emit(LogsDataResult.Error(e.localizedMessage ?: "Failed to load parameter history")) }
            .collect { logs -> emit(LogsDataResult.Success(logs)) }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LogsDataResult.Loading // Start in loading state
    )

    // StateFlow for the UI state, derived from the data flow
    val state: StateFlow<ParameterHistoryContract.State> = _logsDataFlow.map { result ->
        val isLoading = result is LogsDataResult.Loading
        val error = (result as? LogsDataResult.Error)?.message
        val logs = (result as? LogsDataResult.Success)?.logs ?: emptyList()

        // Process logs into chart data only when successful
        val chartProducers = if (result is LogsDataResult.Success) {
            processLogsForCharts(result.logs)
        } else {
            // Return empty producers if loading or error, preserving previous ones might be complex here
            emptyMap()
        }

        ParameterHistoryContract.State(
            isLoading = isLoading,
            error = error,
            chartDataProducers = chartProducers
            // availableChartTypes remains constant for now
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ParameterHistoryContract.State() // Initial UI state
    )

    // Helper function to process logs, now returns the map
    private fun processLogsForCharts(logs: List<ParameterLog>): Map<ParameterType, ChartEntryModelProducer> {
        Log.d("ParameterHistoryVM", "Processing ${logs.size} log sets for charts.")
        val producers = mutableMapOf<ParameterType, ChartEntryModelProducer>()
        val allEntries = mutableMapOf<ParameterType, MutableList<FloatEntry>>()
        val availableTypes = ParameterHistoryContract.State().availableChartTypes // Get default types

        availableTypes.forEach {
            producers[it] = ChartEntryModelProducer()
            allEntries[it] = mutableListOf()
        }

        val sortedLogs = logs.sortedBy { it.timestamp }

        sortedLogs.forEachIndexed { index, log ->
            val xValue = index.toFloat()
            log.parameters.forEach { (type, value) ->
                if (type in availableTypes) {
                    allEntries[type]?.add(FloatEntry(x = xValue, y = value.toFloat()))
                }
            }
        }

        allEntries.forEach { (type, entries) ->
            producers[type]?.setEntries(entries)
            Log.d("ParameterHistoryVM", "Set ${entries.size} entries for $type")
        }
        Log.d("ParameterHistoryVM", "Finished processing chart data.")
        return producers
    }

    // Handle error helper (can be used by specific actions if needed, otherwise handled by flow)
    private suspend fun handleError(message: String) {
        Log.e("ParameterHistoryVM", "Error: $message")
        // Error state is now handled by the state flow mapping
        _eventChannel.send(ParameterHistoryContract.Event.ShowErrorSnackbar(message))
    }
}

// Sealed class to represent the result of loading logs
private sealed class LogsDataResult {
    object Loading : LogsDataResult()
    data class Success(val logs: List<ParameterLog>) : LogsDataResult()
    data class Error(val message: String) : LogsDataResult()
} 