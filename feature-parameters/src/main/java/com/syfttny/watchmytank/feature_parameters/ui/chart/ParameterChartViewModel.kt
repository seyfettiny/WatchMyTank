package com.syfttny.watchmytank.feature_parameters.ui.chart

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
import java.time.ZoneOffset
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ParameterChartViewModel @Inject constructor(
    private val getParameterLogSetsUseCase: GetParameterLogSetsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tankId: String = savedStateHandle.get<String>("tankId")
        ?: throw IllegalStateException("tankId not found in navigation arguments for ParameterChart")

    private val _state = MutableStateFlow(ParameterChartContract.State())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ParameterChartContract.Event>()
    val events = _eventChannel.receiveAsFlow()

    private val chartEntryModelProducer = ChartEntryModelProducer()

    private var fullParameterHistory: List<ParameterLog> = emptyList()

    init {
        _state.update { it.copy(chartModelProducer = chartEntryModelProducer) }
        loadFullHistoryForTank()

        viewModelScope.launch {
            state.map { it.selectedType }.distinctUntilChanged().collect { type ->
                updateChartEntries(fullParameterHistory, type)
            }
        }
    }

    fun handleIntent(intent: ParameterChartContract.Intent) {
        when (intent) {
            is ParameterChartContract.Intent.SelectParameterType -> {
                if (_state.value.selectedType != intent.type) {
                    _state.update { it.copy(selectedType = intent.type, error = null) }
                }
            }
        }
    }

    private fun loadFullHistoryForTank() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getParameterLogSetsUseCase(tankId)
                .catch { throwable ->
                    fullParameterHistory = emptyList()
                    updateChartEntries(fullParameterHistory, _state.value.selectedType)
                    handleLoadError(throwable, _state.value.selectedType)
                }
                .collect { logs ->
                    fullParameterHistory = logs.reversed()
                    _state.update { it.copy(isLoading = false, error = null) }
                    updateChartEntries(fullParameterHistory, _state.value.selectedType)
                }
        }
    }

    private fun updateChartEntries(logs: List<ParameterLog>, type: ParameterType) {
        val entries = mapLogsToChartEntries(logs, type)
        val isEmpty = entries.isEmpty() && logs.isNotEmpty()
        _state.update {
            it.copy(
                isEmpty = isEmpty
            )
        }
        chartEntryModelProducer.setEntries(entries)
    }

    private fun mapLogsToChartEntries(logs: List<ParameterLog>, type: ParameterType): List<FloatEntry> {
        return logs.mapNotNull { log ->
            log.parameters[type]?.let { value ->
                FloatEntry(
                    x = log.timestamp.toEpochMilli().toFloat(),
                    y = value.toFloat()
                )
            }
        }
    }

    private suspend fun handleLoadError(throwable: Throwable, type: ParameterType) {
        val errorMessage = throwable.localizedMessage ?: "Failed to load chart data for tank $tankId"
        _state.update {
            it.copy(
                isLoading = false,
                error = errorMessage,
                isEmpty = true
            )
        }
        chartEntryModelProducer.setEntries(emptyList<FloatEntry>())
        _eventChannel.send(ParameterChartContract.Event.ShowErrorSnackbar(errorMessage))
    }
} 