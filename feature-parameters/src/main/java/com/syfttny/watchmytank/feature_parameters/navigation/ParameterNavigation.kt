package com.syfttny.watchmytank.feature_parameters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.syfttny.watchmytank.feature_parameters.ui.history.ParameterHistoryScreen
import com.syfttny.watchmytank.feature_parameters.ui.log.LogParametersScreen

/**
 * Adds the parameter feature's navigation graph to the NavGraphBuilder.
 */
fun NavGraphBuilder.addParameterGraph(navController: NavController) {
    navigation(
        route = ParameterDestinations.PARAMETER_GRAPH_ROUTE + "/{${ParameterDestinations.TANK_ID_ARG}}",
        startDestination = ParameterDestinations.PARAMETER_CHARTS_ROUTE + "/{${ParameterDestinations.TANK_ID_ARG}}"
    ) {
        // Parameter Charts Screen (Main entry for the feature)
        composable(
            route = ParameterDestinations.PARAMETER_CHARTS_ROUTE + "/{${ParameterDestinations.TANK_ID_ARG}}",
            arguments = listOf(navArgument(ParameterDestinations.TANK_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry -> // Access backStackEntry to get arguments
            val tankId = backStackEntry.arguments?.getString(ParameterDestinations.TANK_ID_ARG) ?: ""
            ParameterHistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogParameter = {
                    // Construct the route correctly by replacing the placeholder
                    val route = ParameterDestinations.LOG_PARAMETERS_ROUTE + "/{${ParameterDestinations.TANK_ID_ARG}}"
                    val routeWithArgs = route.replace("{${ParameterDestinations.TANK_ID_ARG}}", tankId)
                    navController.navigate(routeWithArgs)
                }
            )
        }

        // Log Parameters Screen (New screen)
        composable(
             route = ParameterDestinations.LOG_PARAMETERS_ROUTE + "/{${ParameterDestinations.TANK_ID_ARG}}",
             arguments = listOf(navArgument(ParameterDestinations.TANK_ID_ARG) { type = NavType.StringType })
        ) {
            LogParametersScreen(
                //onNavigateBack = { navController.popBackStack() } // Add navigation back
            )
        }

        // Remove old ParameterChartScreen composable
        // composable(route = ParameterDestinations.PARAMETER_CHART_ROUTE) { ... }
    }
} 