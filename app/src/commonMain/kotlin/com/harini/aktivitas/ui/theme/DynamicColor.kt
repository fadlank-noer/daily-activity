package com.harini.aktivitas.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun dynamicColorScheme(darkTheme: Boolean): ColorScheme?
