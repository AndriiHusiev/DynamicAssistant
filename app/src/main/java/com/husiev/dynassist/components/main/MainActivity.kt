package com.husiev.dynassist.components.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.husiev.dynassist.components.main.composables.MainScreen
import com.husiev.dynassist.components.start.utils.StartAccountInfo
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private lateinit var accountInfo: StartAccountInfo
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		getIntentData()
		setContent {
			DynamicAssistantTheme {
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