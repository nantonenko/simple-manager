package com.hfad.simplemanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.theme.theme

@Composable
fun ConfirmCancelButtons(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Column(modifier) {
        LeadingIconButton(
            onClick = onConfirm,
            icon = { Icon(Icons.Default.Done, null) },
            text = { Text(stringResource(R.string.apply), style = theme.typography.h6) }
        )

        LeadingIconButton(
            onClick = onCancel,
            icon = { Icon(Icons.Default.Close, null, tint = theme.colors.error) },
            text = { Text(stringResource(R.string.cancel), style = theme.typography.h6.copy(color = theme.colors.error)) }
        )
    }
}