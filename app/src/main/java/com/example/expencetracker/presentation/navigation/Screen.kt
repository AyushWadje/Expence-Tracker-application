package com.example.expencetracker.presentation.navigation

/**
 * Sealed class for type-safe navigation routes.
 * Each screen in the app has its corresponding Route.
 */
sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Transactions : Screen("transactions")
    data class AddEdit(val transactionId: Long? = null) : Screen(
        if (transactionId != null) "add_edit/$transactionId" else "add_edit"
    )
    data object Reports : Screen("reports")
    data object Budgets : Screen("budgets")
    data object Settings : Screen("settings")

    companion object {
        fun getAddEditRoute(transactionId: Long? = null): String {
            return if (transactionId != null) "add_edit/$transactionId" else "add_edit"
        }
    }
}

