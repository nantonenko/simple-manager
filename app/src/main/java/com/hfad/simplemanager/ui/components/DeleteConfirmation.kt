package com.hfad.simplemanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme


@Composable
fun DeleteConfirmation(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val btnStyle = theme.typography.h6

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.are_you_sure),
            style = theme.typography.h5
        )
        
        Divider()
        Spacer(modifier = Modifier.height(theme.spacing.medium))

        LeadingIconButton(
            onClick = onCancel,
            icon = { Icon(Icons.Default.Close, null) },
            text = { Text(stringResource(R.string.No), style = btnStyle) }
        )

        LeadingIconButton(
            onClick = onConfirm,
            icon = { Icon(Icons.Default.Done, null, tint = theme.colors.error) },
            text = { Text(stringResource(R.string.yes), style = btnStyle.copy(color = theme.colors.error)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDeleteConfirmation() {
    DeleteConfirmation(modifier = Modifier.fillMaxWidth())
}