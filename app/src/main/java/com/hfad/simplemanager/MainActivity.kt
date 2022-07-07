package com.hfad.simplemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.hfad.simplemanager.ui.MainScreen
import com.hfad.simplemanager.ui.TaskListScreen.*
import com.hfad.simplemanager.ui.theme.SimpleManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainScreen(
                        (application as SimpleManagerApp).taskScreenVM,
                        (application as SimpleManagerApp).projectScreenVM
                    )
                }
            }
        }
    }
}