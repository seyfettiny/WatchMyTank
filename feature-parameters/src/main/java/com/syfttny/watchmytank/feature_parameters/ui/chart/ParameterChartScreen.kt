package com.syfttny.watchmytank.feature_parameters.ui.chart

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.syfttny.watchmytank.domain.model.ParameterType
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.syfttny.watchmytank.core.ui.theme.WatchMyTankTheme
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import com.syfttny.watchmytank.core.ui.components.InfoStateView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParameterChartScreen(
    viewModel: ParameterChartViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-off events
    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ParameterChartContract.Event.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("${state.selectedType.displayName} Chart") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            // Parameter Type Selector Tabs
            ParameterChartTypeSelectorTabs(
                availableTypes = state.availableTypes,
                selectedType = state.selectedType,
                onTypeSelected = { viewModel.handleIntent(ParameterChartContract.Intent.SelectParameterType(it)) }
            )

            Divider()

            // Chart Content Area
            Box(
                modifier = Modifier.weight(1f).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }
                    state.error != null -> {
                        InfoStateView(
                            message = "Error loading chart data: \n${state.error}",
                            icon = Icons.Default.Warning
                            // Optionally add retry
                        )
                    }
                    state.isEmpty -> {
                        InfoStateView(
                            message = "No data available to display chart for ${state.selectedType.displayName}.",
                            icon = Icons.Default.Info
                        )
                    }
                    else -> {
                        // Vico Chart
                        val bottomAxisValueFormatter = remember { provideTimestampAxisValueFormatter() }
                        Chart(
                            chart = lineChart(),
                            chartModelProducer = state.chartModelProducer,
                            startAxis = rememberStartAxis(title = state.selectedType.unit),
                            bottomAxis = rememberBottomAxis(
                                title = "Time",
                                valueFormatter = bottomAxisValueFormatter
                                // Consider adding tickCount or other customizations
                            ),
                            modifier = Modifier.fillMaxSize()
                            // Add zoom/pan modifiers later if needed
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ParameterChartTypeSelectorTabs(
    availableTypes: List<ParameterType>,
    selectedType: ParameterType,
    onTypeSelected: (ParameterType) -> Unit
) {
    val selectedIndex = availableTypes.indexOf(selectedType)
    if (selectedIndex == -1) return

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 8.dp
    ) {
        availableTypes.forEachIndexed { index, type ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTypeSelected(type) },
                text = { Text(type.displayName) }
            )
        }
    }
}

/**
 * Provides an AxisValueFormatter for the bottom axis (timestamps).
 */
private fun provideTimestampAxisValueFormatter(): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    // Formatter to display date/time nicely on the axis
    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault()) // Use system default time zone for display

    return AxisValueFormatter { value, _ ->
        // Convert the Float value (epoch seconds) back to Instant -> LocalDateTime
        try {
            val instant = Instant.ofEpochSecond(value.toLong())
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(dateTimeFormatter)
        } catch (e: Exception) {
            // Fallback if conversion fails
            value.toString()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Chart Screen - Loading")
@Composable
private fun ParameterChartScreenLoadingPreview() {
    WatchMyTankTheme {
        // Simulate loading state - Need to hoist state or pass params directly
        // For simplicity, we'll just show the structure
        Scaffold(
            topBar = { TopAppBar(title = { Text("Temperature Chart") }) }
        ) {
            Column(Modifier.padding(it)) {
                ParameterChartTypeSelectorTabs(ParameterType.values().toList(), ParameterType.TEMPERATURE, {})
                Divider()
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Chart Screen - Empty")
@Composable
private fun ParameterChartScreenEmptyPreview() {
    WatchMyTankTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("pH Chart") }) }
        ) {
            Column(Modifier.padding(it)) {
                ParameterChartTypeSelectorTabs(ParameterType.values().toList(), ParameterType.PH, {})
                Divider()
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    InfoStateView(message = "No data available to display chart for pH.", icon = Icons.Default.Info)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Chart Screen - Data")
@Composable
private fun ParameterChartScreenDataPreview() {
    val sampleEntries = listOf(entryOf(0, 10), entryOf(1, 12), entryOf(2, 8), entryOf(3, 15))
    val modelProducer = ChartEntryModelProducer(sampleEntries)
    WatchMyTankTheme {
        Scaffold(
             topBar = { TopAppBar(title = { Text("Nitrate Chart")}) }
        ) {
            Column(Modifier.padding(it)) {
                ParameterChartTypeSelectorTabs(ParameterType.values().toList(), ParameterType.NITRATE, {}) 
                Divider()
                Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Chart(
                         chart = lineChart(),
                         chartModelProducer = modelProducer,
                         startAxis = rememberStartAxis(title = "ppm"),
                         bottomAxis = rememberBottomAxis(title = "Time")
                    )
                }
            }
        }
    }
}

// TODO: Add Composable Previews for ParameterChartScreen 