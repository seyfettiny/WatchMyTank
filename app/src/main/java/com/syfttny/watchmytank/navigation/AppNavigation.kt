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

const val DASHBOARD_ROUTE = "dashboard"

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: String = DASHBOARD_ROUTE
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = DASHBOARD_ROUTE) {
            DashboardScreen(
                onNavigateToReminders = { navController.navigate(REMINDER_GRAPH_ROUTE) },
                onNavigateToParameters = { tankId ->
                    navController.navigate(PARAMETER_GRAPH_ROUTE + "/$tankId")
                }
            )
        }

        addReminderGraph(
            navController = navController
        )

        addParameterGraph(
            navController = navController
        )

        addReminderGraph(
            navController = navController,
        )
        addParameterGraph(
            navController = navController,
        )
    }
} 