package com.syfttny.watchmytank.feature_parameters.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.use_case.GetParameterHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ParameterHistoryViewModel @Inject constructor(
    private val getParameterHistoryUseCase: GetParameterHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ParameterHistoryContract.State())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ParameterHistoryContract.Event>()
    val events = _eventChannel.receiveAsFlow()

    // Trigger to reload data when the selected type changes
    private val selectedTypeFlow = MutableStateFlow(_state.value.selectedType)

    init {
        // Observe the selected type and load data whenever it changes
        selectedTypeFlow
            .flatMapLatest { type -> loadHistoryForType(type) }
            .launchIn(viewModelScope)
    }

    fun handleIntent(intent: ParameterHistoryContract.Intent) {
        when (intent) {
            is ParameterHistoryContract.Intent.SelectFilterType -> {
                // Update state and trigger reload via selectedTypeFlow
                if (_state.value.selectedType != intent.type) {
                    _state.update { it.copy(selectedType = intent.type, isLoading = true, error = null) }
                    selectedTypeFlow.value = intent.type
                }
            }
        }
    }

    private fun loadHistoryForType(type: ParameterType): Flow<Unit> {
        return getParameterHistoryUseCase(type)
            .onEach { logs ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        historyLogs = logs,
                        error = null
                    )
                }
            }
            .catch { throwable ->
                val errorMessage = throwable.localizedMessage ?: "Failed to load history for ${type.displayName}"
                _state.update {
                    it.copy(
                        isLoading = false,
                        historyLogs = emptyList(), // Clear logs on error
                        error = errorMessage
                    )
                }
                _eventChannel.send(ParameterHistoryContract.Event.ShowErrorSnackbar(errorMessage))
            }
            .map { Unit } // Map to Flow<Unit> for flatMapLatest
    }
} 