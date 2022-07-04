package com.hfad.simplemanager.ui.theme.elements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hfad.simplemanager.ui.theme.*

@Composable
fun TransparentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: Dp = theme.elevation.no,
    shape: Shape = theme.shapes.flat,
    border: BorderStroke? = null,
    color: Color = theme.colors.surface.copy(alpha = 0.0f),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor = contentColorFor(backgroundColor = color)
    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .indication(
                interactionSource,
                LocalIndication.current
            ),
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.button
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewNButton() {
    Column(
        verticalArrangement = Arrangement.Top, modifier = Modifier
            .padding(20.dp)
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text("some btn")
        }
        TransparentButton(onClick = { /*TODO*/ }) {
            Text("some btn")
        }
        TransparentButton(onClick = { /*TODO*/ }) {
            Text("some btn 2")
        }
    }
}

