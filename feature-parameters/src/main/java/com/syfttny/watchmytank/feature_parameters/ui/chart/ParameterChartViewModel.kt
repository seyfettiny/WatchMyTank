package com.syfttny.watchmytank.feature_parameters.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import com.syfttny.watchmytank.domain.use_case.GetParameterHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ParameterChartViewModel @Inject constructor(
    private val getParameterHistoryUseCase: GetParameterHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ParameterChartContract.State())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ParameterChartContract.Event>()
    val events = _eventChannel.receiveAsFlow()

    private val chartEntryModelProducer = ChartEntryModelProducer()

    // Trigger to reload data when the selected type changes
    private val selectedTypeFlow = MutableStateFlow(_state.value.selectedType)

    init {
        _state.update { it.copy(chartModelProducer = chartEntryModelProducer) } // Assign producer to state

        // Observe the selected type and load data whenever it changes
        selectedTypeFlow
            .flatMapLatest { type -> loadHistoryAndPrepareChart(type) }
            .launchIn(viewModelScope)
    }

    fun handleIntent(intent: ParameterChartContract.Intent) {
        when (intent) {
            is ParameterChartContract.Intent.SelectParameterType -> {
                if (_state.value.selectedType != intent.type) {
                    _state.update { it.copy(selectedType = intent.type, isLoading = true, error = null, isEmpty = false) }
                    selectedTypeFlow.value = intent.type
                }
            }
        }
    }

    private fun loadHistoryAndPrepareChart(type: ParameterType): Flow<Unit> {
        return getParameterHistoryUseCase(type)
            .map { logs -> mapLogsToChartEntries(logs.reversed()) } // Reverse logs for chronological order on chart
            .onEach { entries ->
                val isEmpty = entries.isEmpty()
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        isEmpty = isEmpty
                    )
                }
                // Update the chart producer with the new entries
                chartEntryModelProducer.setEntries(entries)
            }
            .catch { throwable ->
                handleLoadError(throwable, type)
            }
            .map { Unit } // Map to Flow<Unit> for flatMapLatest
    }

    private fun mapLogsToChartEntries(logs: List<WaterParameterLog>): List<FloatEntry> {
        // Using epoch seconds as X value. Alternatively, use simple index (0, 1, 2...). Axis formatters can handle labels.
        return logs.map {
            FloatEntry(
                x = it.timestamp.toEpochSecond(ZoneOffset.UTC).toFloat(), // X: Timestamp
                y = it.value.toFloat() // Y: Parameter Value
            )
        }
    }

    private suspend fun handleLoadError(throwable: Throwable, type: ParameterType) {
        val errorMessage = throwable.localizedMessage ?: "Failed to load chart data for ${type.displayName}"
        _state.update {
            it.copy(
                isLoading = false,
                error = errorMessage,
                isEmpty = true // Consider empty on error
            )
        }
        chartEntryModelProducer.setEntries(emptyList<FloatEntry>()) // Clear chart on error
        _eventChannel.send(ParameterChartContract.Event.ShowErrorSnackbar(errorMessage))
    }
} 