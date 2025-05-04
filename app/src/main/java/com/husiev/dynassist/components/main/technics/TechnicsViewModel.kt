package com.husiev.dynassist.components.main.technics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.utils.VehicleData
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.asExternalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TechnicsViewModel @Inject constructor(
	private val databaseRepository: DatabaseRepository,
): ViewModel() {
	
	val nickname = databaseRepository.nickname
	
	val vehicleData: StateFlow<List<VehicleData>> =
		databaseRepository.getAllVehiclesStatData(databaseRepository.accountId)
			.map { stat ->
				val onlyTankId = stat.map { it.tankId }.distinct()
				val veh = mutableListOf<VehicleData>()
				databaseRepository.getExactVehiclesShortData(onlyTankId).first { list ->
					list.forEach { item ->
						val lst = stat.filter { it.tankId == item.tankId }
						if (lst.isNotEmpty()) {
							veh.add(item.asExternalModel(lst))
						}
					}
					true
				}
				veh
			}
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyList<VehicleData>()
			)
	
}