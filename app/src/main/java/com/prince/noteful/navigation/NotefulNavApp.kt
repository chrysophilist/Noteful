package com.prince.noteful.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prince.noteful.ui.screens.ActiveNoteScreen
import com.prince.noteful.ui.screens.HomeScreen
import com.prince.noteful.ui.viewModels.NotefulViewModel
import com.prince.noteful.ui.viewModels.PrefViewModel

@Composable
fun NotefulNavApp(
    viewModel: NotefulViewModel = hiltViewModel(),
    prefViewModel: PrefViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    var scaffoldState by remember { mutableStateOf(AppScaffoldState()) }

    Scaffold(
        modifier = scaffoldState.modifier,
        topBar = { scaffoldState.topBar?.invoke() },
        bottomBar = { scaffoldState.bottomBar?.invoke() },
        floatingActionButton = { scaffoldState.floatingActionButton?.invoke() }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = HomeScreenRoute
        ){
            composable<HomeScreenRoute>{
                HomeScreen(
                    setScaffoldState = { scaffoldState = it },
                    innerPadding = innerPadding,
                    viewModel,
                    onCardClick = {
                        navController.navigate(ActiveNoteRoute)
                    },
                    prefViewModel
                )
            }

            composable<ActiveNoteRoute>{
                ActiveNoteScreen(
                    setScaffoldState = { scaffoldState = it },
                    innerPadding = innerPadding,
                    viewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

data class AppScaffoldState(
    val modifier: Modifier = Modifier,
    val topBar: (@Composable ()-> Unit) ?= null,
    val bottomBar: (@Composable () -> Unit) ?= null,
    val floatingActionButton: (@Composable () -> Unit) ?= null,
)