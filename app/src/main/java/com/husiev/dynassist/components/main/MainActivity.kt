package com.husiev.dynassist.components.main

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
import com.husiev.dynassist.components.main.composables.MainScreen
import com.husiev.dynassist.components.start.PrefViewModel
import com.husiev.dynassist.components.start.composables.ThemeConfig
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private lateinit var accountInfo: StartAccountInfo
	private val viewModel: PrefViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		getIntentData()
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
					MainScreen(account = accountInfo)
				}
			}
		}
	}
	
	private fun getIntentData() {
		accountInfo = StartAccountInfo(
			id = intent.getIntExtra("account_id", 0),
			nickname = intent.getStringExtra("nickname") ?: "",
			updateTime = ""
		)
	}
}