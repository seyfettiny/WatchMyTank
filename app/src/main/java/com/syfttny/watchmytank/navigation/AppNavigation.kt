package com.syfttny.watchmytank.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.syfttny.watchmytank.feature_parameters.navigation.ParameterDestinations
import com.syfttny.watchmytank.feature_parameters.navigation.ParameterDestinations.PARAMETER_GRAPH_ROUTE
import com.syfttny.watchmytank.feature_parameters.navigation.addParameterGraph
import com.syfttny.watchmytank.feature_reminders.navigation.REMINDER_GRAPH_ROUTE
import com.syfttny.watchmytank.feature_reminders.navigation.addReminderGraph
import com.syfttny.watchmytank.ui.dashboard.DashboardScreen

// Define top-level routes
const val DASHBOARD_ROUTE = "dashboard"

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: String = DASHBOARD_ROUTE // Start at the dashboard
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Dashboard Screen (Top Level)
        composable(route = DASHBOARD_ROUTE) {
            DashboardScreen(
                onNavigateToReminders = { navController.navigate(REMINDER_GRAPH_ROUTE) },
                onNavigateToParameters = { tankId ->
                    navController.navigate(PARAMETER_GRAPH_ROUTE + "/$tankId")
                }
            )
        }

        // Reminder Feature Graph (Nested)
        addReminderGraph(
            navController = navController
        )

        // Parameter Feature Graph (Nested)
        addParameterGraph(
            navController = navController
        )

        // Feature Graphs
        addReminderGraph(
            navController = navController,
            // Add necessary actions if reminders graph needs to navigate elsewhere
        )
        addParameterGraph(
            navController = navController,
            // Add necessary actions if parameters graph needs to navigate elsewhere
        )

        // TODO: Add other top-level destinations or graphs (e.g., Settings)
    }
} 