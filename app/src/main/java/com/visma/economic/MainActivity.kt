package com.visma.economic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.visma.presentation.navigation.RootNavigationGraph
import com.visma.presentation.theme.VismaEConomicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VismaEConomicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    RootNavigationGraph()
                }
            }
        }
    }
}