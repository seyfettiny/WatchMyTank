package com.syfttny.watchmytank.feature_parameters.ui.log

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme
import com.syfttny.watchmytank.domain.model.ParameterRangeDefaults
import com.syfttny.watchmytank.domain.model.ParameterType



data class LogParametersState(
    val isLoading: Boolean = false,
    val parameterValues: Map<ParameterType, String> = ParameterType.values().associateWith { "" },
    val notes: String = ""
)

sealed interface LogParametersEvent {
    object SaveClicked : LogParametersEvent
    data class ParameterValueChanged(val type: ParameterType, val value: String) : LogParametersEvent
    data class NotesChanged(val notes: String) : LogParametersEvent
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogParametersScreen(
    viewModel: LogParametersViewModel = hiltViewModel()
    
    
    
) {
    val state by viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent

    
    
    
    
    
    
    
    
    

    
    val parametersToShow = remember {
        listOf(
            ParameterType.TEMPERATURE,
            ParameterType.PH,
            ParameterType.AMMONIA,
            ParameterType.NITRITE,
            ParameterType.NITRATE
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Log Water Parameters") })
            // TODO: Add Back navigation button (e.g., using onNavigateBack)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(parametersToShow) { paramType ->
                        val rangeDef = ParameterRangeDefaults.defaultsMap[paramType]
                        if (rangeDef != null) {
                            ParameterLogItem(
                                parameterType = paramType,
                                rangeDefinition = rangeDef,
                                valueString = state.parameterValues[paramType] ?: "",
                                onValueChange = { newValue ->
                                    onEvent(LogParametersEvent.ParameterValueChanged(paramType, newValue))
                                },
                                
                                
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.notes,
                            onValueChange = { onEvent(LogParametersEvent.NotesChanged(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Notes (Optional)") },
                            maxLines = 4,
                            enabled = !state.isLoading
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onEvent(LogParametersEvent.SaveClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    enabled = !state.isLoading
                ) {
                    Text("Save Log")
                }
            }

            
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LogParametersScreenPreview() {
    WatchMyTankTheme {
        
        LogParametersScreen_PreviewLayout()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LogParametersScreen_PreviewLayout() {
    val parametersToShow = remember {
        listOf(
            ParameterType.TEMPERATURE,
            ParameterType.PH,
            ParameterType.AMMONIA,
            ParameterType.NITRITE,
            ParameterType.NITRATE
        )
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Log Water Parameters") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(parametersToShow) { paramType ->
                    val rangeDef = ParameterRangeDefaults.defaultsMap[paramType]
                    if (rangeDef != null) {
                        ParameterLogItem(
                            parameterType = paramType,
                            rangeDefinition = rangeDef,
                            valueString = "", 
                            onValueChange = { }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Notes (Optional)") },
                        maxLines = 4
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Save Log")
            }
        }
    }
} 