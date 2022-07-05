package com.hfad.simplemanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Back
import androidx.compose.ui.res.stringResource
import com.hfad.simplemanager.R
import com.hfad.simplemanager.ui.theme.spacing
import com.hfad.simplemanager.ui.theme.theme

@Composable
fun ChangeValueMenu(
    modifier: Modifier = Modifier,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    label: @Composable () -> Unit = {},
    onConfirm: (String) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var innerValue by remember { mutableStateOf(value) }

    Column(modifier = modifier) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = innerValue,
            label = label,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            onValueChange = { innerValue = it }
        )

        ConfirmCancelButtons(
            onConfirm = { onConfirm(innerValue) },
            onCancel = onCancel
        )
    }
}