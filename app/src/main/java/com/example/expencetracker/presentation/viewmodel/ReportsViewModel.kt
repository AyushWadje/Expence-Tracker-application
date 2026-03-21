package com.example.expencetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expencetracker.domain.model.CategoryStat
import com.example.expencetracker.domain.model.DailyStat
import com.example.expencetracker.domain.usecase.GetAllTransactionsUseCase
import com.example.expencetracker.domain.usecase.GetMonthlySummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

/**
 * ViewModel for the Reports screen.
 * Manages the state and logic for displaying various expense reports and statistics.
 */
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReportsUiState>(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    private val _selectedMonth = MutableStateFlow(YearMonth.now())
    val selectedMonth: StateFlow<YearMonth> = _selectedMonth.asStateFlow()

    init {
        loadReports()
    }

    /**
     * Load reports data for the selected month.
     */
    private fun loadReports() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val month = _selectedMonth.value.monthValue
                val year = _selectedMonth.value.year

                // Load all transactions to calculate statistics
                getAllTransactionsUseCase().collect { transactions ->
                    // Filter transactions for the selected month
                    val monthTransactions = transactions.filter { transaction ->
                        val transactionDate = LocalDate.ofEpochDay(transaction.dateTime / (24 * 60 * 60 * 1000))
                        transactionDate.monthValue == month && transactionDate.year == year
                    }

                    // Calculate category breakdown
                    val categoryBreakdown = monthTransactions
                        .filter { it.type.name == "EXPENSE" }
                        .groupBy { it.category }
                        .map { (category, categoryTransactions) ->
                            val amount = categoryTransactions.sumOf { it.amount }
                            CategoryStat(
                                category = category,
                                amount = amount,
                                percentage = 0f
                            )
                        }
                        .sortedByDescending { it.amount }

                    // Calculate percentages
                    val totalExpense = categoryBreakdown.sumOf { it.amount }
                    val categoryBreakdownWithPercentages = categoryBreakdown.map { stat ->
                        stat.copy(
                            percentage = if (totalExpense > 0) (stat.amount / totalExpense * 100).toFloat() else 0f
                        )
                    }

                    // Calculate daily spending
                    val dailyStats = monthTransactions
                        .filter { it.type.name == "EXPENSE" }
                        .groupBy { transaction ->
                            LocalDate.ofEpochDay(transaction.dateTime / (24 * 60 * 60 * 1000)).dayOfMonth
                        }
                        .map { (day, dayTransactions) ->
                            DailyStat(
                                day = day,
                                total = dayTransactions.sumOf { it.amount }
                            )
                        }
                        .sortedBy { it.day }

                    // Load monthly summary
                    val (incomeFlow, expenseFlow) = getMonthlySummaryUseCase(month, year)
                    incomeFlow.collect { income ->
                        expenseFlow.collect { expense ->
                            _uiState.value = _uiState.value.copy(
                                categoryBreakdown = categoryBreakdownWithPercentages,
                                dailyStats = dailyStats,
                                totalIncome = income,
                                totalExpense = expense,
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    /**
     * Handle events on the reports screen.
     */
    fun onEvent(event: ReportsEvent) {
        when (event) {
            is ReportsEvent.SelectMonth -> {
                _selectedMonth.value = event.yearMonth
                loadReports()
            }
            ReportsEvent.ExportReport -> {
                exportReport()
            }
        }
    }

    /**
     * Export report as CSV.
     */
    private fun exportReport() {
        viewModelScope.launch {
            try {
                // TODO: Implement CSV export functionality
                _uiState.value = _uiState.value.copy(
                    exportSuccessful = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }
}

/**
 * UI State for the Reports screen.
 */
data class ReportsUiState(
    val categoryBreakdown: List<CategoryStat> = emptyList(),
    val dailyStats: List<DailyStat> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val exportSuccessful: Boolean = false
)

/**
 * Events for the Reports screen.
 */
sealed class ReportsEvent {
    data class SelectMonth(val yearMonth: YearMonth) : ReportsEvent()
    data object ExportReport : ReportsEvent()
}

