package com.example.expencetracker.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.expencetracker.domain.usecase.GetBudgetsByMonthYearUseCase
import com.example.expencetracker.domain.usecase.GetMonthlyExpenseByCategoryUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.time.LocalDate

/**
 * WorkManager worker for checking budget alerts.
 * Runs daily to check if any category has exceeded 80% or 100% of its budget.
 */
@HiltWorker
class BudgetCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getBudgetsByMonthYearUseCase: GetBudgetsByMonthYearUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val now = LocalDate.now()
            val month = now.monthValue
            val year = now.year

            // Get budgets for the current month
            val budgets = getBudgetsByMonthYearUseCase(month, year).first()

            Timber.d("Checking budgets for $month/$year. Found ${budgets.size} budgets.")

            // For each budget, check if it exceeds 80% or 100%
            budgets.forEach { budget ->
                // TODO: Calculate actual spending for this category and check thresholds
                // TODO: Send notifications if thresholds are exceeded
            }

            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Error checking budgets")
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "BudgetCheckWork"
    }
}

