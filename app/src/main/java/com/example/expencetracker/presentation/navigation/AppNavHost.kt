package com.example.expencetracker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expencetracker.presentation.screens.DashboardScreen
import com.example.expencetracker.presentation.screens.TransactionsScreen
import com.example.expencetracker.presentation.screens.AddEditTransactionScreen
import com.example.expencetracker.presentation.screens.ReportsScreen
import com.example.expencetracker.presentation.screens.BudgetsScreen
import com.example.expencetracker.presentation.screens.SettingsScreen

/**
 * Main navigation setup for the application.
 * Contains navigation bar with 5 main screens and handles navigation logic.
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            AppNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController)
            }
            composable(Screen.Transactions.route) {
                TransactionsScreen(navController)
            }
            composable(Screen.AddEdit(null).route) {
                AddEditTransactionScreen(navController, transactionId = null)
            }
            composable("add_edit/{transactionId}") { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId")?.toLongOrNull()
                AddEditTransactionScreen(navController, transactionId = transactionId)
            }
            composable(Screen.Reports.route) {
                ReportsScreen(navController)
            }
            composable(Screen.Budgets.route) {
                BudgetsScreen(navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController)
            }
        }
    }
}

/**
 * Bottom navigation bar for the application.
 */
@Composable
private fun AppNavigationBar(navController: NavHostController) {
    val navigationItems = listOf(
        NavigationItem(
            route = Screen.Dashboard.route,
            icon = Icons.Default.Home,
            label = "Dashboard"
        ),
        NavigationItem(
            route = Screen.Transactions.route,
            icon = Icons.Default.Receipt,
            label = "Transactions"
        ),
        NavigationItem(
            route = Screen.Reports.route,
            icon = Icons.Default.BarChart,
            label = "Reports"
        ),
        NavigationItem(
            route = Screen.Budgets.route,
            icon = Icons.Default.TrendingUp,
            label = "Budgets"
        ),
        NavigationItem(
            route = Screen.Settings.route,
            icon = Icons.Default.Settings,
            label = "Settings"
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * Data class for navigation items.
 */
data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)




