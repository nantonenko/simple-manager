package com.hfad.simplemanager.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hfad.simplemanager.ui.theme.elevation
import com.hfad.simplemanager.ui.theme.flat
import com.hfad.simplemanager.ui.theme.round
import com.hfad.simplemanager.ui.theme.theme

@Composable
fun OutlinedTransparentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: Dp = theme.elevation.no,
    shape: Shape = theme.shapes.round,
    border: BorderStroke? = BorderStroke(width = 2.dp, color = theme.colors.primary.copy(alpha = 0.3f)),
    color: Color = theme.colors.surface.copy(alpha = 0.0f),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    enabled: Boolean = true,
    contentArrangement: Arrangement.Horizontal = Arrangement.Center,
    content: @Composable RowScope.() -> Unit
) {
    TransparentButton(
        onClick,
        modifier,
        interactionSource,
        elevation,
        shape,
        border,
        color,
        contentPadding,
        enabled,
        contentArrangement,
        content
    )
}