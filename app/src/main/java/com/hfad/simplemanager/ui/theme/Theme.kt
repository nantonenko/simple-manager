package com.hfad.simplemanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.hfad.simplemanager.ui.theme.elements.Elevation
import com.hfad.simplemanager.ui.theme.elements.LocalElevation

private val DarkColorPalette = darkColors(
    primary = Red900,
    primaryVariant = RedLight900,
    secondary = BlueGray100
)

private val LightColorPalette = lightColors(
    primary = Red900,
    primaryVariant = RedDark900,
    secondary = BlueGray100
)

@Composable
fun SimpleManagerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    CompositionLocalProvider(
        LocalElevation provides Elevation()
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

val theme = MaterialTheme
val MaterialTheme.elevation @Composable get() = LocalElevation.current
val MaterialTheme.spacing @Composable get() = LocalSpacing.current