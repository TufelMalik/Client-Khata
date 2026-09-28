package com.techquantum.tqdkhata.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrandNavy,
    onPrimary = Color.White,
    primaryContainer = BrandCream,
    onPrimaryContainer = BrandNavy,

    secondary = BrandBronze,
    onSecondary = Color.White,
    secondaryContainer = BrandSage,
    onSecondaryContainer = BrandNavy,

    tertiary = BrandSage,
    onTertiary = BrandNavy,

    background = WarmBackground,
    onBackground = TextPrimary,

    surface = WarmSurface,
    onSurface = TextPrimary,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = TextSecondary,

    outline = BrandSage,
    outlineVariant = Color(0xFFE5DECE)
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandCream,
    onPrimary = BrandNavy,
    primaryContainer = BrandNavy,
    onPrimaryContainer = BrandCream,

    secondary = BrandBronze,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E3A5A),
    onSecondaryContainer = BrandCream,

    tertiary = BrandSage,
    onTertiary = BrandNavy,

    background = Color(0xFF061A2D),
    onBackground = Color(0xFFF3E4C9),

    surface = Color(0xFF0A2947),
    onSurface = Color(0xFFFAF7F2),
    surfaceVariant = Color(0xFF123456),
    onSurfaceVariant = Color(0xFFD3D4C0),

    outline = Color(0xFF2C4A6B)
)

@Composable
fun TQDKhataTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
