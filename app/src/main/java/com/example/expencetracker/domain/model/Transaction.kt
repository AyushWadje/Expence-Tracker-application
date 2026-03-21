package com.example.expencetracker.domain.model

/**
 * Domain model representing a transaction (income or expense).
 * This is independent of the database layer and used throughout the application.
 *
 * @property id Unique identifier for the transaction
 * @property amount Transaction amount
 * @property type Type of transaction: INCOME or EXPENSE
 * @property category The category associated with this transaction
 * @property note Optional note/description
 * @property dateTime Transaction timestamp in epoch milliseconds
 * @property paymentMethod Payment method used: CASH, CARD, or UPI
 * @property receiptImageUri Optional URI to a receipt image
 * @property isRecurring Whether this transaction repeats
 * @property recurringInterval Interval for recurring transactions
 */
data class Transaction(
    val id: Long,
    val amount: Double,
    val type: TransactionType,
    val category: Category,
    val note: String = "",
    val dateTime: Long,
    val paymentMethod: PaymentMethod,
    val receiptImageUri: String? = null,
    val isRecurring: Boolean = false,
    val recurringInterval: RecurringInterval? = null
)

/**
 * Enumeration for transaction types.
 */
enum class TransactionType {
    INCOME, EXPENSE
}

/**
 * Enumeration for payment methods.
 */
enum class PaymentMethod {
    CASH, CARD, UPI
}

/**
 * Enumeration for recurring transaction intervals.
 */
enum class RecurringInterval {
    DAILY, WEEKLY, MONTHLY, YEARLY
}

