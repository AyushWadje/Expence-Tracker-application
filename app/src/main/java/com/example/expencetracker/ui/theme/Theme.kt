package com.example.expencetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Dark color scheme for Material Design 3.
 */
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = TextPrimaryDark,
    primaryContainer = PrimaryDarkBlue,
    onPrimaryContainer = PrimaryLightBlue,
    secondary = SecondaryGreen,
    onSecondary = TextPrimaryDark,
    secondaryContainer = SecondaryLightGreen,
    onSecondaryContainer = TextPrimaryDark,
    tertiary = TertiaryOrange,
    onTertiary = TextPrimaryDark,
    tertiaryContainer = TertiaryLightOrange,
    onTertiaryContainer = TextPrimaryDark,
    error = ErrorRed,
    onError = TextPrimaryDark,
    errorContainer = Color(0xFF5D1B1B),
    onErrorContainer = ErrorRed,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFF424242)
)

/**
 * Light color scheme for Material Design 3.
 */
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = PrimaryLightBlue,
    onPrimaryContainer = TextPrimaryLight,
    secondary = SecondaryGreen,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = SecondaryLightGreen,
    onSecondaryContainer = TextPrimaryLight,
    tertiary = TertiaryOrange,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = TertiaryLightOrange,
    onTertiaryContainer = TextPrimaryLight,
    error = ErrorRed,
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = ErrorRed,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFBDBDBD),
    outlineVariant = Color(0xFFE0E0E0)
)

/**
 * Expense Tracker theme composable.
 * Applies Material Design 3 theming with light/dark mode support.
 *
 * @param darkTheme Whether to use dark theme (defaults to system preference)
 * @param content The composable content to apply the theme to
 */
@Composable
fun ExpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkMode(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

