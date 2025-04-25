package com.syfttny.watchmytank.feature_parameters.ui.log

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.use_case.SaveParameterLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogParametersViewModel @Inject constructor(
    private val saveParameterLogUseCase: SaveParameterLogUseCase,
    private val savedStateHandle: SavedStateHandle // For potentially getting tankId/userId
) : ViewModel() {

    private val _state = MutableStateFlow(LogParametersState())
    val state: StateFlow<LogParametersState> = _state.asStateFlow()

    // TODO: Get actual tankId and userId (e.g., from SavedStateHandle or global state)
    private val currentTankId = "DUMMY_TANK_ID"
    private val currentUserId = "DUMMY_USER_ID"

    // Consider adding a SharedFlow for single-shot UI events like navigation or toasts
    // private val _uiEffect = MutableSharedFlow<LogUiEffect>()
    // val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: LogParametersEvent) {
        when (event) {
            is LogParametersEvent.ParameterValueChanged -> {
                // Basic input validation (allow empty, dot, digits)
                val filteredValue = event.value.filter { it.isDigit() || it == '.' || it == '-' }
                // Prevent multiple dots
                if (filteredValue.count { it == '.' } <= 1) {
                    _state.update {
                        it.copy(parameterValues = it.parameterValues + (event.type to filteredValue))
                    }
                }
            }
            is LogParametersEvent.NotesChanged -> {
                _state.update { it.copy(notes = event.notes) }
            }
            LogParametersEvent.SaveClicked -> {
                saveLog()
            }
        }
    }

    private fun saveLog() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                saveParameterLogUseCase(
                    tankId = currentTankId,
                    userId = currentUserId,
                    parameterValues = _state.value.parameterValues,
                    notes = _state.value.notes
                )
                // Success: Navigate back or show success message
                // _uiEffect.emit(LogUiEffect.LogSavedSuccessfully)
                println("Log saved successfully (ViewModel)") // Placeholder
                // Optionally clear the state after successful save
                // _state.value = LogParametersState()

            } catch (e: NumberFormatException) {
                println("Save failed: Invalid number format - ${e.message}")
                // _uiEffect.emit(LogUiEffect.ShowError("Invalid number format: ${e.message}"))
            } catch (e: IllegalArgumentException) {
                println("Save failed: ${e.message}")
                // _uiEffect.emit(LogUiEffect.ShowError("Error: ${e.message}"))
            } catch (e: Exception) {
                println("Save failed: An unexpected error occurred - ${e.message}")
                // _uiEffect.emit(LogUiEffect.ShowError("An unexpected error occurred."))
            }
            finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

// Example for UI effects (optional)
sealed interface LogUiEffect {
    object LogSavedSuccessfully : LogUiEffect
    data class ShowError(val message: String) : LogUiEffect
} 