package com.example.expencetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expencetracker.domain.model.CategoryStat
import com.example.expencetracker.domain.model.BudgetStatus
import com.example.expencetracker.domain.model.Transaction
import com.example.expencetracker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for the Dashboard screen.
 * Manages the state and logic for displaying the dashboard UI.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getBudgetsByMonthYearUseCase: GetBudgetsByMonthYearUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _selectedMonth = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    private val _selectedYear = MutableStateFlow(LocalDate.now().year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    init {
        loadDashboardData()
    }

    /**
     * Load dashboard data for the selected month and year.
     */
    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val month = _selectedMonth.value
                val year = _selectedYear.value

                // Load transactions
                getAllTransactionsUseCase().collect { transactions ->
                    val recentTransactions = transactions
                        .filter { it.dateTime > 0 }
                        .sortedByDescending { it.dateTime }
                        .take(5)

                    _uiState.value = _uiState.value.copy(
                        recentTransactions = recentTransactions
                    )
                }

                // Load monthly summary
                val (incomeFlow, expenseFlow) = getMonthlySummaryUseCase(month, year)
                incomeFlow.collect { income ->
                    _uiState.value = _uiState.value.copy(
                        totalIncome = income
                    )
                }
                expenseFlow.collect { expense ->
                    _uiState.value = _uiState.value.copy(
                        totalExpense = expense
                    )
                }

                // Load categories
                getAllCategoriesUseCase().collect { categories ->
                    val categoryBreakdown = categories.map { category ->
                        CategoryStat(
                            category = category,
                            amount = 0.0,
                            percentage = 0f
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        categoryBreakdown = categoryBreakdown
                    )
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    /**
     * Handle events on the dashboard.
     */
    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.SelectMonth -> {
                _selectedMonth.value = event.month
                loadDashboardData()
            }
            is DashboardEvent.SelectYear -> {
                _selectedYear.value = event.year
                loadDashboardData()
            }
            DashboardEvent.Refresh -> {
                loadDashboardData()
            }
        }
    }
}

/**
 * UI State for the Dashboard.
 */
data class DashboardUiState(
    val isLoading: Boolean = false,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val categoryBreakdown: List<CategoryStat> = emptyList(),
    val budgetStatus: List<BudgetStatus> = emptyList(),
    val errorMessage: String? = null
)

/**
 * Events for the Dashboard.
 */
sealed class DashboardEvent {
    data class SelectMonth(val month: Int) : DashboardEvent()
    data class SelectYear(val year: Int) : DashboardEvent()
    data object Refresh : DashboardEvent()
}

