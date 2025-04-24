package com.syfttny.watchmytank.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.syfttny.watchmytank.feature_reminders.navigation.REMINDER_GRAPH_ROUTE
import com.syfttny.watchmytank.feature_reminders.navigation.addReminderGraph

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: String = REMINDER_GRAPH_ROUTE // Start with the reminder feature for now
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Add the reminder feature's navigation graph
        addReminderGraph(
            navController = navController
            // Pass modifier if needed, e.g., modifier = Modifier.padding(innerPadding)
        )

        // TODO: Add other feature graphs here later (e.g., addParameterGraph)
        // composable("some_other_feature_route") { ... }
        // navigation(route = "parameter_graph", startDestination = "parameter_list") { ... }
    }
} 