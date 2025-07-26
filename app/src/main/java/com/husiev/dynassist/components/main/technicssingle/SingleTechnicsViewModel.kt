package com.husiev.dynassist.components.main.technicssingle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.utils.VehicleData
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.asUiSingleTechnicModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SingleTechnicsViewModel @Inject constructor(
	databaseRepository: DatabaseRepository,
	savedStateHandle: SavedStateHandle
): ViewModel() {
	
	private val selectedId: Int = savedStateHandle.get<Int>(TECHNICS_SINGLE_ARG) ?: -1
	
	val vehicleData: StateFlow<VehicleData?> =
		databaseRepository.getFlatVehicleData(selectedId)
			.map { list ->
				withContext(Dispatchers.Default) {
					list.asUiSingleTechnicModel()
				}
			}
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
	
}