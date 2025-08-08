@file:OptIn(ExperimentalMaterial3Api::class)

package com.syfttny.watchmytank.feature_parameters.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme
import com.syfttny.watchmytank.domain.model.ParameterType
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParameterHistoryScreen(
    viewModel: ParameterHistoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToLogParameter: () -> Unit 
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    
    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ParameterHistoryContract.Event.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Parameter Charts") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToLogParameter) {
                Icon(Icons.Filled.Add, contentDescription = "Log New Parameter Set")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text("Error loading history: ${state.error}")
                state.chartDataProducers.isEmpty() && !state.isLoading -> Text("No history data available.")
                else -> {
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.availableChartTypes) { paramType ->
                            val producer = state.chartDataProducers[paramType]
                            if (producer != null) {
                                ParameterChartCard( 
                                    title = "${paramType.displayName} (${paramType.unit})",
                                    producer = producer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ParameterChartCard(
    title: String,
    producer: ChartEntryModelProducer,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Chart(
                chart = lineChart(),
                chartModelProducer = producer,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
                modifier = Modifier.height(200.dp) 
                // TODO: Add marker, legends, axis formatters etc. for better UX
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ParameterHistoryScreenPreview() {
    WatchMyTankTheme {
        
        Scaffold(
            topBar = { TopAppBar(title = { Text("Parameter Charts") }) },
            floatingActionButton = { FloatingActionButton(onClick = {}) { Icon(Icons.Filled.Add, "") } }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chart Area Placeholder")
            }
        }
    }
} 