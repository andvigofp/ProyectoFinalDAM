package com.example.helpdeskpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.helpdeskpro.navehost.AppNavigation
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

/** Activity that hosts the Compose navigation graph. */
class MainActivity : ComponentActivity() {

    /**
     * Initializes the activity content and applies the application theme.
     *
     * @param savedInstanceState previously saved activity state, if available.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelpdeskProTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}