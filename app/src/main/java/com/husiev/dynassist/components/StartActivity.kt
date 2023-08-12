package com.husiev.dynassist.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.husiev.dynassist.components.composables.StartScreen
import com.husiev.dynassist.components.composables.ThemeConfig
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		setContent {
			var themeConfig by rememberSaveable { mutableStateOf(ThemeConfig.FOLLOW_SYSTEM) }
			
			DynamicAssistantTheme(useDarkTheme =
				when(themeConfig) {
					ThemeConfig.LIGHT -> false
					ThemeConfig.DARK -> true
					ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
				}
			) {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					StartScreen(
						theme = themeConfig,
						onChangeTheme = { themeConfig = it }
					)
				}
			}
		}
	}
}