package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = IndigoContainer,
    onPrimaryContainer = IndigoDark,
    secondary = PurpleAccent,
    onSecondary = Color.White,
    secondaryContainer = PurpleContainer,
    onSecondaryContainer = PurpleAccent,
    tertiary = EmeraldSuccess,
    onTertiary = Color.White,
    tertiaryContainer = EmeraldContainer,
    onTertiaryContainer = EmeraldSuccess,
    background = SlateBackground,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SlateBackground,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = IndigoLight,
    onPrimary = Color.Black,
    primaryContainer = IndigoDark,
    onPrimaryContainer = Color.White,
    secondary = PurpleAccent,
    onSecondary = Color.White,
    tertiary = EmeraldSuccess,
    onTertiary = Color.Black,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    outline = Color(0xFF334155)
)

@Composable
fun ServiHogarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
