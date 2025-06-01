package com.example.android.appsolutechaos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android.appsolutechaos.ui.theme.AppsoluteChaosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppsoluteChaosTheme {
                AppsoluteChaosApp()
            }
        }
    }
} 