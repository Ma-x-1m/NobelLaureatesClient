package com.example.nobellaureatesclient.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nobellaureatesclient.presentation.details.PrizeDetailsScreen
import com.example.nobellaureatesclient.presentation.list.PrizesListScreen

@Composable
fun NobelNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NobelDestinations.PRIZES_LIST
    ) {
        composable(NobelDestinations.PRIZES_LIST) {
            PrizesListScreen(
                onPrizeClick = { prize ->
                    navController.navigate(
                        NobelDestinations.prizeDetails(prize.awardYear, prize.category.apiCode)
                    )
                }
            )
        }

        composable(
            route = NobelDestinations.PRIZE_DETAILS_PATTERN,
            arguments = listOf(
                navArgument(NobelDestinations.ARG_YEAR) { type = NavType.StringType },
                navArgument(NobelDestinations.ARG_CATEGORY) { type = NavType.StringType }
            )
        ) {
            PrizeDetailsScreen(onBack = { navController.popBackStack() })
        }
    }
}
