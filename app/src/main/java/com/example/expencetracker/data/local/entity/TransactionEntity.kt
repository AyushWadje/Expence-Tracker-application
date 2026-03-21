package com.example.expencetracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Room entity representing a transaction (income or expense).
 *
 * @property id Unique identifier, auto-generated primary key
 * @property amount Transaction amount in double format
 * @property type Type of transaction: INCOME or EXPENSE
 * @property categoryId Foreign key reference to CategoryEntity
 * @property note Optional note/description for the transaction
 * @property dateTime Timestamp in epoch milliseconds
 * @property paymentMethod Payment method: CASH, CARD, or UPI
 * @property receiptImageUri Optional URI to receipt image
 * @property isRecurring Whether this transaction repeats
 * @property recurringInterval Interval for recurring transactions (e.g., MONTHLY, WEEKLY)
 */
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("categoryId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: String, // INCOME or EXPENSE
    val categoryId: Long,
    val note: String = "",
    val dateTime: Long,
    val paymentMethod: String, // CASH, CARD, UPI
    val receiptImageUri: String? = null,
    val isRecurring: Boolean = false,
    val recurringInterval: String? = null // DAILY, WEEKLY, MONTHLY, YEARLY
)

