package com.syfttny.watchmytank.feature_parameters.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import com.syfttny.watchmytank.domain.use_case.LogParameterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ParameterLogViewModel @Inject constructor(
    private val logParameterUseCase: LogParameterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ParameterLogContract.State())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ParameterLogContract.Event>()
    val events = _eventChannel.receiveAsFlow()

    fun handleIntent(intent: ParameterLogContract.Intent) {
        when (intent) {
            is ParameterLogContract.Intent.SelectParameterType -> {
                _state.update { it.copy(selectedType = intent.type, error = null) }
            }
            is ParameterLogContract.Intent.UpdateValue -> {
                // Allow only numbers and a single decimal point
                val filteredValue = intent.value.filter { it.isDigit() || it == '.' }
                // Ensure only one decimal point
                val finalValue = if (filteredValue.count { it == '.' } > 1) {
                    filteredValue.substring(0, filteredValue.lastIndexOf('.'))
                } else {
                    filteredValue
                }
                _state.update { it.copy(currentValue = finalValue, error = null) }
            }
            is ParameterLogContract.Intent.UpdateNotes -> {
                _state.update { it.copy(currentNotes = intent.notes) }
            }
            ParameterLogContract.Intent.SaveLog -> saveLog()
        }
    }

    private fun saveLog() {
        val currentState = _state.value
        val valueDouble = currentState.currentValue.toDoubleOrNull()

        // Validation
        if (currentState.currentValue.isBlank() || valueDouble == null) {
            _state.update { it.copy(error = "Please enter a valid number for the value.") }
            return
        }
        // TODO: Add more specific validation based on ParameterType (e.g., pH range)

        _state.update { it.copy(isLoading = true, error = null) }

        val logEntry = WaterParameterLog(
            timestamp = LocalDateTime.now(), // Or currentState.selectedTimestamp if editable
            parameterType = currentState.selectedType,
            value = valueDouble,
            notes = currentState.currentNotes.takeIf { it.isNotBlank() } // Save null if notes are blank
        )

        viewModelScope.launch {
            try {
                logParameterUseCase(logEntry)
                _state.update { it.copy(isLoading = false) }
                _eventChannel.send(ParameterLogContract.Event.ShowSnackbar("${currentState.selectedType.displayName} logged successfully."))
                _eventChannel.send(ParameterLogContract.Event.NavigateBack) // Navigate back after successful save
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Failed to save log.") }
                _eventChannel.send(ParameterLogContract.Event.ShowSnackbar(e.localizedMessage ?: "Failed to save log"))
            }
        }
    }
} 