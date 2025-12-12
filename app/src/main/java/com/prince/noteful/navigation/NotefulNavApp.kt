package com.prince.noteful.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prince.noteful.ui.screens.ActiveNoteScreen
import com.prince.noteful.ui.screens.HomeScreen
import com.prince.noteful.ui.viewModels.NotefulViewModel

@Composable
fun NotefulNavApp(
    viewModel: NotefulViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute
    ){
        composable<HomeScreenRoute>{
            HomeScreen(
                viewModel,
                onCardClick = {
                    navController.navigate(ActiveNoteRoute)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable<ActiveNoteRoute>{
            ActiveNoteScreen(
                viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}