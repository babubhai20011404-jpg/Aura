package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AuraEmerald,
    onPrimary = AuraWhite,
    primaryContainer = AuraEmeraldMuted,
    onPrimaryContainer = AuraEmeraldDark,
    secondary = AuraTextSecondary,
    onSecondary = AuraWhite,
    tertiary = AuraAmber,
    onTertiary = AuraWhite,
    background = AuraBackground,
    onBackground = AuraTextPrimary,
    surface = AuraSurfacePrimary,
    onSurface = AuraTextPrimary,
    surfaceVariant = AuraSurfaceSecondary,
    onSurfaceVariant = AuraTextSecondary,
    outline = AuraBorder,
    outlineVariant = AuraBorderSubtle,
    error = AuraRose,
    onError = AuraWhite,
    errorContainer = AuraRoseSurface,
    onErrorContainer = AuraRose
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Ignoring darkTheme for now as per instructions
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = AuraBackground.toArgb()
                window.navigationBarColor = AuraBackground.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = true
                insetsController.isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object AuraTheme {
    object Spacing {
        val xs = 4.dp
        val s = 8.dp
        val m = 12.dp
        val l = 16.dp
        val xl = 20.dp
        val xxl = 24.dp
        val xxxl = 32.dp
        val huge = 48.dp
    }

    object Radius {
        val xs = 8.dp
        val s = 12.dp
        val m = 16.dp
        val l = 20.dp
        val xl = 24.dp
        val xxl = 32.dp
        val pill = 100.dp
    }
    
    object Glass {
        val lightOpacity = 0.7f
        val mediumOpacity = 0.8f
        val elevatedOpacity = 0.9f
        val blur = 16.dp
    }
}
