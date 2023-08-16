package com.husiev.dynassist.components.main

import androidx.lifecycle.ViewModel
import com.husiev.dynassist.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val networkRepository: NetworkRepository,
): ViewModel() {
}