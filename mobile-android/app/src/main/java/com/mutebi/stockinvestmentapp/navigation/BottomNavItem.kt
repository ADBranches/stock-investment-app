package com.mutebi.stockinvestmentapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Dashboard : BottomNavItem(
        route = Routes.Dashboard.route,
        label = "Dashboard",
        icon = Icons.Filled.Home
    )

    data object Market : BottomNavItem(
        route = Routes.Market.route,
        label = "Market",
        icon = Icons.Filled.Star
    )

    data object Watchlist : BottomNavItem(
        route = Routes.Watchlist.route,
        label = "Watchlist",
        icon = Icons.Filled.List
    )

    data object Portfolio : BottomNavItem(
        route = Routes.Portfolio.route,
        label = "Portfolio",
        icon = Icons.Filled.List
    )

    data object Settings : BottomNavItem(
        route = Routes.Settings.route,
        label = "Settings",
        icon = Icons.Filled.Settings
    )

    companion object {
        val items = listOf(Dashboard, Market, Watchlist, Portfolio, Settings)
    }
}