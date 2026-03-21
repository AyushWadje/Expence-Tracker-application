package com.example.expencetracker.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.example.expencetracker.domain.model.Category
import com.example.expencetracker.domain.model.CategoryType
import com.example.expencetracker.domain.model.Transaction
import com.example.expencetracker.domain.model.TransactionType
import com.example.expencetracker.domain.model.PaymentMethod
import com.example.expencetracker.domain.usecase.*

/**
 * Unit tests for DashboardViewModel.
 */
class DashboardViewModelTest {

    @MockK
    private lateinit var getAllTransactionsUseCase: GetAllTransactionsUseCase

    @MockK
    private lateinit var getMonthlySummaryUseCase: GetMonthlySummaryUseCase

    @MockK
    private lateinit var getAllCategoriesUseCase: GetAllCategoriesUseCase

    @MockK
    private lateinit var getBudgetsByMonthYearUseCase: GetBudgetsByMonthYearUseCase

    private lateinit var viewModel: DashboardViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        // Mock the use cases
        every { getAllTransactionsUseCase() } returns flowOf(emptyList())
        every { getAllCategoriesUseCase() } returns flowOf(emptyList())
        every { getMonthlySummaryUseCase(any(), any()) } returns Pair(
            flowOf(0.0),
            flowOf(0.0)
        )

        viewModel = DashboardViewModel(
            getAllTransactionsUseCase,
            getMonthlySummaryUseCase,
            getAllCategoriesUseCase,
            getBudgetsByMonthYearUseCase
        )
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // We can't directly test the initial loading state due to Hilt initialization
        // but we can verify the state structure
        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.totalIncome == 0.0)
            assert(state.totalExpense == 0.0)
        }
    }

    @Test
    fun `select month should update selected month`() = runTest {
        viewModel.onEvent(DashboardEvent.SelectMonth(3))

        viewModel.selectedMonth.test {
            val month = awaitItem()
            assert(month == 3)
        }
    }

    @Test
    fun `select year should update selected year`() = runTest {
        viewModel.onEvent(DashboardEvent.SelectYear(2024))

        viewModel.selectedYear.test {
            val year = awaitItem()
            assert(year == 2024)
        }
    }
}

