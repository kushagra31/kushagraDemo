package com.example.kushagraDemo.navigation

import com.example.kushagraDemo.holdings.HoldingsBaseRoute
import com.example.kushagraDemo.holdings.HoldingsRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val titleText: String,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    HOLDINGS(
        titleText = "Portfolio",
        route = HoldingsRoute::class,
        baseRoute = HoldingsBaseRoute::class,
    ),
}