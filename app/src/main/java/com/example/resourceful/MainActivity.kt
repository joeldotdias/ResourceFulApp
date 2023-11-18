package com.example.resourceful

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.resourceful.presentation.HomeScreen
import com.example.resourceful.ui.theme.ResourceFulTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResourceFulTheme {
                HomeScreen()
            }
        }
    }
}