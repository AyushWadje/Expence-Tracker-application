package com.example.expencetracker.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Utility functions for currency formatting.
 */
object CurrencyFormatter {

    private var currentCurrency = "INR"

    /**
     * Set the current currency for formatting.
     */
    fun setCurrency(currencyCode: String) {
        currentCurrency = currencyCode
    }

    /**
     * Format a double value as currency string.
     */
    fun formatCurrency(amount: Double): String {
        return try {
            val currency = Currency.getInstance(currentCurrency)
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.currency = currency
            formatter.format(amount)
        } catch (e: IllegalArgumentException) {
            // Fallback to simple formatting
            "₹${String.format("%.2f", amount)}"
        }
    }

    /**
     * Parse a currency string to double.
     */
    fun parseCurrency(currencyString: String): Double? {
        return try {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.parse(currencyString)?.toDouble()
        } catch (e: Exception) {
            // Try simple numeric parsing
            currencyString.filter { it.isDigit() || it == '.' }.toDoubleOrNull()
        }
    }

    /**
     * Get currency symbol for the current currency.
     */
    fun getCurrencySymbol(): String {
        return try {
            Currency.getInstance(currentCurrency).symbol
        } catch (e: Exception) {
            "₹"
        }
    }
}

