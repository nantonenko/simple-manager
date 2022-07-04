package com.hfad.simplemanager.ui.theme.elements

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Elevation(
    val no: Dp = 0.dp,
    val small: Dp = 3.dp,
    val medium: Dp = 8.dp,
    val large: Dp = 13.dp
)

val LocalElevation = staticCompositionLocalOf { Elevation() }
