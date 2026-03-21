package com.example.expencetracker.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore wrapper for managing user preferences.
 */
class UserPreferencesRepository(private val context: Context) {

    private val dataStore = context.preferencesDataStore

    companion object {
        private const val PREFERENCES_NAME = "expense_tracker_prefs"
        private val CURRENCY_KEY = stringPreferencesKey("currency")
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val BIOMETRIC_ENABLED_KEY = booleanPreferencesKey("biometric_enabled")
        private val BUDGET_RESET_DAY_KEY = intPreferencesKey("budget_reset_day")
        private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
    }

    val currencyFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[CURRENCY_KEY] ?: "INR"
    }

    val themeModeFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_MODE_KEY] ?: "SYSTEM"
    }

    val biometricEnabledFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[BIOMETRIC_ENABLED_KEY] ?: false
    }

    val budgetResetDayFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[BUDGET_RESET_DAY_KEY] ?: 1
    }

    val notificationsEnabledFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED_KEY] ?: true
    }

    suspend fun setCurrency(currency: String) {
        dataStore.edit { preferences ->
            preferences[CURRENCY_KEY] = currency
        }
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED_KEY] = enabled
        }
    }

    suspend fun setBudgetResetDay(day: Int) {
        dataStore.edit { preferences ->
            preferences[BUDGET_RESET_DAY_KEY] = day
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }

    suspend fun clearAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "expense_tracker_prefs"
)

