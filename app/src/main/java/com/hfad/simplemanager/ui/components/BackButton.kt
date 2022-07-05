package com.hfad.simplemanager.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.theme.theme

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val backStyle = theme.typography.h6.copy(color = theme.typography.h6.color.copy(alpha = 0.6f))
    LeadingIconButton(
        onClick = onClick,
        icon = { Icon(Icons.Default.ArrowBack, null, tint = backStyle.color) },
        text = { Text(stringResource(R.string.back), style = backStyle) }
    )
}