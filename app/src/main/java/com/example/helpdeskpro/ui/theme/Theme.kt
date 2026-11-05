package com.example.helpdeskpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.helpdeskpro.R

/**
 * Aplica el tema visual de Helpdesk Pro al contenido Compose.
 *
 * @param darkTheme indica si se debe usar el esquema oscuro.
 * @param dynamicColor indica si se deben usar colores dinámicos del sistema cuando estén disponibles.
 * @param content contenido Compose al que se aplica el tema.
 */
@Composable
fun HelpdeskProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val appLightColorScheme = lightColorScheme(
        primary = colorResource(R.color.md_primary),
        onPrimary = colorResource(R.color.md_on_primary),
        primaryContainer = colorResource(R.color.md_primary_container),
        onPrimaryContainer = colorResource(R.color.md_on_primary_container),
        secondary = colorResource(R.color.md_secondary),
        onSecondary = colorResource(R.color.md_on_secondary),
        secondaryContainer = colorResource(R.color.md_secondary_container),
        onSecondaryContainer = colorResource(R.color.md_on_secondary_container),
        tertiary = colorResource(R.color.md_tertiary),
        onTertiary = colorResource(R.color.md_on_tertiary),
        background = colorResource(R.color.md_background),
        onBackground = colorResource(R.color.md_on_background),
        surface = colorResource(R.color.md_surface),
        onSurface = colorResource(R.color.md_on_surface),
        surfaceVariant = colorResource(R.color.md_surface_variant),
        onSurfaceVariant = colorResource(R.color.md_on_surface_variant),
        outline = colorResource(R.color.md_outline),
        outlineVariant = colorResource(R.color.md_outline_variant),
        error = colorResource(R.color.md_error),
        onError = colorResource(R.color.md_on_error)
    )
    val appDarkColorScheme = darkColorScheme(
        primary = colorResource(R.color.md_primary_dark),
        onPrimary = colorResource(R.color.md_on_primary_dark),
        primaryContainer = colorResource(R.color.md_primary_container_dark),
        onPrimaryContainer = colorResource(R.color.md_on_primary_container_dark),
        secondary = colorResource(R.color.md_secondary_dark),
        onSecondary = colorResource(R.color.md_on_secondary_dark),
        secondaryContainer = colorResource(R.color.md_secondary_container_dark),
        onSecondaryContainer = colorResource(R.color.md_on_secondary_container_dark),
        tertiary = colorResource(R.color.md_tertiary_dark),
        onTertiary = colorResource(R.color.md_on_tertiary_dark),
        background = colorResource(R.color.md_background_dark),
        onBackground = colorResource(R.color.md_on_background_dark),
        surface = colorResource(R.color.md_surface_dark),
        onSurface = colorResource(R.color.md_on_surface_dark),
        surfaceVariant = colorResource(R.color.md_surface_variant_dark),
        onSurfaceVariant = colorResource(R.color.md_on_surface_variant_dark),
        outline = colorResource(R.color.md_outline_dark),
        outlineVariant = colorResource(R.color.md_outline_variant_dark),
        error = colorResource(R.color.md_error_dark),
        onError = colorResource(R.color.md_on_error_dark)
    )
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> appDarkColorScheme
        else -> appLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}