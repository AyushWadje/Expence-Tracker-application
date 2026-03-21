package com.example.expencetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expencetracker.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 * Manages the state and logic for user preferences and settings.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadPreferences()
    }

    /**
     * Load user preferences.
     */
    private fun loadPreferences() {
        viewModelScope.launch {
            try {
                userPreferencesRepository.currencyFlow.collect { currency ->
                    _uiState.value = _uiState.value.copy(currency = currency)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }

        viewModelScope.launch {
            try {
                userPreferencesRepository.themeModeFlow.collect { theme ->
                    _uiState.value = _uiState.value.copy(themeMode = theme)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }

        viewModelScope.launch {
            try {
                userPreferencesRepository.biometricEnabledFlow.collect { enabled ->
                    _uiState.value = _uiState.value.copy(biometricEnabled = enabled)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }

        viewModelScope.launch {
            try {
                userPreferencesRepository.budgetResetDayFlow.collect { day ->
                    _uiState.value = _uiState.value.copy(budgetResetDay = day)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }

        viewModelScope.launch {
            try {
                userPreferencesRepository.notificationsEnabledFlow.collect { enabled ->
                    _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * Handle events on the settings screen.
     */
    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.UpdateCurrency -> {
                updateCurrency(event.currency)
            }
            is SettingsEvent.UpdateThemeMode -> {
                updateThemeMode(event.mode)
            }
            is SettingsEvent.UpdateBiometricEnabled -> {
                updateBiometricEnabled(event.enabled)
            }
            is SettingsEvent.UpdateBudgetResetDay -> {
                updateBudgetResetDay(event.day)
            }
            is SettingsEvent.UpdateNotificationsEnabled -> {
                updateNotificationsEnabled(event.enabled)
            }
        }
    }

    /**
     * Update currency preference.
     */
    private fun updateCurrency(currency: String) {
        viewModelScope.launch {
            try {
                userPreferencesRepository.setCurrency(currency)
                _uiState.value = _uiState.value.copy(currency = currency)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * Update theme mode preference.
     */
    private fun updateThemeMode(mode: String) {
        viewModelScope.launch {
            try {
                userPreferencesRepository.setThemeMode(mode)
                _uiState.value = _uiState.value.copy(themeMode = mode)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * Update biometric lock preference.
     */
    private fun updateBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                userPreferencesRepository.setBiometricEnabled(enabled)
                _uiState.value = _uiState.value.copy(biometricEnabled = enabled)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * Update budget reset day preference.
     */
    private fun updateBudgetResetDay(day: Int) {
        viewModelScope.launch {
            try {
                userPreferencesRepository.setBudgetResetDay(day)
                _uiState.value = _uiState.value.copy(budgetResetDay = day)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * Update notifications preference.
     */
    private fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                userPreferencesRepository.setNotificationsEnabled(enabled)
                _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
}

/**
 * UI State for the Settings screen.
 */
data class SettingsUiState(
    val currency: String = "INR",
    val themeMode: String = "SYSTEM",
    val biometricEnabled: Boolean = false,
    val budgetResetDay: Int = 1,
    val notificationsEnabled: Boolean = true,
    val appVersion: String = "1.0.0",
    val errorMessage: String? = null
)

/**
 * Events for the Settings screen.
 */
sealed class SettingsEvent {
    data class UpdateCurrency(val currency: String) : SettingsEvent()
    data class UpdateThemeMode(val mode: String) : SettingsEvent()
    data class UpdateBiometricEnabled(val enabled: Boolean) : SettingsEvent()
    data class UpdateBudgetResetDay(val day: Int) : SettingsEvent()
    data class UpdateNotificationsEnabled(val enabled: Boolean) : SettingsEvent()
}

