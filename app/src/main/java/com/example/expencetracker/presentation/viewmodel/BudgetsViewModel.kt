package com.example.expencetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expencetracker.domain.model.BudgetStatus
import com.example.expencetracker.domain.model.Budget
import com.example.expencetracker.domain.model.Category
import com.example.expencetracker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for the Budgets screen.
 * Manages the state and logic for displaying and managing budgets.
 */
@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val getBudgetsByMonthYearUseCase: GetBudgetsByMonthYearUseCase,
    private val setBudgetUseCase: SetBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BudgetsUiState>(BudgetsUiState())
    val uiState: StateFlow<BudgetsUiState> = _uiState.asStateFlow()

    private val _selectedMonth = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    private val _selectedYear = MutableStateFlow(LocalDate.now().year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    init {
        loadBudgets()
        loadCategories()
    }

    /**
     * Load budgets for the selected month and year.
     */
    private fun loadBudgets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val month = _selectedMonth.value
                val year = _selectedYear.value

                getBudgetsByMonthYearUseCase(month, year).collect { budgets ->
                    // TODO: Calculate spent amounts for each budget
                    val budgetStatuses = budgets.map { budget ->
                        BudgetStatus(
                            category = _uiState.value.categories.find { it.id == budget.categoryId }
                                ?: return@map null,
                            spent = 0.0,
                            limit = budget.limitAmount
                        )
                    }.filterNotNull()

                    _uiState.value = _uiState.value.copy(
                        budgetStatuses = budgetStatuses,
                        isLoading = false
                    )
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
     * Load all categories.
     */
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                getAllCategoriesUseCase().collect { categories ->
                    _uiState.value = _uiState.value.copy(
                        categories = categories
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    /**
     * Handle events on the budgets screen.
     */
    fun onEvent(event: BudgetsEvent) {
        when (event) {
            is BudgetsEvent.SelectMonth -> {
                _selectedMonth.value = event.month
                loadBudgets()
            }
            is BudgetsEvent.SelectYear -> {
                _selectedYear.value = event.year
                loadBudgets()
            }
            is BudgetsEvent.SetBudget -> {
                setBudget(event.categoryId, event.limit)
            }
            is BudgetsEvent.DeleteBudget -> {
                deleteBudget(event.budgetId)
            }
        }
    }

    /**
     * Set a budget for a category.
     */
    private fun setBudget(categoryId: Long, limit: Double) {
        viewModelScope.launch {
            try {
                val month = _selectedMonth.value
                val year = _selectedYear.value
                
                val budget = Budget(
                    id = 0,
                    categoryId = categoryId,
                    limitAmount = limit,
                    month = month,
                    year = year
                )
                
                setBudgetUseCase(budget)
                loadBudgets()
                _uiState.value = _uiState.value.copy(
                    successMessage = "Budget set successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    /**
     * Delete a budget.
     */
    private fun deleteBudget(budgetId: Long) {
        viewModelScope.launch {
            try {
                deleteBudgetUseCase(budgetId)
                loadBudgets()
                _uiState.value = _uiState.value.copy(
                    successMessage = "Budget deleted"
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
 * UI State for the Budgets screen.
 */
data class BudgetsUiState(
    val budgetStatuses: List<BudgetStatus> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

/**
 * Events for the Budgets screen.
 */
sealed class BudgetsEvent {
    data class SelectMonth(val month: Int) : BudgetsEvent()
    data class SelectYear(val year: Int) : BudgetsEvent()
    data class SetBudget(val categoryId: Long, val limit: Double) : BudgetsEvent()
    data class DeleteBudget(val budgetId: Long) : BudgetsEvent()
}


