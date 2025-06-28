package com.example.kushagraDemo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.kushagraDemo.SampleAppState
import com.example.kushagraDemo.holdings.holdingsScreen

@Composable
fun SampleAppNavHost(
    appState: SampleAppState,
    startDestination: Any,
    modifier: Modifier,
    isOffline: Boolean,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        holdingsScreen(isOffline)
    }
}