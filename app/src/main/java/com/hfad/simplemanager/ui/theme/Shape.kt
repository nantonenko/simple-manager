package com.hfad.simplemanager.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

val Shapes.round get() = RoundedCornerShape(25.dp)
val Shapes.flat get() = RoundedCornerShape(0.dp)