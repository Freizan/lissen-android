package org.grakovne.lissen.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.grakovne.lissen.persistence.preferences.LissenSharedPreferences
import org.grakovne.lissen.ui.screens.library.LibraryScreen
import org.grakovne.lissen.ui.screens.login.LoginScreen
import org.grakovne.lissen.ui.screens.player.PlayerScreen
import org.grakovne.lissen.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    val hasCredentials by remember {
        mutableStateOf(
            LissenSharedPreferences.getInstance().hasCredentials()
        )
    }
    val startDestination = when {
        hasCredentials -> "library_screen"
        else -> "login_screen"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("library_screen") {
            LibraryScreen(navController)
        }

        composable(
            "player_screen/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { navigationStack ->
            val bookId = navigationStack.arguments?.getString("bookId")

            PlayerScreen(
                navController = navController,
                onBack = {
                    navController.popBackStack()
                },
                bookId = bookId
            )
        }

        composable("login_screen") {
            LoginScreen(navController)
        }

        composable("settings_screen") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
    }
}
