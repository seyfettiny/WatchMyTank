package com.syfttny.watchmytank.feature_parameters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.syfttny.watchmytank.feature_parameters.ui.chart.ParameterChartScreen
import com.syfttny.watchmytank.feature_parameters.ui.history.ParameterHistoryScreen
import com.syfttny.watchmytank.feature_parameters.ui.log.ParameterLogScreen

/**
 * Adds the parameter feature's navigation graph to the NavGraphBuilder.
 */
fun NavGraphBuilder.addParameterGraph(navController: NavController) {
    navigation(
        route = ParameterDestinations.PARAMETER_GRAPH_ROUTE,
        startDestination = ParameterDestinations.PARAMETER_HISTORY_ROUTE // Start at the history screen
    ) {
        // History Screen (List + Filter)
        composable(route = ParameterDestinations.PARAMETER_HISTORY_ROUTE) {
            ParameterHistoryScreen(
                onNavigateBack = { navController.popBackStack() }, // Or navigate up to a main dashboard
                onNavigateToLogParameter = {
                    navController.navigate(ParameterDestinations.PARAMETER_LOG_ROUTE)
                },
                // TODO: Add navigation to chart screen from here (e.g., button in TopAppBar)
                onNavigateToChart = { navController.navigate(ParameterDestinations.PARAMETER_CHART_ROUTE) }
            )
        }

        // Log Parameter Screen (Form)
        composable(route = ParameterDestinations.PARAMETER_LOG_ROUTE) {
            ParameterLogScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Chart Screen
        composable(route = ParameterDestinations.PARAMETER_CHART_ROUTE) {
            ParameterChartScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Add other parameter-related destinations here (e.g., detail screen)
    }
} 