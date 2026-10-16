package com.example.sshmanager.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7EE2A8),
    onPrimary = Color(0xFF00391C),
    primaryContainer = Color(0xFF0E522E),
    onPrimaryContainer = Color(0xFF9BFFC6),
    secondary = Color(0xFF8ED0CE),
    tertiary = Color(0xFFC5C2E9),
    background = Color(0xFF101418),
    onBackground = Color(0xFFE0E3E6),
    surface = Color(0xFF101418),
    onSurface = Color(0xFFE0E3E6),
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF1D6B3F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFA5F4C3),
    onPrimaryContainer = Color(0xFF00210E),
    secondary = Color(0xFF2A6A68),
    tertiary = Color(0xFF4A4A79),
)

@Composable
fun SshManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
