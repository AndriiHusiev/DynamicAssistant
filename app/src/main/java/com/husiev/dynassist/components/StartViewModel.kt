package com.husiev.dynassist.components

import androidx.lifecycle.ViewModel
import com.husiev.dynassist.components.utils.StartAccountInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(

) : ViewModel() {
	private val _accounts = MutableStateFlow(
			mutableListOf(
			StartAccountInfo("load","DTS"), StartAccountInfo("vector"),
			StartAccountInfo("asset"), StartAccountInfo("format","KFC"),
			StartAccountInfo("modifier"), StartAccountInfo("You"),
			StartAccountInfo("png"), StartAccountInfo("logo"),
			StartAccountInfo("element"), StartAccountInfo("content","NIL"),
		)
	)
	private val _searchResult = MutableStateFlow(
		listOf(
			"load","DTS","vector","asset","format","KFC","png","logo","element","content",
			"MaterialThemeColorSchemeOnSecondaryContainer","BTW","modifier","You"
		)
	)
	
	val accounts: StateFlow<List<StartAccountInfo>> = _accounts
	val searchResult: StateFlow<List<String>> = _searchResult
	
	fun addAccount(account: StartAccountInfo) {
		_accounts.value.add(account)
	}
}