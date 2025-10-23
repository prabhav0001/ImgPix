package com.deecode.walls

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.deecode.walls.data.preferences.PreferencesManager
import com.deecode.walls.ui.WallsApp
import com.deecode.walls.ui.theme.WallsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)

        // Enable edge-to-edge with transparent system bars
        enableEdgeToEdge()

        setContent {
            val isDarkTheme by preferencesManager.isDarkTheme.collectAsState(initial = false)
            val scope = rememberCoroutineScope()

            WallsTheme(darkTheme = isDarkTheme) {
                WallsApp(
                    onThemeToggle = {
                        scope.launch {
                            preferencesManager.toggleTheme()
                        }
                    }
                )
            }
        }
    }
}