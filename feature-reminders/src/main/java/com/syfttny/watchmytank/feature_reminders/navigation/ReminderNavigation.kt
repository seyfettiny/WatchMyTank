package com.syfttny.watchmytank.feature_reminders.navigation

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.syfttny.watchmytank.feature_reminders.ui.list.ReminderListScreen
import com.syfttny.watchmytank.feature_reminders.ui.edit.ReminderEditScreen

const val REMINDER_GRAPH_ROUTE = "reminder_graph"
const val REMINDERS_LIST_ROUTE = "reminders_list"
const val REMINDER_EDIT_ROUTE = "reminder_edit"
const val REMINDER_ID_ARG = "reminderId" // Argument name

// Optional argument: Use query parameter style ?arg={arg} for optional args
// Or provide a default value in the route path itself for required args with defaults
const val REMINDER_EDIT_ROUTE_TEMPLATE = "$REMINDER_EDIT_ROUTE/{$REMINDER_ID_ARG}" // Path template
const val REMINDER_EDIT_ROUTE_BASE = REMINDER_EDIT_ROUTE // Base route for "add"

// Helper function to create the navigation route string
fun createReminderEditRoute(reminderId: Long?): String {
    return if (reminderId != null && reminderId > 0) {
        "$REMINDER_EDIT_ROUTE/$reminderId" // Route for editing an existing reminder
    } else {
        "$REMINDER_EDIT_ROUTE/-1" // Route for adding a new reminder (using -1 as default)
    }
}

fun NavGraphBuilder.addReminderGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation(
        route = REMINDER_GRAPH_ROUTE,
        startDestination = REMINDERS_LIST_ROUTE
    ) {
        composable(route = REMINDERS_LIST_ROUTE) {
            ReminderListScreen(
                onNavigateToEdit = { reminderId ->
                    // Use the helper function to create the route
                    val route = createReminderEditRoute(reminderId)
                    navController.navigate(route)
                }
                // Add other necessary parameters for ReminderListScreen if any
                // e.g., viewModel = hiltViewModel() // Assuming Hilt is set up
            )
        }

        composable(
            route = REMINDER_EDIT_ROUTE_TEMPLATE, // Use the template with the mandatory argument
            arguments = listOf(
                navArgument(REMINDER_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = -1L // Provide a default value for "add" case
                    // nullable = true // Alternatively, make it nullable if -1L is not desired
                }
            )
        ) { backStackEntry ->
            // Extract the reminderId argument (will be -1L if adding new)
            val reminderId = backStackEntry.arguments?.getLong(REMINDER_ID_ARG) ?: -1L // Use elvis for safety

            // Use the actual ReminderEditScreen
            ReminderEditScreen(
                reminderId = reminderId,
                onNavigateBack = { navController.popBackStack() },
                // Pass ViewModel if needed, usually handled by hiltViewModel()
            )
        }
    }
} 