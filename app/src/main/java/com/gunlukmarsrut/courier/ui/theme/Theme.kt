package com.gunlukmarsrut.courier.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun colorScheme(tokens: AppColorTokens, dark: Boolean) = if (dark) {
    darkColorScheme(
        primary = tokens.primary,
        onPrimary = tokens.onPrimary,
        primaryContainer = tokens.primaryContainer,
        onPrimaryContainer = tokens.onPrimaryContainer,
        error = tokens.danger,
        onError = tokens.onDanger,
        errorContainer = tokens.dangerContainer,
        onErrorContainer = tokens.onDangerContainer,
        background = tokens.background,
        onBackground = tokens.textPrimary,
        surface = tokens.surface,
        onSurface = tokens.textPrimary,
        surfaceVariant = tokens.surface2,
        onSurfaceVariant = tokens.textSecondary,
        outline = tokens.border,
        outlineVariant = tokens.border,
        scrim = tokens.scrim,
    )
} else {
    lightColorScheme(
        primary = tokens.primary,
        onPrimary = tokens.onPrimary,
        primaryContainer = tokens.primaryContainer,
        onPrimaryContainer = tokens.onPrimaryContainer,
        error = tokens.danger,
        onError = tokens.onDanger,
        errorContainer = tokens.dangerContainer,
        onErrorContainer = tokens.onDangerContainer,
        background = tokens.background,
        onBackground = tokens.textPrimary,
        surface = tokens.surface,
        onSurface = tokens.textPrimary,
        surfaceVariant = tokens.surface2,
        onSurfaceVariant = tokens.textSecondary,
        outline = tokens.border,
        outlineVariant = tokens.border,
        scrim = tokens.scrim,
    )
}

@Composable
fun GunlukMarsrutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val tokens = if (darkTheme) DarkAppColors else LightAppColors
    val scheme = colorScheme(tokens, darkTheme)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = tokens.background.toArgb()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalAppColors provides tokens) {
        MaterialTheme(
            colorScheme = scheme,
            typography = AppMaterialTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

/** Shorthand accessor: `AppColors.success`, `AppColors.textMuted`, etc. */
val MaterialTheme.appColors: AppColorTokens
    @Composable get() = LocalAppColors.current
