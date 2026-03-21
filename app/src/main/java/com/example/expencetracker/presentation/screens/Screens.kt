package com.example.expencetracker.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

/**
 * Dashboard screen composable.
 */
@Composable
fun DashboardScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Dashboard Screen")
    }
}

/**
 * Transactions screen composable.
 */
@Composable
fun TransactionsScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Transactions Screen")
    }
}

/**
 * Add/Edit transaction screen composable.
 */
@Composable
fun AddEditTransactionScreen(navController: NavHostController, transactionId: Long?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Add/Edit Transaction Screen")
    }
}

/**
 * Reports screen composable.
 */
@Composable
fun ReportsScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Reports Screen")
    }
}

/**
 * Budgets screen composable.
 */
@Composable
fun BudgetsScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Budgets Screen")
    }
}

/**
 * Settings screen composable.
 */
@Composable
fun SettingsScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Settings Screen")
    }
}

