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
    private val savedStateHandle: SavedStateHandle 
) : ViewModel() {

    private val _state = MutableStateFlow(LogParametersState())
    val state: StateFlow<LogParametersState> = _state.asStateFlow()

    
    private val currentTankId = "DUMMY_TANK_ID"
    private val currentUserId = "DUMMY_USER_ID"

    
    
    

    fun onEvent(event: LogParametersEvent) {
        when (event) {
            is LogParametersEvent.ParameterValueChanged -> {
                
                val filteredValue = event.value.filter { it.isDigit() || it == '.' || it == '-' }
                
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
                
                
                println("Log saved successfully (ViewModel)") 
                
                

            } catch (e: NumberFormatException) {
                println("Save failed: Invalid number format - ${e.message}")
                
            } catch (e: IllegalArgumentException) {
                println("Save failed: ${e.message}")
                
            } catch (e: Exception) {
                println("Save failed: An unexpected error occurred - ${e.message}")
                
            }
            finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}


sealed interface LogUiEffect {
    object LogSavedSuccessfully : LogUiEffect
    data class ShowError(val message: String) : LogUiEffect
} 