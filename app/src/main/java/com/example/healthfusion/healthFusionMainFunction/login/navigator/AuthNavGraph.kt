package com.example.healthfusion.healthFusionMainFunction.login.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.healthfusion.healthFusionMainFunction.login.ui.LoginScreen
import com.example.healthfusion.healthFusionMainFunction.login.ui.LoginViewModel
import com.example.healthfusion.healthFusionMainFunction.login.ui.SignUpScreen

@Composable
fun AuthNavGraph(navController: NavHostController, loginViewModel: LoginViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, viewModel = loginViewModel)
        }
        composable("signup") {
            SignUpScreen(navController, viewModel = loginViewModel)
        }
    }
}