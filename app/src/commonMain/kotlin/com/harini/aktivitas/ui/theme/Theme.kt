package com.harini.aktivitas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AktivitasHARINITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color (Material You) via expect/actual — null di platform yang tidak support
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = dynamicColorScheme(darkTheme)
        ?: when {
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
