package com.example.expencetracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Room entity representing a budget limit for a category in a specific month/year.
 *
 * @property id Unique identifier, auto-generated primary key
 * @property categoryId Foreign key reference to CategoryEntity
 * @property limitAmount Maximum budget amount allowed for this category
 * @property month Month (1-12) for which the budget applies
 * @property year Year for which the budget applies
 */
@Entity(
    tableName = "budgets",
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
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: Long,
    val limitAmount: Double,
    val month: Int,
    val year: Int
)

