package com.gunlukmarsrut.courier.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gunlukmarsrut.courier.data.SampleData
import com.gunlukmarsrut.courier.ui.screens.ComponentsScreen
import com.gunlukmarsrut.courier.ui.screens.DayScreen
import com.gunlukmarsrut.courier.ui.screens.DaysScreen
import com.gunlukmarsrut.courier.ui.screens.LoginScreen
import com.gunlukmarsrut.courier.ui.screens.XListScreen

private object Routes {
    const val LOGIN = "login"
    const val DAYS = "days"
    const val DAY = "day/{dayId}"
    const val X_LIST = "xlist/{dayId}"
    const val COMPONENTS = "components"
    fun day(id: String) = "day/$id"
    fun xList(id: String) = "xlist/$id"
}

/**
 * The app's whole navigation surface: Login → Days → Day → X list.
 * No tab bar — a single back stack, exactly as the spec calls for.
 */
@Composable
fun GunlukMarsrutNavGraph() {
    val navController = rememberNavController()
    val days = remember { SampleData.allDays }

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DAYS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.DAYS) {
            DaysScreen(
                onOpenDay = { day -> navController.navigate(Routes.day(day.id)) },
                onOpenXList = { day -> navController.navigate(Routes.xList(day.id)) },
                initialDays = days,
                onOpenComponents = { navController.navigate(Routes.COMPONENTS) },
            )
        }

        composable(Routes.COMPONENTS) {
            ComponentsScreen()
        }

        composable(
            route = Routes.DAY,
            arguments = listOf(navArgument("dayId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val dayId = backStackEntry.arguments?.getString("dayId")
            val day = days.find { it.id == dayId } ?: days.first()
            DayScreen(
                initialDay = day,
                onBack = { navController.popBackStack() },
                onOpenXList = { navController.navigate(Routes.xList(it.id)) },
            )
        }

        composable(
            route = Routes.X_LIST,
            arguments = listOf(navArgument("dayId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val dayId = backStackEntry.arguments?.getString("dayId")
            val day = days.find { it.id == dayId } ?: days.first()
            XListScreen(
                initialDay = day,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
