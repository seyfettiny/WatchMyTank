package com.syfttny.watchmytank.feature_parameters.ui.log

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syfttny.watchmytank.domain.model.ParameterType
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.tooling.preview.Preview
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParameterLogScreen(
    viewModel: ParameterLogViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    
    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ParameterLogContract.Event.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
                ParameterLogContract.Event.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Log Water Parameter") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp))
                    } else {
                        IconButton(onClick = { viewModel.handleIntent(ParameterLogContract.Intent.SaveLog) }) {
                            Icon(Icons.Default.Check, contentDescription = "Save Log")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        ParameterLogContent(
            state = state,
            onIntent = viewModel::handleIntent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParameterLogContent(
    state: ParameterLogContract.State,
    onIntent: (ParameterLogContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), 
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        ParameterTypeSelector(
            availableTypes = state.availableTypes,
            selectedType = state.selectedType,
            onTypeSelected = { onIntent(ParameterLogContract.Intent.SelectParameterType(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        
        OutlinedTextField(
            value = state.currentValue,
            onValueChange = { onIntent(ParameterLogContract.Intent.UpdateValue(it)) },
            label = { Text("Value (${state.selectedType.unit})") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = state.error?.contains("value", ignoreCase = true) == true
        )

        
        OutlinedTextField(
            value = state.currentNotes,
            onValueChange = { onIntent(ParameterLogContract.Intent.UpdateNotes(it)) },
            label = { Text("Notes (Optional)") },
            modifier = Modifier.fillMaxWidth().height(120.dp), 
            maxLines = 5
        )

        
        state.error?.let {
            if (it.contains("value", ignoreCase = true).not()) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParameterTypeSelector(
    availableTypes: List<ParameterType>,
    selectedType: ParameterType,
    onTypeSelected: (ParameterType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedType.displayName,
            onValueChange = {}, 
            readOnly = true,
            label = { Text("Parameter Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth() 
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Log Screen Content")
@Composable
private fun ParameterLogContentPreview() {
    WatchMyTankTheme {
        ParameterLogContent(
            state = ParameterLogContract.State(), 
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Log Screen Content - Error")
@Composable
private fun ParameterLogContentWithErrorPreview() {
    WatchMyTankTheme {
        ParameterLogContent(
            state = ParameterLogContract.State(error = "Invalid input"),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Parameter Type Selector")
@Composable
private fun ParameterTypeSelectorPreview() {
    WatchMyTankTheme {
        ParameterTypeSelector(
            availableTypes = ParameterType.values().toList(),
            selectedType = ParameterType.PH,
            onTypeSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

// TODO: Add Composable Previews for ParameterLogScreen/Content 