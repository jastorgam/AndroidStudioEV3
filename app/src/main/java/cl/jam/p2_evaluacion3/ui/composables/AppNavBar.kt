package cl.jam.p2_evaluacion3.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNavBar(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "request"
    ) {
//        composable("login") {
//            LoginAccountUI(
//                navToRequest = { navController.navigate("request") }
//            )
//
//        }
        composable("request") {
            RequestAccountUI(
                navToLogin = { navController.navigate("login") }
            )
        }
    }
}