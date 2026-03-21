package com.example.expencetracker.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility functions for date and time formatting.
 */
object DateTimeFormatter {

    /**
     * Format epoch milliseconds to a readable date string.
     */
    fun formatDate(timeInMillis: Long): String {
        return try {
            val date = Date(timeInMillis)
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Format epoch milliseconds to a date and time string.
     */
    fun formatDateTime(timeInMillis: Long): String {
        return try {
            val date = Date(timeInMillis)
            val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Format epoch milliseconds to just the time.
     */
    fun formatTime(timeInMillis: Long): String {
        return try {
            val date = Date(timeInMillis)
            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Format epoch milliseconds to relative time (e.g., "2 days ago").
     */
    fun formatRelativeTime(timeInMillis: Long): String {
        val now = System.currentTimeMillis()
        val diffInMillis = now - timeInMillis
        val diffInSeconds = diffInMillis / 1000
        val diffInMinutes = diffInSeconds / 60
        val diffInHours = diffInMinutes / 60
        val diffInDays = diffInHours / 24

        return when {
            diffInSeconds < 60 -> "Just now"
            diffInMinutes < 60 -> "$diffInMinutes minutes ago"
            diffInHours < 24 -> "$diffInHours hours ago"
            diffInDays < 7 -> "$diffInDays days ago"
            diffInDays < 30 -> "${diffInDays / 7} weeks ago"
            else -> formatDate(timeInMillis)
        }
    }

    /**
     * Get the day of month from epoch milliseconds.
     */
    fun getDayOfMonth(timeInMillis: Long): Int {
        return try {
            val date = Date(timeInMillis)
            val formatter = SimpleDateFormat("dd", Locale.getDefault())
            formatter.format(date).toInt()
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Get the month from epoch milliseconds (1-12).
     */
    fun getMonth(timeInMillis: Long): Int {
        return try {
            val date = Date(timeInMillis)
            val formatter = SimpleDateFormat("MM", Locale.getDefault())
            formatter.format(date).toInt()
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Get the year from epoch milliseconds.
     */
    fun getYear(timeInMillis: Long): Int {
        return try {
            val date = Date(timeInMillis)
            val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
            formatter.format(date).toInt()
        } catch (e: Exception) {
            0
        }
    }
}

