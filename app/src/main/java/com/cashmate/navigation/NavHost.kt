package com.cashmate.navigation

import Home
import Logs
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = CahsMateScreen.Home.name,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(CahsMateScreen.Stats.name) {
//            ProfileScreen()
        }
        composable(CahsMateScreen.Home.name) {
            Home()

        }
        composable(CahsMateScreen.Logs.name) {
            Logs()
        }
    }

}

