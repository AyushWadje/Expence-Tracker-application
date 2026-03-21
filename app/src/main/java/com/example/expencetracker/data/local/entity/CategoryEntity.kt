package com.example.expencetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing an expense/income category.
 *
 * @property id Unique identifier, auto-generated primary key
 * @property name Category name (e.g., Food, Transport, Entertainment)
 * @property iconName Icon identifier/name for UI rendering
 * @property colorHex Hex color code for visual representation
 * @property type Category type: INCOME, EXPENSE, or BOTH
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val type: String // INCOME, EXPENSE, BOTH
)

