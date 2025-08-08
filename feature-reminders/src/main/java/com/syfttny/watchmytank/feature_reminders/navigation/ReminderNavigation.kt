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
const val REMINDER_ID_ARG = "reminderId" 



const val REMINDER_EDIT_ROUTE_TEMPLATE = "$REMINDER_EDIT_ROUTE/{$REMINDER_ID_ARG}" 
const val REMINDER_EDIT_ROUTE_BASE = REMINDER_EDIT_ROUTE 


fun createReminderEditRoute(reminderId: Long?): String {
    return if (reminderId != null && reminderId > 0) {
        "$REMINDER_EDIT_ROUTE/$reminderId" 
    } else {
        "$REMINDER_EDIT_ROUTE/-1" 
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
                    
                    val route = createReminderEditRoute(reminderId)
                    navController.navigate(route)
                }
                
                
            )
        }

        composable(
            route = REMINDER_EDIT_ROUTE_TEMPLATE, 
            arguments = listOf(
                navArgument(REMINDER_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = -1L 
                    
                }
            )
        ) { backStackEntry ->
            
            val reminderId = backStackEntry.arguments?.getLong(REMINDER_ID_ARG) ?: -1L 

            
            ReminderEditScreen(
                reminderId = reminderId,
                onNavigateBack = { navController.popBackStack() },
                
            )
        }
    }
} 