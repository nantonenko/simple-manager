package com.hfad.simplemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hfad.simplemanager.ui.taskListScreen.Task
import com.hfad.simplemanager.ui.taskListScreen.TaskState
import com.hfad.simplemanager.ui.theme.SimpleManagerTheme
import com.hfad.simplemanager.ui.theme.spacing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val s by remember {
                        mutableStateOf(
                            TaskState(
                                title = "very long and interesting name",
                                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem" +
                                        " Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown" +
                                        " printer took a galley of type and scrambled it to make a type specimen book. It has survived " +
                                        "not only five centuries, but also the leap into electronic typesetting, remaining essentially " +
                                        "unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem " +
                                        "Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including " +
                                        "versions of Lorem Ipsum.",
                                points = 10000
                            )
                        )
                    }
                    Surface(modifier = Modifier) {
                        Task(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(com.hfad.simplemanager.ui.theme.theme.spacing.corner)
                                .wrapContentHeight(),
                            state = s
                        )
                    }
                }
            }
        }
    }
}