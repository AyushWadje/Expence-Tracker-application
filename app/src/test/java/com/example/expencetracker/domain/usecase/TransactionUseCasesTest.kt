package com.example.expencetracker.domain.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
import com.example.expencetracker.domain.repository.TransactionRepository

/**
 * Unit tests for transaction use cases.
 */
class TransactionUseCasesTest {

    @MockK
    private lateinit var mockRepository: TransactionRepository

    private lateinit var addTransactionUseCase: AddTransactionUseCase
    private lateinit var updateTransactionUseCase: UpdateTransactionUseCase
    private lateinit var deleteTransactionUseCase: DeleteTransactionUseCase
    private lateinit var getTransactionByIdUseCase: GetTransactionByIdUseCase
    private lateinit var getAllTransactionsUseCase: GetAllTransactionsUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        addTransactionUseCase = AddTransactionUseCase(mockRepository)
        updateTransactionUseCase = UpdateTransactionUseCase(mockRepository)
        deleteTransactionUseCase = DeleteTransactionUseCase(mockRepository)
        getTransactionByIdUseCase = GetTransactionByIdUseCase(mockRepository)
        getAllTransactionsUseCase = GetAllTransactionsUseCase(mockRepository)
    }

    @Test
    fun `add transaction should call repository insert`() = runTest {
        // Arrange
        val category = Category(1, "Food", "restaurant", "#FF9C3E", CategoryType.EXPENSE)
        val transaction = Transaction(
            id = 0,
            amount = 100.0,
            type = TransactionType.EXPENSE,
            category = category,
            note = "Lunch",
            dateTime = System.currentTimeMillis(),
            paymentMethod = PaymentMethod.CASH
        )
        coEvery { mockRepository.addTransaction(transaction) } returns 1L

        // Act
        val result = addTransactionUseCase(transaction)

        // Assert
        assert(result == 1L)
        coVerify { mockRepository.addTransaction(transaction) }
    }

    @Test
    fun `delete transaction should call repository delete`() = runTest {
        // Arrange
        coEvery { mockRepository.deleteTransaction(1L) } returns Unit

        // Act
        deleteTransactionUseCase(1L)

        // Assert
        coVerify { mockRepository.deleteTransaction(1L) }
    }

    @Test
    fun `get transaction by id should return transaction from repository`() = runTest {
        // Arrange
        val category = Category(1, "Food", "restaurant", "#FF9C3E", CategoryType.EXPENSE)
        val transaction = Transaction(
            id = 1,
            amount = 100.0,
            type = TransactionType.EXPENSE,
            category = category,
            note = "Lunch",
            dateTime = System.currentTimeMillis(),
            paymentMethod = PaymentMethod.CASH
        )
        coEvery { mockRepository.getTransactionById(1L) } returns flowOf(transaction)

        // Act
        val result = getTransactionByIdUseCase(1L)

        // Assert
        result.collect { retrieved ->
            assert(retrieved?.id == 1L)
            assert(retrieved?.amount == 100.0)
        }
    }

    @Test
    fun `get all transactions should return list from repository`() = runTest {
        // Arrange
        val category = Category(1, "Food", "restaurant", "#FF9C3E", CategoryType.EXPENSE)
        val transactions = listOf(
            Transaction(
                id = 1,
                amount = 100.0,
                type = TransactionType.EXPENSE,
                category = category,
                note = "Lunch",
                dateTime = System.currentTimeMillis(),
                paymentMethod = PaymentMethod.CASH
            )
        )
        coEvery { mockRepository.getAllTransactions() } returns flowOf(transactions)

        // Act
        val result = getAllTransactionsUseCase()

        // Assert
        result.collect { retrieved ->
            assert(retrieved.size == 1)
            assert(retrieved[0].id == 1L)
        }
    }
}

