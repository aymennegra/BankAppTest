package com.example.bankapp.android.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Color.Black, // Bleu iOS pour les accents
    onPrimary = Color.White,
    primaryContainer = Color.White, // Fond blanc pour TopBar
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF9E9E9E), // Gris pour textes secondaires
    onSecondary = Color.White,
    secondaryContainer = Color.White, // Fond blanc pour les items
    onSecondaryContainer = Color.Black,
    tertiary = Color(0xFF666666),
    onTertiary = Color.White,
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    background = Color(0xFFF5F5F5), // Fond gris clair principal
    onBackground = Color.Black,
    surface = Color.White, // Surface blanche pour les cards/items
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF5F5F5), // Variante gris clair
    onSurfaceVariant = Color(0xFF9E9E9E)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1C1C1E),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF8E8E93),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF2C2C2E),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF98989D),
    onTertiary = Color.White,
    error = Color(0xFFFF453A),
    onError = Color.White,
    background = Color(0xFF000000),
    onBackground = Color.White,
    surface = Color(0xFF1C1C1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFF8E8E93)
)

@Composable
fun BankAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // StatusBar avec le fond approprié
            window.statusBarColor = if (darkTheme) {
                colorScheme.background.toArgb()
            } else {
                Color.White.toArgb()
            }
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}