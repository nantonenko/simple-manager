package com.hfad.simplemanager.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LeadingIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit = {},
    text: @Composable () -> Unit = {}
) = TransparentButton(modifier = modifier, onClick = onClick) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.align(Alignment.CenterStart)) { icon() }
        Box(modifier = Modifier.align(Alignment.Center)) { text() }
    }
}
