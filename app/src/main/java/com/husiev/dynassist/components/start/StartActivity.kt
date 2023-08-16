package com.husiev.dynassist.components.start

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.husiev.dynassist.components.start.composables.StartScreen
import com.husiev.dynassist.components.start.composables.ThemeConfig
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : ComponentActivity() {
	private val viewModel: StartPrefViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		setContent {
			val uiState by viewModel.uiState.collectAsStateWithLifecycle()
			
			DynamicAssistantTheme(useDarkTheme =
				when(uiState.theme) {
					ThemeConfig.LIGHT -> false
					ThemeConfig.DARK -> true
					else -> isSystemInDarkTheme()
				}
			) {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					StartScreen(
						theme = uiState.theme,
						onChangeTheme = viewModel::setTheme
					)
				}
			}
		}
	}
}