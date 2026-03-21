package com.example.expencetracker.data.local.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.expencetracker.data.local.database.ExpenseDatabase
import com.example.expencetracker.data.local.entity.CategoryEntity
import com.example.expencetracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration tests for TransactionDao.
 */
@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {

    private lateinit var database: ExpenseDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, ExpenseDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        transactionDao = database.transactionDao()
        categoryDao = database.categoryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveTransaction() = runTest {
        // Arrange
        val category = CategoryEntity(
            id = 1,
            name = "Food",
            iconName = "restaurant",
            colorHex = "#FF9C3E",
            type = "EXPENSE"
        )
        categoryDao.insert(category)

        val transaction = TransactionEntity(
            id = 1,
            amount = 100.0,
            type = "EXPENSE",
            categoryId = 1,
            note = "Lunch",
            dateTime = System.currentTimeMillis(),
            paymentMethod = "CASH"
        )
        transactionDao.insert(transaction)

        // Act
        val retrieved = transactionDao.getTransactionById(1).first()

        // Assert
        assert(retrieved != null)
        assert(retrieved?.amount == 100.0)
        assert(retrieved?.note == "Lunch")
    }

    @Test
    fun deleteTransaction() = runTest {
        // Arrange
        val category = CategoryEntity(
            id = 1,
            name = "Food",
            iconName = "restaurant",
            colorHex = "#FF9C3E",
            type = "EXPENSE"
        )
        categoryDao.insert(category)

        val transaction = TransactionEntity(
            id = 1,
            amount = 100.0,
            type = "EXPENSE",
            categoryId = 1,
            note = "Lunch",
            dateTime = System.currentTimeMillis(),
            paymentMethod = "CASH"
        )
        transactionDao.insert(transaction)
        transactionDao.delete(transaction)

        // Act
        val retrieved = transactionDao.getTransactionById(1).first()

        // Assert
        assert(retrieved == null)
    }

    @Test
    fun updateTransaction() = runTest {
        // Arrange
        val category = CategoryEntity(
            id = 1,
            name = "Food",
            iconName = "restaurant",
            colorHex = "#FF9C3E",
            type = "EXPENSE"
        )
        categoryDao.insert(category)

        val transaction = TransactionEntity(
            id = 1,
            amount = 100.0,
            type = "EXPENSE",
            categoryId = 1,
            note = "Lunch",
            dateTime = System.currentTimeMillis(),
            paymentMethod = "CASH"
        )
        transactionDao.insert(transaction)

        val updatedTransaction = transaction.copy(amount = 150.0)
        transactionDao.update(updatedTransaction)

        // Act
        val retrieved = transactionDao.getTransactionById(1).first()

        // Assert
        assert(retrieved?.amount == 150.0)
    }

    @Test
    fun getAllTransactions() = runTest {
        // Arrange
        val category = CategoryEntity(
            id = 1,
            name = "Food",
            iconName = "restaurant",
            colorHex = "#FF9C3E",
            type = "EXPENSE"
        )
        categoryDao.insert(category)

        val transactions = listOf(
            TransactionEntity(
                id = 1,
                amount = 100.0,
                type = "EXPENSE",
                categoryId = 1,
                note = "Lunch",
                dateTime = System.currentTimeMillis(),
                paymentMethod = "CASH"
            ),
            TransactionEntity(
                id = 2,
                amount = 200.0,
                type = "EXPENSE",
                categoryId = 1,
                note = "Dinner",
                dateTime = System.currentTimeMillis(),
                paymentMethod = "CARD"
            )
        )
        transactions.forEach { transactionDao.insert(it) }

        // Act
        val retrieved = transactionDao.getAllTransactions().first()

        // Assert
        assert(retrieved.size == 2)
    }
}

